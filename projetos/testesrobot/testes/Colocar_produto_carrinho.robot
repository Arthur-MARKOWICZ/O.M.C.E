*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Colocar_produto_carrinho_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT60 - Adicionar produto ao carrinho com sucesso
    [Documentation]    
    [Tags]    adicionar_produto_carrinho
    Realizar login no site
    Entrar no feed
    Adicionar produto ao carrinho
    Verificar se o produto foi adicionado ao carrinho