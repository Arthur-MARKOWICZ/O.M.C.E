*** Settings ***
Library     SeleniumLibrary

*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/projetos/front-end/html/login.html
${URL_CADASTRO_PRODUTO}  http://127.0.0.1:5500/projetos/front-end/html/cadastroProduto.html
${Nome_produto}    esp
${Preco_negativo}     -1
${Preco}     100
${Detalhes}    isto e um teste
${IMAGE_PATH}    ${EXECDIR}${/}/projetos/testesrobot${/}imagem${/}test_Esp32.jpeg

*** Keywords ***
Realizar Cadastro De Produto Com Valor negativo
    Wait Until Element Is Visible    xpath=//a[contains(text(),'Anunciar Produto')]    5s
    Click Element    xpath=//a[contains(text(),'Anunciar Produto')]
    Wait Until Location Contains    cadastroProduto.html    5s
    Location Should Be    ${URL_CADASTRO_PRODUTO}
    Input Text    id=txtName    ${Nome_produto}
    Input Text    id=txtPreco    ${Preco_negativo}
    Input Text    id=txtDetalhes    ${Detalhes}
    Select Radio Button    Condicao    NOVO
    Select Radio Button    categoria    ESP32
    Choose File    id=productImage    ${IMAGE_PATH}
    Sleep    2s    
    Click Element    xpath=//input[@type='submit' and @value='Finalizar cadastro de produto']
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Erro no cadastro.
    Click Button    css=.swal2-confirm

Realizar Cadastro De Produto sem categoria
  Wait Until Element Is Visible    xpath=//a[contains(text(),'Anunciar Produto')]    5s
    Click Element    xpath=//a[contains(text(),'Anunciar Produto')]
    Wait Until Location Contains    cadastroProduto.html    5s
    Location Should Be    ${URL_CADASTRO_PRODUTO}
    Input Text    id=txtName    ${Nome_produto}
    Input Text    id=txtPreco    ${Preco}
    Input Text    id=txtDetalhes    ${Detalhes}
    Select Radio Button    Condicao    NOVO
    Choose File    id=productImage    ${IMAGE_PATH}
    Sleep    1s
    Click Element    xpath=//input[@type='submit' and @value='Finalizar cadastro de produto']
    Sleep    1s
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=.swal2-popup   selecione uma condicao e categoria
Realizar Cadastro De Produto Sem Imagem
    Wait Until Element Is Visible    xpath=//a[contains(text(),'Anunciar Produto')]    5s
    Click Element    xpath=//a[contains(text(),'Anunciar Produto')]
    Wait Until Location Contains    cadastroProduto.html    5s
    Location Should Be    ${URL_CADASTRO_PRODUTO}
    Input Text    id=txtName    ${Nome_produto}
    Input Text    id=txtPreco    ${Preco}
    Input Text    id=txtDetalhes    ${Detalhes}
    Select Radio Button    Condicao    NOVO
    Select Radio Button    categoria    ESP32
    Sleep    1s
    Click Element    xpath=//input[@type='submit' and @value='Finalizar cadastro de produto']
    Sleep    1s
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=.swal2-popup    Preenchimento obrigatório: Imagem