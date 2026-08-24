# Contexto do projeto

## Arquitetura suportada

O O.M.C.E é composto pelos módulos abaixo:

| Módulo | Tecnologia | Responsabilidade |
| --- | --- | --- |
| `back-end/` | Java 23, Spring Boot, Maven | API REST, autenticação, regras de negócio, persistência e migrações. |
| `front-end-react/` | React, Vite, pnpm | Interface web suportada. |
| MySQL | MySQL 8 | Persistência da aplicação e dos testes de integração. |
| Docker | Docker Compose e Dockerfiles | Ambiente de banco, empacotamento e execução de serviços. |

O arquivo `docker-compose.yml` na raiz compõe back-end e interface React. O banco de desenvolvimento é definido em `docker/docker-compose.yaml`. Para testes de integração, `back-end/compose.yaml` fornece o banco de testes esperado pelo perfil `test`.

## Back-end

- Código de aplicação: `back-end/src/main/java/OMCE/OMCE/`.
- Endpoints: `controller/`; regras de negócio: os diretórios `service/`; acesso a dados: `repository/`.
- Dados de entrada e saída devem permanecer nos respectivos diretórios `dto/` do domínio.
- Entidades, validações, exceções e enums ficam nos diretórios de seus domínios. Reutilize as estruturas existentes antes de criar uma nova camada ou pacote.
- Migrações do banco ficam em `back-end/src/main/resources/db/migration/` e usam Flyway.
- Configuração principal e perfil de testes ficam, respectivamente, em `src/main/resources/application.properties` e `src/test/resources/application-test.yml`.

Ao mudar um endpoint, investigue controlador, serviço, DTOs, validações, testes e consumidores antes de alterar seu contrato. Preserve autenticação, códigos HTTP e formatos já usados, a menos que a tarefa peça uma quebra de compatibilidade.

## Front-end React

- Ponto de entrada e rotas: `front-end-react/src/main.jsx`.
- Estilos globais: `front-end-react/src/styles.css`.
- Toda comunicação HTTP com a API deve passar por `front-end-react/src/api.js` e pela função `request` já existente.
- A URL da API é configurável por `VITE_API_URL`; não codifique URLs alternativas diretamente em componentes.

Mantenha componentes, rotas, tratamento de autenticação e mensagens de erro coerentes com os padrões de `main.jsx`. Para qualquer alteração da API consumida pela interface, adapte o cliente React no mesmo trabalho ou registre claramente por que a compatibilidade foi preservada.

## Legado fora do escopo

`front-end/` e `testesrobot/` representam a interface estática e sua suíte Robot legadas. Elas não são a base para novas funcionalidades. Só as consulte ou modifique quando o pedido mencionar explicitamente a interface estática ou os testes Robot.
