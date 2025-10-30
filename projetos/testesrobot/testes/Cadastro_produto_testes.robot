*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Cadastro_produto_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT49 - Cadastro de produto com preço negativo
    [Documentation]     Esse teste realiza um cadastro de produto com valor negativo
    [Tags]     produto_valor_negativo
    Realizar login no site
    Realizar Cadastro De Produto Com Valor negativo
CT58 - Cadastro de produto sem imagem
    [Documentation]     Esse teste realiza um cadastro de produto sem selecionar uma imagem
    [Tags]     produto_sem_imagem
    Realizar login no site
    Realizar Cadastro De Produto Sem Imagem
CT47 - Listagem de 10 produtos por página
    [Documentation]     Esse teste verifica se a listagem de produtos exibe 10 produtos por página no feed
    [Tags]     listagem_10_produtos
    Realizar login no site
    Realizar Cadastro de Produto Corretamente
    # Ainda não acabei esse teste