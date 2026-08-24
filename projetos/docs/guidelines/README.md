# Guidelines para agentes de IA

Estas guidelines orientam agentes de IA que analisam, modificam ou validam este repositório. Elas complementam o pedido recebido: o pedido do usuário sempre define o objetivo e o escopo da tarefa.

## Ordem de leitura

Antes de alterar arquivos, leia nesta ordem:

1. [Contexto do projeto](./CONTEXTO_DO_PROJETO.md) para localizar o módulo e os contratos envolvidos.
2. [Regras de alteração](./REGRAS_DE_ALTERACAO.md) para planejar e executar uma mudança limitada ao escopo.
3. [Validação e segurança](./VALIDACAO_E_SEGURANCA.md) antes de executar verificações ou entregar o resultado.

## Escopo destas regras

- A interface suportada é `front-end-react/`.
- `front-end/` é uma interface estática legada. Não a altere, nem execute ou atualize os testes Robot em `testesrobot/`, salvo solicitação explícita.
- As regras são independentes de fornecedor: aplicam-se a qualquer assistente ou agente de IA.
- Não use estas guidelines para expandir o pedido original. Mantenha mudanças, dependências e arquivos modificados no menor escopo necessário.

Se uma regra entrar em conflito com uma solicitação explícita do usuário, explique o impacto e siga a solicitação somente dentro do escopo autorizado.
