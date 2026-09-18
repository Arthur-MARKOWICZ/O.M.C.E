// ---------------------------------------------------------------------------
// stress.js - teste de stress do O.M.C.E.
//
// SOMENTE LEITURA: nenhum POST/PUT/DELETE que crie registro. O unico POST e'
// /auth/login, que nao grava nada. Pode rodar quantas vezes quiser - o banco
// fica exatamente como estava, entao nao existe script de limpeza.
//
// Depende dos dados do seed. Rode seed.js antes.
//
//   docker run --rm -i --network host --user "$(id -u):$(id -g)" \
//     -v "$PWD:/k6" -w /k6 grafana/k6 run stress.js
// ---------------------------------------------------------------------------
import http from 'k6/http';
import { check, sleep, fail } from 'k6';
import { Trend } from 'k6/metrics';

// --- configuracao ----------------------------------------------------------
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const TAG = __ENV.SEED_TAG || 'seed';
const SENHA = __ENV.SEED_SENHA || 'Omce1234*';
const VU_MAX = Number(__ENV.VU_MAX || 200);

const CATEGORIAS = [
  'ESP32', 'ARDUINO', 'REGISTORES', 'SENSORES', 'BATERIA',
  'CABOS', 'MOTORES', 'CONECTORES', 'OUTRO',
];

export const options = {
  scenarios: {
    // Rampa em degraus ate o ponto de quebra.
    navegacao: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: Math.round(VU_MAX * 0.125) }, //  25
        { duration: '2m', target: Math.round(VU_MAX * 0.25) },  //  50
        { duration: '2m', target: Math.round(VU_MAX * 0.5) },   // 100
        { duration: '2m', target: VU_MAX },                     // 200
        { duration: '1m', target: 0 },
      ],
      gracefulRampDown: '30s',
      tags: { cenario: 'navegacao' },
    },
    // Taxa baixa e constante so' para medir o custo do BCrypt do login,
    // sem contaminar as metricas de leitura.
    login: {
      executor: 'constant-arrival-rate',
      rate: 5,
      timeUnit: '1s',
      duration: '8m',
      preAllocatedVUs: 10,
      maxVUs: 50,
      exec: 'cenarioLogin',
      tags: { cenario: 'login' },
    },
  },
  thresholds: {
    // Aborta sozinho quando o sistema comeca a derreter, em vez de continuar
    // martelando um backend ja' caido.
    http_req_failed: [{ threshold: 'rate<0.10', abortOnFail: true, delayAbortEval: '30s' }],
    'http_req_duration{endpoint:filtro}': ['p(95)<800'],
    // Mais folgado: carrega o LONGBLOB da imagem e serializa em Base64.
    'http_req_duration{endpoint:detalhes}': ['p(95)<1500'],
    'http_req_duration{endpoint:avaliacoes}': ['p(95)<800'],
    // BCrypt e' caro por design.
    'http_req_duration{endpoint:login}': ['p(95)<2000'],
    checks: ['rate>0.95'],

    // Thresholds que nunca falham, presentes so' para o k6 materializar as
    // submetricas por tag: sem isso o resumo nao consegue ler a contagem de
    // requisicoes nem a taxa de erro de cada endpoint separadamente.
    'http_reqs{endpoint:filtro}': ['count>=0'],
    'http_reqs{endpoint:detalhes}': ['count>=0'],
    'http_reqs{endpoint:avaliacoes}': ['count>=0'],
    'http_reqs{endpoint:login}': ['count>=0'],
    'http_req_failed{endpoint:filtro}': ['rate>=0'],
    'http_req_failed{endpoint:detalhes}': ['rate>=0'],
    'http_req_failed{endpoint:avaliacoes}': ['rate>=0'],
    'http_req_failed{endpoint:login}': ['rate>=0'],
  },
};

const duracaoJornada = new Trend('duracao_jornada', true);

// --- setup: roda uma vez, resultado e' entregue a todos os VUs -------------
export function setup() {
  const resCatalogo = http.get(BASE_URL + '/produto/filtro?size=50');
  if (resCatalogo.status !== 200) {
    fail(
      'Nao consegui ler o catalogo em ' + BASE_URL + '/produto/filtro (HTTP ' +
      resCatalogo.status + '). O backend esta no ar? Tente: docker compose up -d'
    );
  }

  const corpo = resCatalogo.json();
  const ids = (corpo.content || []).map(function (p) { return p.id; });
  if (ids.length === 0) {
    fail(
      'O catalogo esta vazio - nao da para testar leitura sem dados. ' +
      'Rode o seed primeiro: k6 run seed.js'
    );
  }

  const email = TAG + '.u0@omce.test';
  const resLogin = http.post(
    BASE_URL + '/auth/login',
    JSON.stringify({ email: email, senha: SENHA }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  if (resLogin.status !== 200) {
    fail(
      'Login de ' + email + ' falhou (HTTP ' + resLogin.status + '). ' +
      'Rode o seed primeiro: k6 run seed.js'
    );
  }

  console.log(
    'setup: ' + ids.length + ' produtos no catalogo (total ' +
    corpo.totalElements + '), login OK como ' + email
  );

  return {
    ids: ids,
    token: resLogin.json('token'),
    idUsuario: resLogin.json('id'),
  };
}

function aleatorio(lista) {
  return lista[Math.floor(Math.random() * lista.length)];
}

// --- cenario principal: navegacao no catalogo (publico, so' leitura) -------
export default function (dados) {
  const inicio = Date.now();

  // 1. abre o catalogo numa pagina qualquer das 5 primeiras
  const pagina = Math.floor(Math.random() * 5);
  const resLista = http.get(
    BASE_URL + '/produto/filtro?page=' + pagina + '&size=10',
    { tags: { endpoint: 'filtro' } }
  );
  check(resLista, { 'catalogo 200': (r) => r.status === 200 });

  // 2. filtra por categoria e faixa de preco
  const resFiltro = http.get(
    BASE_URL + '/produto/filtro?categoria=' + aleatorio(CATEGORIAS) +
      '&precoMin=10&precoMax=300&size=10',
    { tags: { endpoint: 'filtro' } }
  );
  check(resFiltro, { 'filtro por categoria 200': (r) => r.status === 200 });

  // 3. abre os detalhes de um produto - endpoint mais pesado, devolve a
  //    imagem (LONGBLOB) codificada em Base64 dentro do JSON
  const resDetalhes = http.get(
    BASE_URL + '/produto/visualizarDetalhesProduto/' + aleatorio(dados.ids),
    { tags: { endpoint: 'detalhes' } }
  );
  check(resDetalhes, {
    'detalhes 200': (r) => r.status === 200,
    'detalhes tem nome': (r) => {
      try { return !!r.json('nome'); } catch (e) { return false; }
    },
  });

  // 4. le as avaliacoes do produto (publico)
  const idProduto = aleatorio(dados.ids);
  const resAvaliacoes = http.get(
    BASE_URL + '/avaliacoes/produto/' + idProduto + '/pagina?page=0&size=10',
    { tags: { endpoint: 'avaliacoes' } }
  );
  check(resAvaliacoes, { 'avaliacoes 200': (r) => r.status === 200 });

  duracaoJornada.add(Date.now() - inicio);
  sleep(Math.random() * 2 + 0.5); // think time de 0,5s a 2,5s
}

// --- cenario isolado: custo do login (BCrypt) ------------------------------
export function cenarioLogin(dados) {
  const res = http.post(
    BASE_URL + '/auth/login',
    JSON.stringify({ email: TAG + '.u0@omce.test', senha: SENHA }),
    { headers: { 'Content-Type': 'application/json' }, tags: { endpoint: 'login' } }
  );
  check(res, { 'login 200': (r) => r.status === 200 });
}

// --- relatorio -------------------------------------------------------------
function ms(v) {
  return v === undefined || v === null ? '-' : v.toFixed(1) + 'ms';
}

function valores(data, nome) {
  const m = data.metrics[nome];
  return m && m.values ? m.values : null;
}

function linhaEndpoint(data, nome, rotulo) {
  // Trend nao tem 'count': a contagem vem da submetrica de http_reqs.
  const dur = valores(data, 'http_req_duration{endpoint:' + nome + '}');
  if (!dur) return null;
  const reqs = valores(data, 'http_reqs{endpoint:' + nome + '}') || {};
  const erros = valores(data, 'http_req_failed{endpoint:' + nome + '}') || {};
  return (
    '  ' + rotulo.padEnd(12) +
    ' reqs ' + String(reqs.count === undefined ? '-' : reqs.count).padStart(6) +
    ' | erro ' + (erros.rate === undefined ? '-' : (erros.rate * 100).toFixed(1) + '%').padStart(6) +
    ' | med ' + ms(dur.med).padStart(9) +
    ' | p95 ' + ms(dur['p(95)']).padStart(9) +
    ' | max ' + ms(dur.max).padStart(9)
  );
}

export function handleSummary(data) {
  const http_reqs = data.metrics.http_reqs ? data.metrics.http_reqs.values : {};
  const falhas = data.metrics.http_req_failed ? data.metrics.http_req_failed.values : {};
  const checks = data.metrics.checks ? data.metrics.checks.values : {};
  const jornada = data.metrics.duracao_jornada ? data.metrics.duracao_jornada.values : {};
  const iter = data.metrics.iteration_duration ? data.metrics.iteration_duration.values : {};

  const linhas = [
    '',
    '================================================================',
    '  STRESS O.M.C.E - ' + BASE_URL,
    '================================================================',
    '  Requisicoes ......... ' + (http_reqs.count || 0) +
      '  (' + (http_reqs.rate || 0).toFixed(1) + '/s)',
    '  Taxa de erro ........ ' + ((falhas.rate || 0) * 100).toFixed(2) + '%',
    '  Checks OK ........... ' + ((checks.rate || 0) * 100).toFixed(2) + '%',
    '  VUs maximos ......... ' + VU_MAX,
    '----------------------------------------------------------------',
    '  Por endpoint:',
  ];

  [
    ['filtro', 'catalogo'],
    ['detalhes', 'detalhes'],
    ['avaliacoes', 'avaliacoes'],
    ['login', 'login'],
  ].forEach(function (par) {
    const l = linhaEndpoint(data, par[0], par[1]);
    if (l) linhas.push(l);
  });

  linhas.push('----------------------------------------------------------------');
  // Jornada = soma das 4 requisicoes, SEM o think time. E' o numero a olhar.
  linhas.push('  Jornada (4 reqs) .... p95 ' + ms(jornada['p(95)']));
  // Iteracao = jornada + sleep de 0,5s a 2,5s, entao fica na casa dos segundos
  // por construcao. So' interessa se ficar MUITO acima de jornada + 2,5s, o que
  // indicaria o k6 (e nao o backend) como gargalo.
  linhas.push('  Iteracao (inclui think time de 0,5-2,5s) p95 ' + ms(iter['p(95)']));
  linhas.push('================================================================');
  linhas.push('  Nenhum dado foi gravado: este teste e\' somente leitura.');
  linhas.push('================================================================');
  linhas.push('');

  return {
    stdout: linhas.join('\n'),
    'out/stress-summary.json': JSON.stringify(data, null, 2),
  };
}
