*** Settings ***
Resource     ${CURDIR}/../resources/login_resources.robot
Test Setup     Abrir o navegador
Test Teardown     Fechar o navegador
*** Test Cases ***
CT42 - Deve fazer o login do usuário quando colocar a senha e emails corretos
    [Documentation]     Esse teste realiza um cadastro e login corretamente no sistema O.M.C.E
    [Tags]     login_correto
    Acessar a pagina de cadastro do site
    Realizar cadastro no site
    Realizar login no site
CT46 - Cadastro de usuário com e-mail já cadastrado
    [Documentation]     Esse teste realiza um cadastro de usuário com um email já cadastrado no sistema O.M.C.E
    [Tags]     email_ja_cadastrado
    Acessar a pagina de cadastro do site
    Realizar cadastro no site email existente
CT44 - Fazer login com email errado 
    [Documentation]     Esse teste realiza login com email incorreto
    [Tags]     email_incorreto
    Realizar Login Com Email incorreto
CT45 - Deslogar do site pelo botão na navbar
    [Documentation]    Esse teste realiza logout 
    [Tags]    logout
    Realizar login no site
    Realizar logout
CT53 - Realizar Login com Senha incorreta
    [Documentation]     Esse teste realiza login com senha incorreto
    [Tags]     Senha_INCORRETO
    Realizar login com Senha incorreta
CT51 - Cadastro de um usuário com o campo de email em branco
    [Documentation]     Esse teste realiza um cadastro de usuário com o campo email em branco
    [Tags]     email_em_branco
    Acessar a pagina de cadastro do site
    Realizar cadastro com email em branco
CT48- Enviar email de recuperar senha
    [Documentation]     Esse teste realiza o envio de email para recuperar a senha
    [Tags]     recuperar_senha
    Acessar pagina login
    Acessar pagina de recuperar senha
    Enviar email para recuperar senha