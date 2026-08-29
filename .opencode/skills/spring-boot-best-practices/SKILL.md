---
name: spring-boot-best-practices
description: Use when creating or modifying Java Spring Boot code in projetos/back-end — controllers, services, repositories, DTOs, validations, Flyway migrations, security or tests.
---

# Spring Boot Best Practices — O.M.C.E

Applies to `projetos/back-end/` (Java 23, Spring Boot 3.4.3, Maven, MySQL 8, Flyway, Spring Security + JWT `com.auth0:java-jwt`).

## 1. Architecture — preserve `controller → service → repository`

```
controller/       → thin HTTP layer, no business logic, no direct repo access
  *service/ / User/Service/ → business rules, orchestration, calls Validacao + repositories
repository/       → Spring Data JPA interfaces only (extend JpaRepository)
dto/             → records for I/O (DadosCadastro*, DadosAlterar*, *RespostaDTO)
Validacao/       → @Component validators (ValidacaoProduto, ValidacaoUser)
Execao/          → domain exceptions + Execao/global/GlobalExceptionHandler
config/          → SecurityConfig, SecurityFilter, TokenService, CorsConfig
```

- Never put business logic in controllers. Never inject `EntityManager` or call repositories from controllers — delegate to service.
- Reuse existing DTOs, entities, enums, exceptions before creating new ones. Check `Produto/dto/`, `User/dto/`, `Produto/enums/` first.
- Package = domain (`OMCE.OMCE.Produto`, `OMCE.OMCE.User`, etc.). Keep `@Entity` classes there, not in a generic `model/` folder.

## 2. DTOs and Entities

- DTOs are `record` with `jakarta.validation.constraints.@NotNull` where required (`projetos/back-end/src/main/java/OMCE/OMCE/Produto/dto/DadosCadastroProduto.java:5`). Keep field names in Portuguese (idioma do projeto).
- Response DTOs expose only needed fields; map via constructor `new ProdutoRespostaDTO(produto)` — never expose entity directly.
- Entities: Portuguese fields, `@Entity @Table`, `@ManyToOne(fetch=LAZY) + @JsonBackReference` / `@OneToMany(mappedBy) + @JsonManagedReference` to avoid cycles (`Produto.java`, `User.java`). Image stored as `byte[] @Lob` + `imagem_tipo` column — encode/decode with `Base64`.
- Business state in entity methods (`alterarDados()`, constructors from DTO) — keep setters minimal.

## 3. Validation

- Two layers: `jakarta.validation` annotations on DTOs + explicit `Validacao*` component (`Validacao/ValidacaoProduto.java:7`). Service must call `validar.ValidarCadastroProduto(dados)` before persisting.
- Validation throws `RuntimeException` with Portuguese message; `GlobalExceptionHandler` maps to `400 BAD_REQUEST` with `mensagem` field. For domain not-found use `ProdutoNaoEncontrado` / `UserNaoEncontrado` → `404`.
- Never rely only on client validation. Always validate `preco > 0`, `nome != null`, `imagem != null` server-side.

## 4. Controllers

Pattern (`ProdutoController.java:23`):
```java
@RestController @RequestMapping("/produto")
public class ProdutoController {
    private final ProdutoService service; // constructor injection
    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping("/cadastroProduto")
    public ResponseEntity<Void> cadastroProduto(@RequestBody DadosCadastroProduto dados) {
        service.cadastro(dados);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/filtro")
    public ResponseEntity<Page<ProdutoRespostaDTO>> filtrarProdutos(
        @RequestParam(required=false) String nome,
        @RequestParam(required=false) String categoria,
        @PageableDefault(size=10) Pageable pageable) { ... }
}
```

- Use `@PageableDefault(size=10)` for paginated `GET`s. Return `Page<DTO>` directly.
- `@Transactional` only on mutating endpoints that need it (`@DeleteMapping`, `@PutMapping`).
- Header-based user context: `@RequestHeader("Id-Usuario") Long id` for user-scoped queries (`todosProdutosUsuario`).
- Preserve existing HTTP codes and `mensagem` error shape — consumers (`front-end-react/src/api.js`, `mobile/src/lib/api.ts`) parse `body.mensagem || body.message`.

## 5. Services

- Annotated `@Service @Slf4j`, constructor injection only (no field `@Autowired` except legacy controllers — prefer constructor).
- Pattern (`ProdutoService.java:20`): validate → fetch `User` via `UserService.pegarUserPorId` → map DTO → `repository.save`.
- For filtered queries: convert `String categoria` → `Categoria.valueOf(categoria.toUpperCase())` with `catch IllegalArgumentException → throw CategoriaInvalida`.
- Image handling: `Base64.getDecoder().decode(dados.imagem())` on write, `Base64.getEncoder().encodeToString(produto.getImagem())` on read (`pegarDetalhesDoProduto`).

## 6. Repositories

- Extend `JpaRepository<Produto, Long>`; add `@Query` JPQL for filtering (`ProdutoRepository.java:12`):
```java
@Query("""
 SELECT p FROM Produto p
 WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
 AND (:categoria IS NULL OR p.categoria = :categoria)
 AND p.vendido = false""")
Page<Produto> filtrarProdutos(@Param("nome") String nome, ... , Pageable pageable);
```
- Use `@Modifying @Query("UPDATE Produto p SET p.vendido = true WHERE p.id = :id")` for bulk state changes.
- Never write native SQL unless JPQL cannot express it.

## 7. Error Handling

- `Execao/global/GlobalExceptionHandler.java:14` is `@RestControllerAdvice`. Add a handler per domain exception → appropriate `HttpStatus` (`NOT_FOUND` for `*NaoEncontrado`, `CONFLICT` for `CategoriaInvalida`/`SenhaIgualAOriginal`, `BAD_REQUEST` for validation).
- Response shape (`GlobalExceptionHandler.buildResponse`):
```json
{ "timestamp": "...", "status": 404, "error": "Not Found", "message": "Produto não encontrado", "path": "/produto/..." }
```
- Frontend expects `mensagem` or `message` — always set one.

## 8. Security and Auth

- `SecurityConfig.java:28` — `csrf.disable()`, `CORS` from `CorsConfig`, `SessionCreationPolicy.STATELESS`, `SecurityFilter` before `UsernamePasswordAuthenticationFilter`.
- Public endpoints explicitly `permitAll()`: `POST /user/cadastro`, `POST /auth/login`, `POST /user/redefinirSenha`, `PUT /user/novaSenha`, `GET /produto/filtro*`, `GET /produto/visualizarDetalhesProduto/**`, Swagger. Everything else `.authenticated()`.
- `SecurityFilter.java:16` — reads `Authorization: Bearer <token>`, validates via `TokenService.validateToken(email)`, loads `UserDetails` from `UserRepository.findByEmail`.
- `TokenService` uses `com.auth0:java-jwt 4.5.0`. Never store token in DB; validate on every request.
- When adding new endpoints, decide `permitAll()` vs authenticated and update `SecurityConfig` accordingly.

## 9. Persistence and Flyway

- Migrations: `src/main/resources/db/migration/V{NN}__descricao.sql` (`V1__`..`V17__` exists). Never edit applied migrations — create next `V18__` with incremental `ALTER`/`CREATE`. `spring.flyway.out-of-order=true`, `spring.jpa.hibernate.ddl-auto=none` in prod (`application.properties:6`), `update` only in `application-test.yml:9`.
- Entities map to MySQL 8 tables; use `@Column(name="imagem_tipo")`, `@Enumerated(EnumType.STRING)`, `@Lob` for images. Test DB is `127.0.0.1:3307/omce_test` (profile `test`, `replace: none` → real MySQL), dev DB is `localhost:3306/omce`.

## 10. Testing

- Unit: `src/test/java/OMCE/OMCE/unitarios/*Test.java` — `@ExtendWith(MockitoExtension.class)`, `@Mock` repositories/services, `@InjectMocks` service under test, verify via `when(...).thenReturn` + `assertThrows` for validation paths (`ProdutoServiceTest.java:40`).
- Integration: `src/test/java/OMCE/OMCE/integracao/*` with `@SpringBootTest` + `application-test.yml` profile. Requires test DB: `cd projetos/back-end && docker compose up -d mysql_db` (port 3307) before `./mvnw test`.
- Always add/adjust tests when changing behavior, repository query, or endpoint contract.

## 11. Config and Secrets

- `application.properties` contains committed `senha`/`senha_root` and Gmail app password — treat as existing exposure, do not copy into new code/logs/docs. Use env vars (`SPRING_DATASOURCE_*`, `SPRING_MAIL_*`) for new secrets.
- Build: `mvn -q -DskipTests dependency:go-offline` then `mvn -q -DskipTests package` in Docker (`Dockerfile:1` uses `maven:3.9.9-eclipse-temurin-23`).

## Checklist before commit

- [ ] Controller only delegates to service; service validates and handles business rules
- [ ] DTOs are records with `@NotNull` and Portuguese names; entity not exposed directly
- [ ] `GlobalExceptionHandler` covers new exceptions with correct status + `mensagem`
- [ ] Flyway migration is incremental `V18__` (or next free number), not an edit
- [ ] `SecurityConfig` updated if endpoint visibility changed
- [ ] Tests added/updated; `./mvnw test` passes with test DB on 3307
