import { Product } from '@/src/types/models';

export const money = (value: number | string | undefined) => Number(value || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

export const imageSource = (product?: Product | null) => {
  const image = product?.imagem ?? product?.Imagem;
  const type = product?.imagem_tipo ?? product?.Imagem_tipo;
  return image ? `data:${type || 'image/jpeg'};base64,${image}` : undefined;
};

export const queryString = (values: Record<string, string | number | undefined>) => {
  const params = Object.entries(values).filter(([, value]) => value !== '' && value !== undefined).map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(String(value))}`);
  return params.length ? `?${params.join('&')}` : '';
};
