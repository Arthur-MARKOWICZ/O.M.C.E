*** Settings ***
Library     SeleniumLibrary
*** Variables ***
${BROWSER}     Chrome
${URL}         http://127.0.0.1:5500/projetos/front-end/html/login.html
${URL_feed}  http://127.0.0.1:5500/projetos/front-end/html/feed.html
${URL_carrinho}     http://127.0.0.1:5500/projetos/front-end/html/carrinho.html
${URL_pedido}    http://127.0.0.1:5500/projetos/front-end/html/pedido.html
${CEP}         80215-901

*** Keywords ***
Entrar no feed
    Go To    ${URL_feed}
    Wait Until Page Contains Element    xpath=//button[contains(text(),'Adicionar ao Carrinho')]

Adicionar produto ao carrinho
    Click Button    xpath=(//button[contains(text(),'Adicionar ao Carrinho')])[1]
    Sleep    2s

Verificar se o produto foi adicionado ao carrinho
    Go To    ${URL_carrinho}
    Wait Until Element Is Visible    xpath=//div[contains(@class,'produto-card')]    5s
    Sleep    2s
Remover produto do carrinho
    Click Button    xpath=(//button[contains(text(),'Remover do Carrinho')])[1]
    Sleep    2s

Verificar se o produto foi removido do carrinho
    Go To    ${URL_carrinho}
       Wait Until Page Contains    Total: R$ 0.00    timeout=10s

Finalizar pedido
    Click Button    xpath=//button[contains(text(),'Finalizar Compra')]
    Sleep    2s
    Go To    ${URL_pedido}
    Input Text    id=end_cep    ${CEP} 
    Sleep    5s
    Click Button    xpath=(//button[contains(text(),'Finalizar Pedido')])[1]
    Sleep    2s
