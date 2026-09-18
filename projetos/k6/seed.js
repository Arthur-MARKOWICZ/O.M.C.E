// ---------------------------------------------------------------------------
// seed.js - popula o banco do O.M.C.E com dados de teste PERMANENTES.
//
// Cria 50 usuarios (role MISTO) e 250 produtos (5 por usuario) via API REST.
// MISTO e' usado de proposito: concede ROLE_VENDEDOR (necessario para cadastrar
// produto) e ROLE_COMPRADOR no mesmo usuario.
//
// E' idempotente: rodar de novo nao duplica nada (usuario ja existente e'
// reaproveitado e so' os produtos faltantes sao criados).
//
//   docker run --rm -i --network host --user "$(id -u):$(id -g)" \
//     -v "$PWD:/k6" -w /k6 grafana/k6 run seed.js
// ---------------------------------------------------------------------------
import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

// --- configuracao (tudo sobrescrivel por variavel de ambiente) -------------
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const TAG = __ENV.SEED_TAG || 'seed';
const TOTAL_USERS = Number(__ENV.SEED_USERS || 50);
const PRODUTOS_POR_USER = Number(__ENV.PRODUTOS_POR_USER || 5);
const VUS = Number(__ENV.SEED_VUS || 5);
const SENHA = __ENV.SEED_SENHA || 'Omce1234*';

const ITER_POR_VU = Math.ceil(TOTAL_USERS / VUS);

export const options = {
  scenarios: {
    seed: {
      executor: 'per-vu-iterations',
      vus: VUS,
      iterations: ITER_POR_VU,
      maxDuration: '30m',
    },
  },
  // Um seed que falha tem que falhar barulhento, nao silenciosamente.
  thresholds: {
    checks: ['rate>0.99'],
  },
};

// --- metricas customizadas -------------------------------------------------
const usuariosCriados = new Counter('usuarios_criados');
const usuariosExistentes = new Counter('usuarios_ja_existiam');
const produtosCriados = new Counter('produtos_criados');
const falhas = new Counter('falhas');

// --- dados -----------------------------------------------------------------

// PNG 1x1 transparente. Base64 VALIDO e' obrigatorio: o backend faz
// Base64.getDecoder().decode(imagem) sem try/catch em Produto.java.
// Pequeno de proposito, para o seed nao virar benchmark de rede (LONGBLOB).
const IMG_B64 =
  'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk' +
  '+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==';

const COMPONENTES = [
  { nome: 'ESP32 DevKit V1', categoria: 'ESP32', detalhes: 'Placa de desenvolvimento ESP32 com Wi-Fi e Bluetooth integrados, 30 pinos.' },
  { nome: 'ESP32-CAM', categoria: 'ESP32', detalhes: 'Modulo ESP32 com camera OV2640 e slot para cartao microSD.' },
  { nome: 'Arduino Uno R3', categoria: 'ARDUINO', detalhes: 'Placa Arduino Uno R3 com microcontrolador ATmega328P e cabo USB.' },
  { nome: 'Arduino Nano', categoria: 'ARDUINO', detalhes: 'Versao compacta do Arduino, ideal para projetos embarcados em protoboard.' },
  { nome: 'Kit de Resistores 1/4W', categoria: 'REGISTORES', detalhes: 'Kit com 600 resistores de 1/4W, 30 valores diferentes de 10R a 1M.' },
  { nome: 'Resistor 10k Ohm', categoria: 'REGISTORES', detalhes: 'Pacote com 100 resistores de 10k Ohm, tolerancia de 5%.' },
  { nome: 'Sensor DHT11', categoria: 'SENSORES', detalhes: 'Sensor digital de temperatura e umidade, faixa de 0 a 50 graus Celsius.' },
  { nome: 'Sensor Ultrassonico HC-SR04', categoria: 'SENSORES', detalhes: 'Sensor de distancia por ultrassom, alcance de 2cm a 400cm.' },
  { nome: 'Sensor PIR HC-SR501', categoria: 'SENSORES', detalhes: 'Sensor de presenca infravermelho passivo com ajuste de sensibilidade.' },
  { nome: 'Bateria 18650 3000mAh', categoria: 'BATERIA', detalhes: 'Bateria recarregavel de litio 3.7V 3000mAh com protecao.' },
  { nome: 'Suporte para Bateria 9V', categoria: 'BATERIA', detalhes: 'Suporte com clip e cabo para bateria de 9V.' },
  { nome: 'Kit Jumpers Macho-Femea', categoria: 'CABOS', detalhes: 'Kit com 120 cabos jumper de 20cm nas combinacoes macho e femea.' },
  { nome: 'Cabo USB-C 1m', categoria: 'CABOS', detalhes: 'Cabo de dados e alimentacao USB-A para USB-C com 1 metro.' },
  { nome: 'Motor DC 6V com Roda', categoria: 'MOTORES', detalhes: 'Motor DC 6V com caixa de reducao e roda, ideal para robos seguidores de linha.' },
  { nome: 'Servo Motor SG90', categoria: 'MOTORES', detalhes: 'Micro servo motor de 9g com rotacao de 180 graus e hastes inclusas.' },
  { nome: 'Motor de Passo 28BYJ-48', categoria: 'MOTORES', detalhes: 'Motor de passo 5V acompanhado do driver ULN2003.' },
  { nome: 'Ponte H L298N', categoria: 'MOTORES', detalhes: 'Driver de motor ponte H dupla L298N, controla ate 2 motores DC.' },
  { nome: 'Conectores JST 2 Pinos', categoria: 'CONECTORES', detalhes: 'Kit com 20 pares de conectores JST-XH de 2 pinos com fios.' },
  { nome: 'Barra de Pinos 40 Vias', categoria: 'CONECTORES', detalhes: 'Barra de pinos macho 2.54mm com 40 vias, pode ser cortada.' },
  { nome: 'Protoboard 830 Pontos', categoria: 'OUTRO', detalhes: 'Protoboard de 830 pontos com trilhas de alimentacao nas laterais.' },
];

const CONDICOES = ['NOVO', 'USADO'];

const CIDADES = [
  { cidade: 'Curitiba', estado: 'PR', cep: '80215901' },
  { cidade: 'Sao Paulo', estado: 'SP', cep: '01310100' },
  { cidade: 'Rio de Janeiro', estado: 'RJ', cep: '20031170' },
  { cidade: 'Belo Horizonte', estado: 'MG', cep: '30130010' },
  { cidade: 'Porto Alegre', estado: 'RS', cep: '90020004' },
];

function endereco(n) {
  const c = CIDADES[n % CIDADES.length];
  return {
    cep: c.cep,
    pais: 'Brasil',
    estado: c.estado,
    cidade: c.cidade,
    logradouro: 'Rua dos Testes, ' + (100 + n),
  };
}

function novoUsuario(n) {
  return {
    nome: 'Usuario Seed ' + n,
    cpf: String(10000000000 + n),
    dataNasc: '1995-05-20',
    sexo: n % 2 === 0 ? 'M' : 'F',
    endereco: endereco(n),
    email: TAG + '.u' + n + '@omce.test',
    telefone: '419' + String(90000000 + n),
    nomeUser: TAG + '_u' + n,
    senha: SENHA,
    role: 'MISTO', // MISTO = ROLE_VENDEDOR + ROLE_COMPRADOR
  };
}

function novoProduto(idUsuario, n, i) {
  const c = COMPONENTES[(n * PRODUTOS_POR_USER + i) % COMPONENTES.length];
  // Preco pseudo-aleatorio mas deterministico, entre R$ 5,00 e R$ 500,00.
  const preco = Math.round((5 + ((n * 37 + i * 91) % 4951) / 10) * 100) / 100;
  return {
    nome: '[' + TAG.toUpperCase() + '] ' + c.nome,
    preco: preco,
    detalhes: c.detalhes,
    id_usuario: idUsuario,
    imagem: IMG_B64,
    imagem_tipo: 'image/png',
    categoria: c.categoria,
    condicao: CONDICOES[(n + i) % CONDICOES.length],
  };
}

// --- chamadas HTTP ---------------------------------------------------------
const JSON_HEADERS = { 'Content-Type': 'application/json' };

function cadastrarUsuario(usuario) {
  return http.post(BASE_URL + '/user/cadastro', JSON.stringify(usuario), {
    headers: JSON_HEADERS,
    tags: { endpoint: 'cadastro_user' },
  });
}

function login(email, senha) {
  return http.post(
    BASE_URL + '/auth/login',
    JSON.stringify({ email: email, senha: senha }),
    { headers: JSON_HEADERS, tags: { endpoint: 'login' } }
  );
}

function contarProdutosDoUsuario(token, idUsuario) {
  const res = http.get(BASE_URL + '/produto/todosProdutosUsuario?size=1', {
    headers: { Authorization: 'Bearer ' + token, 'Id-Usuario': String(idUsuario) },
    tags: { endpoint: 'produtos_do_usuario' },
  });
  if (res.status !== 200) return 0;
  try {
    return res.json('totalElements') || 0;
  } catch (e) {
    return 0;
  }
}

function cadastrarProduto(token, produto) {
  return http.post(BASE_URL + '/produto/cadastroProduto', JSON.stringify(produto), {
    headers: { 'Content-Type': 'application/json', Authorization: 'Bearer ' + token },
    tags: { endpoint: 'cadastro_produto' },
  });
}

// --- execucao --------------------------------------------------------------
export default function () {
  // Indice global do usuario: VU e ITER sao 1-based e 0-based respectivamente.
  const n = (__VU - 1) * ITER_POR_VU + __ITER;
  if (n >= TOTAL_USERS) return; // sobra quando TOTAL_USERS nao divide por VUS

  const usuario = novoUsuario(n);

  // 1. cadastro. 400 normalmente significa "email/nomeUser ja existe" (UNIQUE),
  //    entao seguimos em frente e reaproveitamos a conta - e' o que torna o
  //    seed re-executavel sem duplicar dados.
  const resCadastro = cadastrarUsuario(usuario);
  const jaExistia = resCadastro.status === 400;
  const cadastroOk = check(resCadastro, {
    'cadastro de usuario aceito (200) ou ja existente (400)': (r) =>
      r.status === 200 || r.status === 400,
  });
  if (!cadastroOk) {
    falhas.add(1);
    console.error(
      'Falha ao cadastrar ' + usuario.email + ': HTTP ' + resCadastro.status + ' - ' + resCadastro.body
    );
    return;
  }
  if (jaExistia) usuariosExistentes.add(1);
  else usuariosCriados.add(1);

  // 2. login para obter token + id
  const resLogin = login(usuario.email, usuario.senha);
  const loginOk = check(resLogin, {
    'login retornou 200': (r) => r.status === 200,
    'login retornou token': (r) => !!(r.json && r.json('token')),
  });
  if (!loginOk) {
    falhas.add(1);
    console.error(
      'Falha no login de ' + usuario.email + ': HTTP ' + resLogin.status + ' - ' + resLogin.body
    );
    return;
  }
  const token = resLogin.json('token');
  const idUsuario = resLogin.json('id');

  // 3. cria apenas os produtos que faltam para chegar em PRODUTOS_POR_USER
  const jaTem = contarProdutosDoUsuario(token, idUsuario);
  for (let i = jaTem; i < PRODUTOS_POR_USER; i++) {
    const res = cadastrarProduto(token, novoProduto(idUsuario, n, i));
    const ok = check(res, { 'cadastro de produto retornou 200': (r) => r.status === 200 });
    if (ok) {
      produtosCriados.add(1);
    } else {
      falhas.add(1);
      console.error(
        'Falha ao cadastrar produto de ' + usuario.email + ': HTTP ' + res.status + ' - ' + res.body
      );
    }
  }
}

// --- relatorio -------------------------------------------------------------
function contador(data, nome) {
  const m = data.metrics[nome];
  return m && m.values ? m.values.count || 0 : 0;
}

export function handleSummary(data) {
  const criados = contador(data, 'usuarios_criados');
  const existentes = contador(data, 'usuarios_ja_existiam');
  const produtos = contador(data, 'produtos_criados');
  const erros = contador(data, 'falhas');

  const linhas = [
    '',
    '==========================================================',
    '  SEED O.M.C.E - ' + BASE_URL,
    '==========================================================',
    '  Usuarios criados agora .......... ' + criados,
    '  Usuarios que ja existiam ........ ' + existentes,
    '  Produtos criados agora .......... ' + produtos,
    '  Falhas .......................... ' + erros,
    '----------------------------------------------------------',
    '  Alvo: ' + TOTAL_USERS + ' usuarios e ' +
      TOTAL_USERS * PRODUTOS_POR_USER + ' produtos',
    '  Login de teste: ' + TAG + '.u0@omce.test / ' + SENHA,
    '==========================================================',
    '',
  ];
  if (erros > 0) {
    linhas.push('  ATENCAO: houve falhas. Veja os console.error acima.');
    linhas.push('');
  }

  return {
    stdout: linhas.join('\n'),
    'out/seed-summary.json': JSON.stringify(data, null, 2),
  };
}
