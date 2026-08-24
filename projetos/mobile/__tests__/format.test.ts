import { imageSource, money, queryString } from '@/src/lib/format';

describe('format helpers', () => {
  it('formats BRL values and product base64 images', () => {
    expect(money(12.5)).toBe('R$ 12,50');
    expect(imageSource({ id: 1, nome: 'ESP32', preco: 10, imagem: 'abc', imagem_tipo: 'image/png' })).toBe('data:image/png;base64,abc');
  });
  it('creates a query without empty filters', () => {
    expect(queryString({ page: 0, nome: 'esp32', categoria: '', precoMin: undefined })).toBe('?page=0&nome=esp32');
  });
});
