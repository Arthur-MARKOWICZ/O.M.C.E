export type Role = 'COMPRADOR' | 'VENDEDOR' | 'MISTO';
export type Address = { cep?: string; logradouro?: string; estado?: string; cidade?: string; pais?: string };
export type Session = { token: string; id: number | string; nome: string; role: Role };
export type Product = {
  id: number; nome: string; preco: number; detalhes?: string; categoria?: string; condicao?: string;
  imagem?: string; Imagem?: string; imagem_tipo?: string; Imagem_tipo?: string;
  id_vendedor?: number; id_usuario?: number; nomeUsuario?: string; nome_do_usuario?: string;
};
export type PageResult<T> = { content: T[]; totalPages: number; first: boolean; last: boolean; number?: number };
export type Review = { id?: number; nota: number; comentario?: string };
export type User = { id: number; nome: string; nomeUser: string; email: string; telefone: string; cpf: string; dataNasc: string; sexo?: string; endereco?: Address; role?: Role };
export type CartItem = Pick<Product, 'id' | 'nome' | 'preco' | 'imagem' | 'Imagem' | 'imagem_tipo' | 'Imagem_tipo'>;
