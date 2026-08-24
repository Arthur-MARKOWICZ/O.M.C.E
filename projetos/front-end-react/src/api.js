const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

export const auth = {
  get token() { return localStorage.getItem('jwt'); },
  get userId() { return localStorage.getItem('id_usuario'); },
  get name() { return localStorage.getItem('nome'); },
  loggedIn() { return Boolean(this.token && this.userId); },
  save({ token, id, nome }) {
    localStorage.setItem('jwt', token);
    localStorage.setItem('id_usuario', id);
    localStorage.setItem('nome', nome);
  },
  clear() { ['jwt', 'id_usuario', 'nome'].forEach((key) => localStorage.removeItem(key)); },
};

export async function request(path, options = {}) {
  const headers = { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...options.headers };
  if (auth.token) headers.Authorization = `Bearer ${auth.token}`;
  let response;
  try {
    response = await fetch(`${API_URL}${path}`, { ...options, headers });
  } catch (error) {
    if (error instanceof TypeError) {
      throw new Error('Não foi possível conectar à API. Verifique se ela está em execução e se o CORS permite este endereço.');
    }
    throw error;
  }
  const contentType = response.headers.get('content-type') || '';
  const body = contentType.includes('application/json') ? await response.json() : await response.text();
  if (!response.ok) throw new Error(body?.mensagem || body?.message || (typeof body === 'string' && body) || 'Não foi possível concluir a operação.');
  return body;
}

export const imageSource = (product) => {
  const image = product.imagem ?? product.Imagem;
  const type = product.imagem_tipo ?? product.Imagem_tipo;
  return image ? `data:${type || 'image/jpeg'};base64,${image}` : null;
};

export const cartKey = () => `carrinho_${auth.userId}`;
export const getCart = () => JSON.parse(localStorage.getItem(cartKey()) || '[]');
export const saveCart = (items) => localStorage.setItem(cartKey(), JSON.stringify(items));
export const money = (value) => Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
