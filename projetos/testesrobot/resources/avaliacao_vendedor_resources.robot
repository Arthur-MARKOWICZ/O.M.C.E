*** Settings ***
Library     SeleniumLibrary

*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/projetos/front-end/html/login.html
${URL_AVALIACAO_PRODUTO}  http://127.0.0.1:5500/projetos/front-end/html/historicoCompra.html
${URL_AVALIACAO_VENDEDOR}    http://127.0.0.1:5500/projetos/front-end/html/AvaliarVendedor.html
${NOTA_AVALIACAO_VENDEDOR}   5
${COMENTARIO_AVALIACAO_VENDEDOR}     Teste

*** Keywords ***
Realizar Avaliação De Vendedor
    Go to     ${URL_AVALIACAO_PRODUTO}
    Wait Until Element Is Visible    xpath=//button[contains(text(),'Avaliar')]    5s
    Click Element    xpath=//button[contains(text(),'Avaliar Vendedor')]
    Location Should Be    ${URL_AVALIACAO_VENDEDOR}
    Input Text    id=nota-avaliacao    ${NOTA_AVALIACAO_VENDEDOR}
    Input Text    id=Comentario    ${COMENTARIO_AVALIACAO_VENDEDOR}
    Click Element    xpath=//button[contains(text(),'Enviar')]
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Avaliação cadastrado com sucesso
    Click Button    css=.swal2-confirm
Realizar Avaliação De Vendedor Sem Nota
    Go to     ${URL_AVALIACAO_PRODUTO}
    Wait Until Element Is Visible    xpath=//button[contains(text(),'Avaliar')]    5s
    Click Element    xpath=//button[contains(text(),'Avaliar Vendedor')]
    Location Should Be    ${URL_AVALIACAO_VENDEDOR}
    Input Text    id=Comentario    ${COMENTARIO_AVALIACAO_VENDEDOR}
    Click Element    xpath=//button[contains(text(),'Enviar')]
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Não foi possível realizar seu cadastro
    Click Button    css=.swal2-confirm
