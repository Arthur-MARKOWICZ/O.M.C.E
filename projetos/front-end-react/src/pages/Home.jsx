import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../api';
import ProductGrid from '../components/product/ProductGrid';
import { ErrorMessage, Loading } from '../components/ui/Feedback';
import Page from '../components/ui/Page';

export default function Home() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState('');
  useEffect(() => { request('/produto/filtro?page=0').then((data) => setProducts((data.content || []).slice(-3))).catch((error) => setError(error.message)); }, []);
  return <><section className="hero"><div><p className="eyebrow">COMPRA E VENDA DE ELETRÔNICOS</p><h1>Peças que movem <em>ideias.</em></h1><p className="hero-copy">Encontre componentes eletrônicos, dê novo destino ao que já não usa e transforme projetos em realidade.</p><div className="hero-actions"><Link className="button primary" to="/feed">Explorar produtos</Link><Link className="button secondary" to="/produto/novo">Anunciar peça</Link></div></div><div className="hero-art"><span>⚡</span><p>Do circuito<br />à criação.</p></div></section><Page eyebrow="RECÉM-CHEGADOS" title="Novidades no mercado" actions={<Link className="text-link" to="/feed">Ver todos →</Link>}>{error ? <ErrorMessage error={error} /> : products.length ? <ProductGrid products={products} /> : <Loading />}</Page></>;
}
