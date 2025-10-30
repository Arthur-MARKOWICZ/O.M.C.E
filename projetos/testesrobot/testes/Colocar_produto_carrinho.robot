*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Colocar_produto_carrinho_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT59 - Colocar produto no carrinho
    [Documentation]    Esse teste adiciona um produto no carrinho
    [Tags]    adicionar_produto_carrinho
    Realizar login no site
    Entrar no feed
    Adicionar produto ao carrinho
    Verificar se o produto foi adicionado ao carrinho
CT60 - Remover produto do carrinho
    [Documentation]    Esse teste remove um produto do carrinho
    [Tags]    remover_produto_carrinho
    Realizar login no site
    Entrar no feed
    Adicionar produto ao carrinho
    Verificar se o produto foi adicionado ao carrinho
    Remover produto do carrinho
CT61 - Finalizar pedido
    [Documentation]    Esse teste finaliza o pedido com um produto no carrinho
    [Tags]    finalizar_pedido
    Realizar login no site
    Entrar no feed
    Adicionar produto ao carrinho
    Verificar se o produto foi adicionado ao carrinho
    Finalizar pedido