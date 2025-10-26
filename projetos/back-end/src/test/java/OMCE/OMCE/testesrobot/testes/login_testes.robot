*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT42 - Realizar cadastro e login corretamente
    [Documentation]     Esse teste realiza um cadastro e login corretamente no sistema O.M.C.E
    [Tags]     login_correto
    Acessar a pagina de cadastro do site
    Realizar cadastro no site
    Realizar login no site

CT46 - Realizar cadastro com email já cadastrado
    [Documentation]     Esse teste realiza um cadastro de usuário com um email já cadastrado no sistema O.M.C.E
    [Tags]     email_ja_cadastrado
    Acessar a pagina de cadastro do site
    Realizar cadastro no site email existente