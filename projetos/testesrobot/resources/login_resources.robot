*** Settings ***
Library     SeleniumLibrary

*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/projetos/front-end/html/login.html
${URL_RECUPERAR_SENHA}    http://127.0.0.1:5500/projetos/front-end/html/redefinirSenha.html
${NOME}        OMCE
${CPF}         08749058932
${EMAIL}       omce4952@gmail.com
${CEP}         80215-901
${TELEFONE}    991849066
${NOME_USER}   omce4952
${SENHA}       Omce1234*
${EMAIL_INCORRETO}  omce492@gmail.com
${SENHA_INCORRETA}    Omce123*

*** Keywords ***
Abrir o navegador
    Open Browser     ${URL}     ${BROWSER}
    Maximize Browser Window

Acessar a pagina de cadastro do site
    Go To    ${URL}
    Wait Until Element Is Visible    xpath=//button[contains(text(),'Cadastrar-se')]    5s
    Click Button  xpath=//button[contains(text(),'Cadastrar-se')]

Realizar cadastro no site
    Wait Until Element Is Visible    id=txtName    5s
    Input Text    id=txtName    ${NOME}
    Input Text    id=txtCPF    ${CPF}
    Input Text    id=txtNasc    2000-05-12
    Select Radio Button    optGender    masculino
    Input Text    id=txtEmail    ${EMAIL}
    Input Text    id=end_cep    ${CEP}
    Input Text    id=txtTel    ${TELEFONE}
    Input Text    id=txtNU    ${NOME_USER}
    Input Text    id=txtSenha    ${SENHA}
    Input Text    id=txtSenhaConfirmar    ${SENHA}
    Sleep    1s
    Click Button    Enviar
    Wait Until Element Is Visible    id=email    5s
    
Realizar login no site
    Wait Until Element Is Visible    id=email    5s
    Input Text    id=email    ${EMAIL}
    Input Text    id=senha    ${SENHA}
    Wait Until Element Is Visible    xpath=//button[text()='Entrar']    5s
    Click Button    Entrar
    Wait Until Page Contains    Bem-vindo ao O.M.C.E    10s
    Sleep    3s

Fechar o navegador
    Capture Page Screenshot    ${EXECDIR}/printsSelenium/screenshot_{index}.png
    Close Browser

Realizar cadastro no site email existente
    Wait Until Element Is Visible    id=txtName    5s
    Input Text    id=txtName    ${NOME}
    Input Text    id=txtCPF    ${CPF}
    Input Text    id=txtNasc    2000-05-12
    Select Radio Button    optGender    masculino
    Input Text    id=txtEmail    ${EMAIL}
    Input Text    id=end_cep    ${CEP}
    Input Text    id=txtTel    ${TELEFONE}
    Input Text    id=txtNU    ${NOME_USER}
    Input Text    id=txtSenha    ${SENHA}
    Input Text    id=txtSenhaConfirmar    ${SENHA}
    Sleep    1s
    Click Button    Enviar
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Sleep    1s
    Element Should Contain    css=.swal2-title    Não foi possível realizar seu cadastro
Realizar Login Com Email incorreto
    Wait Until Element Is Visible    id=email    5s
    Input Text    id=email    ${EMAIL_INCORRETO}
    Input Text    id=senha    ${SENHA}
    Wait Until Element Is Visible    xpath=//button[text()='Entrar']    5s
    Click Button    Entrar
    Wait Until Element Is Visible    css=.swal2-popup    5s
    Element Should Contain    css=.swal2-title    Não foi possível realizar seu login
    Element Should Contain    css=#swal2-html-container    Verifique suas credenciais
    Click Button    css=.swal2-confirm
Realizar logout 
    Wait Until Element Is Visible    css=button.logout    5s
    Click Element    css=button.logout
    Wait Until Element Is Visible    css=.loginBox    5s
    Element Should Contain    css=.loginBox h1    Bem-vindo ao O.M.C.E

Realizar login com Senha incorreta
    Wait Until Element Is Visible    id=email    5s
    Input Text    id=email    ${EMAIL_INCORRETO}
    Input Text    id=senha    ${SENHA_INCORRETA}
    Wait Until Element Is Visible    xpath=//button[text()='Entrar']    5s
    Click Button    Entrar
    Wait Until Element Is Visible    css=.swal2-popup    5s
    Element Should Contain    css=.swal2-title    Não foi possível realizar seu login
    Element Should Contain    css=#swal2-html-container    Verifique suas credenciais
    Click Button    css=.swal2-confirm

Realizar cadastro com email em branco
    Wait Until Element Is Visible    id=txtName    5s
    Input Text    id=txtName    ${NOME}
    Input Text    id=txtCPF    ${CPF}
    Input Text    id=txtNasc    2000-05-12
    Select Radio Button    optGender    masculino
    Input Text    id=end_cep    ${CEP}
    Input Text    id=txtTel    ${TELEFONE}
    Input Text    id=txtNU    ${NOME_USER}
    Input Text    id=txtSenha    ${SENHA}
    Input Text    id=txtSenhaConfirmar    ${SENHA}
    Sleep    1s
    Click Button    Enviar
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Sleep    1s
    Element Should Contain    css=.swal2-title    Não foi possível realizar seu cadastro

Acessar pagina login
    Go To    ${URL}
    Wait Until Element Is Visible    id=email    5s

Acessar pagina de recuperar senha
    Wait Until Element Is Visible    xpath=//a[contains(text(),'Esqueceu a senha?')]    5s
    Click Link    xpath=//a[contains(text(),'Esqueceu a senha?')]

Enviar email para recuperar senha
    Go To    ${URL_RECUPERAR_SENHA}
    Wait Until Element Is Visible    id=email-reset    5s
    Input Text    id=email-reset    ${EMAIL}
    Click Button    xpath=//button[contains(text(),'Enviar Link de Redefinição')]



