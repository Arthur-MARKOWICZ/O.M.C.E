# Regras de alteração

## Fluxo obrigatório

1. Leia o guia de contexto e inspecione os arquivos diretamente relacionados ao pedido.
2. Identifique contratos afetados: API, DTOs, banco, interface, testes e configuração.
3. Faça a menor alteração que resolva o pedido. Não misture refatorações, reformatações amplas ou atualizações de dependências não solicitadas.
4. Valide somente o que é proporcional à mudança, conforme o guia de validação.
5. Informe arquivos alterados, validações executadas e qualquer limitação real.

## Regras gerais

- Não altere arquivos gerados, diretórios de build, lockfiles ou dependências sem necessidade direta da tarefa.
- Não remova, renomeie ou reestruture arquivos fora do escopo sem autorização explícita.
- Preserve o idioma e o estilo predominantes no arquivo que está sendo modificado.
- Antes de uma mudança potencialmente destrutiva, confirme o alvo, o impacto e a autorização do usuário.
- Não modifique `front-end/` nem `testesrobot/` sem pedido explícito.

## Back-end

- Preserve a separação `controller` → `service` → `repository`; controladores não devem concentrar regra de negócio ou acesso direto ao banco.
- Reutilize DTOs, entidades, validações, exceções e enums existentes quando representarem o mesmo conceito.
- Ao adicionar ou modificar persistência, crie uma migração Flyway incremental em `db/migration/`; nunca altere migrações já aplicadas.
- Mantenha o formato de autenticação, autorização, respostas HTTP e erros compatível com os consumidores existentes, exceto quando a alteração de contrato for solicitada.
- Acrescente ou atualize testes Java quando a mudança afetar comportamento observável, regra de negócio, repositório ou endpoint.

## Front-end React

- Use `front-end-react/src/api.js` para chamadas HTTP, autenticação e tratamento comum de erros. Não crie clientes HTTP paralelos nem faça `fetch` direto para a API em componentes.
- Use `VITE_API_URL` para configuração do endereço da API.
- Preserve a estrutura de rotas, proteção de sessão e componentes de feedback existentes em `src/main.jsx`.
- Para mudanças que dependem de um endpoint, payload ou campo novo, confirme o contrato da API antes de implementar o consumidor.

## Banco e integração

- Trate alterações de esquema como alterações de contrato: avalie entidades JPA, DTOs, serviços, testes e a interface afetados.
- Dê à migração Flyway um número sequencial inédito e uma descrição objetiva.
- Não execute comandos que apaguem ou recriem dados de desenvolvimento sem autorização explícita. Use o banco de testes para validações automatizadas.
