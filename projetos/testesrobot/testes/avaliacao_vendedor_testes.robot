*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/avaliacao_vendedor_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT50 - Cadastro de avaliação de vendedor
    [Documentation]     Esse teste realiza uma avaliação de vendedor corretamente 
    [Tags]     avaliacao_vendedor_correta
    Realizar login no site
    Realizar Avaliação De Vendedor 
CT56 - Deve fazer o cadastro de uma avaliação de um vendedor sem nota
    [Documentation]     Esse teste realiza uma avaliação de vendedor sem nota
    [Tags]     avaliacao_vendedor_sem_nota
    Realizar login no site
    Realizar Avaliação De Vendedor Sem Nota