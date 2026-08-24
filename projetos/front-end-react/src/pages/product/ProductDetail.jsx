import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { getCart, imageSource, money, request, saveCart } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function ProductDetail() {
  const { id } = useParams(); const [product, setProduct] = useState(null); const [rating, setRating] = useState(null); const [error, setError] = useState(''); const notice = useNotice();
  useEffect(() => { request(`/produto/visualizarDetalhesProduto/${id}`).then(setProduct).catch((requestError) => setError(requestError.message)); }, [id]);
  useEffect(() => { if (product?.id_vendedor) request(`/avaliacaoVendedor/media/${product.id_vendedor}`).then(setRating).catch(() => {}); }, [product]);
  const addToCart = () => { const cart = getCart(); if (!cart.some((item) => item.id === product.id)) saveCart([...cart, { id: product.id, nome: product.nome, preco: product.preco, imagem: product.imagem ?? product.Imagem, imagem_tipo: product.imagem_tipo ?? product.Imagem_tipo }]); window.dispatchEvent(new Event('cart-updated')); notice({ message: 'Produto adicionado ao carrinho.' }); };
  if (error) return <Page eyebrow="PRODUTO" title="Não foi possível abrir o produto"><ErrorMessage error={error} /></Page>;
  if (!product) return <Loading />;
  const image = imageSource(product);
  return <section className="detail-page"><Link className="back-link" to="/feed">← Voltar ao catálogo</Link><div className="product-detail"><div className="detail-image">{image ? <img src={image} alt={product.nome} /> : 'Sem imagem'}</div><div className="detail-content"><p className="eyebrow">{product.categoria || 'ELETRÔNICOS'} · {product.condicao}</p><h1>{product.nome}</h1><strong className="detail-price">{money(product.preco)}</strong><p className="detail-description">{product.detalhes || 'Este vendedor ainda não adicionou detalhes a este anúncio.'}</p><div className="seller-box"><span>Vendido por</span><b>{product.nome_do_usuario || product.nomeUsuario || 'Vendedor O.M.C.E'}</b>{rating !== null && <small>★ {Number(rating).toFixed(1)} de avaliação</small>}</div><div className="detail-actions"><button className="button primary" onClick={addToCart}>Adicionar ao carrinho</button><Link className="button secondary" to={`/produto/${id}/avaliacoes`}>Ver avaliações</Link></div></div></div></section>;
}
