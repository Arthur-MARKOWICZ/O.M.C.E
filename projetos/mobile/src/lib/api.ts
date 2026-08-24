import { Platform } from 'react-native';
import { Address, PageResult, Product, Review, Session, User } from '@/src/types/models';

const defaultUrl = Platform.OS === 'android' ? 'http://10.0.2.2:8080' : 'http://localhost:8080';
export const API_URL = (process.env.EXPO_PUBLIC_API_URL || defaultUrl).replace(/\/$/, '');
let token: string | null = null;
export const setApiToken = (value: string | null) => { token = value; };

export class ApiError extends Error { constructor(message: string, public status?: number) { super(message); } }

export async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  if (options.body && !headers.has('Content-Type')) headers.set('Content-Type', 'application/json');
  if (token) headers.set('Authorization', `Bearer ${token}`);
  let response: Response;
  try { response = await fetch(`${API_URL}${path}`, { ...options, headers }); }
  catch { throw new ApiError('Não foi possível conectar à API. Confira a URL configurada e a rede.'); }
  const type = response.headers.get('content-type') || '';
  const body: unknown = type.includes('application/json') ? await response.json() : await response.text();
  if (!response.ok) {
    const message = typeof body === 'object' && body ? (body as { mensagem?: string; message?: string }).mensagem || (body as { message?: string }).message : body;
    throw new ApiError(typeof message === 'string' && message ? message : 'Não foi possível concluir a operação.', response.status);
  }
  return body as T;
}

export const api = {
  login: (email: string, senha: string) => request<Session>('/auth/login', { method: 'POST', body: JSON.stringify({ email, senha }) }),
  register: (body: object) => request<void>('/user/cadastro', { method: 'POST', body: JSON.stringify(body) }),
  requestPasswordReset: (email: string) => request<void>('/user/redefinirSenha', { method: 'POST', body: JSON.stringify({ email }) }),
  resetPassword: (tokenValue: string, senha: string) => request<void>('/user/novaSenha', { method: 'PUT', body: JSON.stringify({ token: tokenValue, senha }) }),
  products: (query: string) => request<PageResult<Product>>(`/produto/filtro${query}`),
  product: (id: string | number) => request<Product>(`/produto/visualizarDetalhesProduto/${id}`),
  createProduct: (body: object) => request<void>('/produto/cadastroProduto', { method: 'POST', body: JSON.stringify(body) }),
  updateProduct: (body: object) => request<void>('/produto/alterarDadosProduto', { method: 'PUT', body: JSON.stringify(body) }),
  deleteProduct: (id: number) => request<void>(`/produto/deletar/${id}`, { method: 'DELETE' }),
  myProducts: (page: number, userId: string | number) => request<PageResult<Product>>(`/produto/todosProdutosUsuario?page=${page}`, { headers: { 'Id-Usuario': String(userId) } }),
  user: (id: string | number) => request<User>(`/user/${id}`),
  updateUser: (body: object) => request<void>('/user/alterardados', { method: 'PUT', body: JSON.stringify(body) }),
  checkout: (body: { id_produtos: number[]; id_comprador: number; valor: number; endereco: Address }) => request<void>('/pedido/cadastro', { method: 'POST', body: JSON.stringify(body) }),
  history: (type: 'compra' | 'vendas', page: number, userId: string | number) => request<PageResult<Product>>(`/historico/${type}?page=${page}`, { headers: { 'Id-Usuario': String(userId) } }),
  productReviews: (id: string | number, page: number) => request<PageResult<Review>>(`/avaliacoes/produto/${id}?page=${page}`),
  createProductReview: (body: { nota: number; comentario: string; idProduto: number }) => request<void>('/avaliacoes/criar', { method: 'POST', body: JSON.stringify(body) }),
  sellerRating: (id: number) => request<number>(`/avaliacaoVendedor/media/${id}`),
  sellerReviews: (id: string | number, page: number) => request<PageResult<Review>>(`/avaliacaoVendedor/${id}?page=${page}`),
  createSellerReview: (body: { nota: number; comentario: string; vendedor_id: number }) => request<void>('/avaliacaoVendedor/cadastro', { method: 'POST', body: JSON.stringify(body) }),
};

export async function lookupCep(cep: string): Promise<Address> {
  const clean = cep.replace(/\D/g, '');
  if (clean.length !== 8) throw new ApiError('Informe um CEP com oito dígitos.');
  const response = await fetch(`https://viacep.com.br/ws/${clean}/json/`);
  const data = await response.json() as { erro?: boolean; logradouro?: string; uf?: string; localidade?: string };
  if (data.erro) throw new ApiError('CEP não encontrado. Confira o número informado.');
  return { cep, logradouro: data.logradouro || '', estado: data.uf || '', cidade: data.localidade || '', pais: 'Brasil' };
}
