*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Cadastro_produto_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT49 - Realizar Cadastro de produto com valor negativo
    [Documentation]     Esse teste realiza cadastro de produto com valor negativo
    [Tags]     Cadastro_De_Produto
    Realizar login no site
    Realizar Cadastro De Produto COm VALor negativo