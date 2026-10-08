# omce-reuso

Componentes reutilizáveis do **O.M.C.E**, empacotados como biblioteca Maven.
Não depende de Spring nem de JPA: usa apenas o JDK (Java 17+).

```xml
<dependency>
    <groupId>OMCE</groupId>
    <artifactId>omce-reuso</artifactId>
    <version>1.0.0</version>
</dependency>
```

## O que a biblioteca oferece

### `OMCE.reuso.strategy.StrategyRegistry<K, S>` (Strategy)
Registro genérico de estratégias indexadas por uma chave (normalmente um enum).

```java
var registro = new StrategyRegistry<>(lista, PagamentoStrategy::getMetodo, "Metodo de pagamento");
PagamentoStrategy pix = registro.escolher(MetodoPagamento.PIX);
```

- `escolher(chave)`: devolve a estratégia ou lança `IllegalArgumentException`.
- `suporta(chave)` e `todas()`.
- Chave duplicada na criação lança `IllegalStateException`.

### `OMCE.reuso.template.CadastroTemplate<D, U, E>` (Template Method)
Esqueleto do cadastro: `validar → buscarUsuario → criar → definirUsuario → configurar → salvar`.
A subclasse implementa cada passo; o fluxo é definido uma única vez em `cadastrar(dados)`.

## Versionamento

[SemVer](https://semver.org/lang/pt-BR/): `MAJOR.MINOR.PATCH`.

| Mudança | Exemplo |
|---|---|
| Correção sem alterar a API | 1.0.0 → 1.0.1 |
| Funcionalidade nova compatível | 1.0.0 → 1.1.0 |
| Quebra de compatibilidade | 1.0.0 → 2.0.0 |

## Build

```bash
mvn clean install   # compila, roda os testes e instala em ~/.m2
```

Gera em `target/`: `omce-reuso-1.0.0.jar`, `-sources.jar` e `-javadoc.jar`.

## Licença

MIT.
