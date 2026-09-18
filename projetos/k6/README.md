# Testes k6 — O.M.C.E

Dois scripts, com papéis bem separados:

| Script | O que faz | Escreve no banco? |
|---|---|---|
| `seed.js` | Cria **50 usuários** e **250 produtos** de teste via API | **Sim** — dados permanentes |
| `stress.js` | Rampa de carga até 200 VUs nos endpoints de leitura | **Não** — só `GET` + `POST /auth/login` |

Como o `stress.js` não grava nada, pode ser executado quantas vezes quiser: o banco fica
exatamente como estava. Por isso não existe script de limpeza.

---

## Pré-requisitos

Subir o ambiente (MySQL + backend na porta 8080):

```bash
cd .. && docker compose up -d
```

Confirmar que o backend respondeu (deve devolver um JSON de página com `totalElements`):

```bash
curl -s "http://localhost:8080/produto/filtro?size=1"
```

> Não use `/v3/api-docs` como verificação: hoje esse endpoint devolve 500 por uma
> incompatibilidade entre o springdoc 2.2.0 e o Spring Boot 3.4.3 do projeto. É um problema
> pré-existente e não afeta os testes.

O k6 **não precisa estar instalado** — roda pela imagem oficial.

---

## Como rodar

A partir desta pasta (`projetos/k6`):

```bash
docker run --rm -i --network host --user "$(id -u):$(id -g)" -v "$PWD:/k6" -w /k6 grafana/k6 run seed.js
```

```bash
docker run --rm -i --network host --user "$(id -u):$(id -g)" -v "$PWD:/k6" -w /k6 grafana/k6 run stress.js
```

Detalhes da linha de comando:

- `--network host` — é o que faz `localhost:8080` funcionar de dentro do container (Linux).
- `--user "$(id -u):$(id -g)"` — a imagem roda com uid 12345; sem isso os arquivos em `out/`
  saem pertencendo ao root.
- `-v "$PWD:/k6" -w /k6` — monta esta pasta dentro do container.

Se preferir o k6 instalado nativamente, é só `k6 run seed.js`.

---

## `seed.js`

Cria 50 usuários com role **`MISTO`**, cada um cadastrando 5 produtos → **50 usuários e
250 produtos**.

`MISTO` é usado de propósito: concede `ROLE_VENDEDOR` (necessário para
`POST /produto/cadastroProduto`) **e** `ROLE_COMPRADOR` no mesmo usuário, então essas 50 contas
servem para qualquer cenário autenticado depois.

Cada iteração faz: `POST /user/cadastro` → `POST /auth/login` → 5× `POST /produto/cadastroProduto`.

**Dados gerados:**

- e-mail `seed.u0@omce.test` … `seed.u49@omce.test`
- usuário `seed_u0` … `seed_u49`
- senha `Omce1234*` (todos)
- produtos nomeados `[SEED] ESP32 DevKit V1`, `[SEED] Sensor DHT11`, … com preço entre R$ 5 e
  R$ 500, `categoria`/`condicao` sorteados dos enums reais e uma imagem PNG 1×1.

**É idempotente.** Rodar de novo não duplica nada: usuário que já existe (HTTP 400 por causa das
constraints `UNIQUE` de `email` e `nome_user`) é reaproveitado, e só os produtos que faltam para
chegar em 5 são criados.

**Variáveis de ambiente** (`-e NOME=valor` no `docker run`):

| Variável | Padrão | Descrição |
|---|---|---|
| `BASE_URL` | `http://localhost:8080` | URL do backend |
| `SEED_USERS` | `50` | Quantidade de usuários |
| `PRODUTOS_POR_USER` | `5` | Produtos por usuário |
| `SEED_VUS` | `5` | Paralelismo (baixo de propósito: o gargalo é o BCrypt do cadastro) |
| `SEED_TAG` | `seed` | Prefixo dos registros — use outro valor para criar um segundo lote |
| `SEED_SENHA` | `Omce1234*` | Senha das contas criadas |

---

## `stress.js`

O `setup()` roda uma vez, coleta ~50 ids reais de produto e loga como `seed.u0@omce.test`. Se o
catálogo estiver vazio ou o login falhar, o teste **para com uma mensagem clara** pedindo para
rodar o `seed.js` antes.

Dois cenários em paralelo:

| Cenário | Perfil | O que exercita |
|---|---|---|
| `navegacao` | rampa `ramping-vus`: 0 → 25 → 50 → 100 → 200 → 0 (8 min) | catálogo paginado, filtro por categoria/preço, **detalhes do produto** (o endpoint mais pesado: carrega o `LONGBLOB` da imagem e serializa em Base64) e leitura de avaliações |
| `login` | `constant-arrival-rate` de 5 req/s | isola o custo do BCrypt sem contaminar as métricas de leitura |

O login fica num cenário separado justamente porque o BCrypt é caro por design (~50–100 ms de CPU
por chamada). Se cada iteração logasse, o teste mediria o BCrypt em vez da aplicação.

**Thresholds** (ajuste depois da primeira execução):

- `http_req_failed: rate<0.10` com `abortOnFail` — o teste se interrompe sozinho quando o sistema
  começa a derreter, em vez de martelar um backend já caído
- `filtro` p95 < 800 ms · `detalhes` p95 < 1500 ms · `login` p95 < 2000 ms

Reduzir a carga: `-e VU_MAX=50`.

O relatório final sai por endpoint e o JSON completo vai para `out/stress-summary.json`
(a pasta `out/` é ignorada pelo git).

O resumo traz duas linhas de tempo que são fáceis de confundir:

- **`Jornada (4 reqs)`** — soma das 4 requisições da iteração, **sem** o think time. É o número
  que mede a aplicação.
- **`Iteracao`** — inclui o `sleep()` de 0,5 s a 2,5 s que simula o usuário lendo a tela, então
  fica na casa dos segundos **por construção**. Só merece atenção se ficar muito acima de
  `jornada + 2,5 s` — aí o gargalo passa a ser a máquina que roda o k6 (ele é single-process e
  acima de ~200 VUs pode saturar antes do backend).

> **Não rode `docker compose up` enquanto o stress estiver rodando.** Isso recria o container do
> backend e derruba todas as conexões no meio do teste, o que aparece como uma enxurrada de
> `connection reset by peer` e faz o `abortOnFail` interromper tudo. Vale também para tarefas em
> outra pasta/worktree: o nome do projeto no Compose vem do nome do diretório (`projetos`), então
> um `docker compose up` de uma cópia do repositório mexe **nestes mesmos containers**.

---

## Conferir os dados no banco

```bash
docker compose exec database mysql -uusuario -psenha omce -e "SELECT COUNT(*) users FROM user WHERE email LIKE 'seed.%'; SELECT COUNT(*) produtos FROM produto WHERE nome LIKE '[SEED]%';"
```

Esperado: **50** e **250**.

Pelo catálogo público (`totalElements` deve ser 250):

```bash
curl -s "http://localhost:8080/produto/filtro?size=5" | head -c 400
```

---

## Apagar os dados de seed

Só se quiser recomeçar do zero. Na ordem das foreign keys:

```bash
docker compose exec -T database mysql -uusuario -psenha omce <<'SQL'
DELETE ap FROM avaliacao_produto ap JOIN produto p ON p.id = ap.produto_id
  JOIN user u ON u.id = p.id_usuario WHERE u.email LIKE 'seed.%@omce.test';
DELETE av FROM avaliacao_vendedor av JOIN user u ON u.id = av.vendedor_id
  WHERE u.email LIKE 'seed.%@omce.test';
DELETE ip FROM itens_pedido ip JOIN produto p ON p.id = ip.produto_id
  JOIN user u ON u.id = p.id_usuario WHERE u.email LIKE 'seed.%@omce.test';
DELETE pe FROM pedido pe JOIN user u ON u.id = pe.comprador_id
  WHERE u.email LIKE 'seed.%@omce.test';
DELETE p FROM produto p JOIN user u ON u.id = p.id_usuario
  WHERE u.email LIKE 'seed.%@omce.test';
DELETE FROM user WHERE email LIKE 'seed.%@omce.test';
SQL
```

Reset completo do ambiente (apaga o volume; o Flyway recria o schema no próximo `up`):

```bash
cd .. && docker compose down -v && docker compose up -d
```
