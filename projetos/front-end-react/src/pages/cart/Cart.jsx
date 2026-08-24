import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getCart, imageSource, money, saveCart } from '../../api';
import { Empty } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function Cart() {
  const navigate = useNavigate(); const [items, setItems] = useState(getCart()); const total = items.reduce((sum, item) => sum + Number(item.preco), 0);
  const remove = (id) => { const updated = items.filter((item) => item.id !== id); setItems(updated); saveCart(updated); window.dispatchEvent(new Event('cart-updated')); };
  return <Page eyebrow="SEU CARRINHO" title="Pronto para levar?">{items.length ? <div className="cart-layout"><div className="cart-list">{items.map((item) => <div className="cart-item" key={item.id}>{imageSource(item) && <img src={imageSource(item)} alt="" />}<div><h2>{item.nome}</h2><p>{money(item.preco)}</p></div><button className="danger-text" onClick={() => remove(item.id)}>Remover</button></div>)}</div><aside className="order-summary"><p>Resumo do pedido</p><div><span>{items.length} item(ns)</span><span>{money(total)}</span></div><hr /><strong>Total <span>{money(total)}</span></strong><button className="button primary" onClick={() => navigate('/pedido')}>Continuar para entrega</button></aside></div> : <Empty title="Seu carrinho está vazio" text="Explore o catálogo e encontre peças para o seu próximo projeto." action={<Link className="button primary" to="/feed">Explorar produtos</Link>} />}</Page>;
}
