*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Cadastro_produto_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT49 - Realizar Cadastro de produto com valor negativo
    Realizar login no site
    Realizar Cadastro De Produto COm VALor negativo