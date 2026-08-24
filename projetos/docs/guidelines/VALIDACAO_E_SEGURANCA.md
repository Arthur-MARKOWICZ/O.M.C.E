# Validação e segurança

## Validação por módulo

Execute as verificações relevantes à alteração, informando quando alguma não puder ser executada.

| Alteração | Verificação mínima |
| --- | --- |
| Back-end Java | Em `back-end/`, execute `./mvnw test`. |
| Interface React | Em `front-end-react/`, execute `pnpm build`. |
| Imagens de produção | Quando Dockerfiles ou composição forem alterados, execute o build Docker correspondente. |
| API, persistência ou migrações | Execute os testes do back-end contra o banco do perfil `test`, além das verificações específicas da mudança. |

Os testes de integração usam o perfil Spring `test` e dependem de um MySQL disponível localmente na porta de testes configurada. Antes de rodá-los, suba o banco de testes a partir de `back-end/` com `docker compose up -d mysql_db`. Não execute a suíte contra o banco de desenvolvimento.

Os testes Robot não fazem parte do fluxo suportado: eles cobrem a interface estática legada e só devem ser usados quando o pedido os incluir explicitamente.

## Segurança e dados sensíveis

- Nunca adicione, copie, registre ou repita segredos em código, documentação, mensagens, logs, testes ou commits.
- Use variáveis de ambiente ou mecanismos de configuração apropriados para novos dados sensíveis. Para a interface React, prefira variáveis `VITE_*` somente para valores que podem ser públicos no navegador.
- Trate credenciais já versionadas como uma exposição existente: não as reproduza e não as altere nesta tarefa, a menos que o usuário peça uma correção de segurança. Se a tarefa exigir isso, proponha também a revogação ou rotação fora do repositório.
- Não exponha dados pessoais de usuários, tokens de autenticação, conteúdo de bancos ou resultados de testes em relatórios.
- Verifique cuidadosamente mudanças em autenticação, autorização, CORS, upload de arquivos, validação de entrada e migrações antes de entregá-las.

## Relatório final do agente

Ao concluir uma tarefa, informe de forma objetiva:

1. O que foi alterado e os arquivos principais envolvidos.
2. As validações executadas e seus resultados.
3. As validações não executadas, a razão e eventuais limitações ou riscos restantes.

Não declare sucesso de uma verificação que não tenha sido executada.
