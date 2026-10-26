import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { auth, request } from '../../api';
import FilterBar from '../../components/product/FilterBar';
import ProductGrid from '../../components/product/ProductGrid';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function Feed() {
  const [products, setProducts] = useState([]); const [filters, setFilters] = useState({ nome: '', categoria: '', precoMin: '', precoMax: '' }); const [page, setPage] = useState(0); const [result, setResult] = useState(null); const [error, setError] = useState('');
  useEffect(() => { const query = new URLSearchParams({ page }); Object.entries(filters).forEach(([key, value]) => value && query.set(key, value)); setResult(null); request(`/produto/filtro?${query}`).then((data) => { setProducts(data.content || []); setResult(data); }).catch((requestError) => setError(requestError.message)); }, [page, filters]);
  const update = (values) => { setPage(0); setFilters(values); };
  return <Page eyebrow="MERCADO O.M.C.E" title="Explore componentes" actions={auth.role !== 'COMPRADOR' ? <Link className="button primary" to="/produto/novo">+ Anunciar produto</Link> : null}><FilterBar filters={filters} onSubmit={update} />{error && <ErrorMessage error={error} />}{result === null ? <Loading /> : products.length ? <><ProductGrid products={products} /><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Nenhum produto encontrado" text="Tente ajustar seus filtros para descobrir mais peças." />}</Page>;
}