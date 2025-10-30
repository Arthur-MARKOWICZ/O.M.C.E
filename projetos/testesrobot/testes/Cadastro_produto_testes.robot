*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Resource    ${CURDIR}/../resources/Cadastro_produto_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
<<<<<<< HEAD
CT49 - Realizar Cadastro de produto com valor negativo
    [Documentation]     Esse teste realiza cadastro de produto com valor negativo
    [Tags]     Cadastro_De_Produto
=======
CT49 - Cadastro de produto com preço negativo
    [Documentation]     Esse teste realiza um cadastro de produto com valor negativo
    [Tags]     produto_valor_negativo
>>>>>>> a7856a2352f6862e047bab7054c13e206821052a
    Realizar login no site
    Realizar Cadastro De Produto Com Valor negativo
CT58 - Cadastro de produto sem imagem
    [Documentation]     Esse teste realiza um cadastro de produto sem selecionar uma imagem
    [Tags]     produto_sem_imagem
    Realizar login no site
    Realizar Cadastro De Produto Sem Imagem