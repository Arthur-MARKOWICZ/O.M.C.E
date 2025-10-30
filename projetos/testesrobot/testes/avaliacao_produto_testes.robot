*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/avaliacao_produto_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT43 - Avaliar um produto corretamente
    [Documentation]     Esse teste realiza uma avaliação de produto corretamente no sistema O.M.C.E
    [Tags]     avaliacao_correta
    Realizar login no site
    Realizar Avaliação De Produto Corretamente
CT52 - Cadastro de avaliação de produto sem nota
    [Documentation]     Esse teste realiza uma avaliação de produto sem selecionar uma nota
    [Tags]     avaliacao_sem_nota
    Realizar login no site
    Realizar Avaliação De Produto Sem Nota
CT54 - Deve fazer cadastro da avaliação de um produto sem comentario
    [Documentation]     Esse teste realiza uma avaliação de produto sem colocar um comentário
    [Tags]     avaliacao_sem_comentario
    Realizar login no site
    Realizar Avaliação De Produto Sem Comentário
