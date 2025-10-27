*** Settings ***
Library     SeleniumLibrary

*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/projetos/front-end/html/login.html
${URL_AVALIACAO_PRODUTO}  http://127.0.0.1:5500/projetos/front-end/html/historicoCompra.html
${NOTA_AVALIACAO_PRODUTO}   5
${COMENTARIO_AVALIACAO_PRODUTO}     Teste

*** Keywords ***
Realizar Avaliação De Produto Corretamente
    Go to     ${URL_AVALIACAO_PRODUTO}
    Wait Until Element Is Visible    xpath=//button[contains(text(),'Avaliar')]    5s
    Click Element    xpath=//button[contains(text(),'Avaliar')]
    Location Should Be    ${URL_AVALIACAO_PRODUTO}
    Input Text    id=nota-avaliacao    ${NOTA_AVALIACAO_PRODUTO}
    Input Text    id=comentario-avaliacao    ${COMENTARIO_AVALIACAO_PRODUTO}
    Click Element    xpath=//button[contains(text(),'Enviar Avaliação')]
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Avaliação enviada com sucesso!
    Click Button    css=.swal2-confirm

Realizar Avaliação De Produto Sem Nota
    Wait Until Element Is Visible    xpath=//a[contains(text(),'Avaliar')]    5s
    Click Element    xpath=//a[contains(text(),'Avaliar')]
    Location Should Be    ${URL_AVALIACAO_PRODUTO}
    Input Text    id=comentario-avaliacao    ${COMENTARIO_AVALIACAO_PRODUTO}
    Click Element    xpath=//input[@type='submit' and @value='Enviar Avaliação']
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Erro!
    Click Button    css=.swal2-confirm

Realizar Avaliação De Produto Sem Comentário
    Wait Until Element Is Visible    xpath=//a[contains(text(),'Avaliar')]    5s
    Click Element    xpath=//a[contains(text(),'Avaliar')]
    Location Should Be    ${URL_AVALIACAO_PRODUTO}
    Input Text    id=nota-avaliacao    ${NOTA_AVALIACAO_PRODUTO}
    Click Element    xpath=//input[@type='submit' and @value='Enviar Avaliação']
    Wait Until Element Is Visible    css=.swal2-popup    10s
    Element Should Contain    css=#swal2-title    Erro
    Click Button    css=.swal2-confirm
