*** Settings ***
Library     SeleniumLibrary

*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/front-end/html/login.html
${URL_CADASTRO_PRODUTO}  http://127.0.0.1:5500/front-end/html/cadastroProduto.html
${Nome_produto}    esp
${Preco_negativo}     -1
${Detalhes}    isto e um teste
${IMAGE_PATH}    ${EXECDIR}${/}testesrobot${/}imagem${/}test_Esp32.jpeg

*** Keywords ***

Realizar Cadastro De Produto COm VALor negativo
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