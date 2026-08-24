import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../../api';
import ProductGrid from '../../components/product/ProductGrid';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function Feed() {
  const [products, setProducts] = useState([]); const [filters, setFilters] = useState({ nome: '', categoria: '', precoMin: '', precoMax: '' }); const [page, setPage] = useState(0); const [result, setResult] = useState(null); const [error, setError] = useState('');
  useEffect(() => { const query = new URLSearchParams({ page }); Object.entries(filters).forEach(([key, value]) => value && query.set(key, value)); setResult(null); request(`/produto/filtro?${query}`).then((data) => { setProducts(data.content || []); setResult(data); }).catch((requestError) => setError(requestError.message)); }, [page, filters]);
  const update = (event) => { event.preventDefault(); setPage(0); setFilters(Object.fromEntries(new FormData(event.currentTarget))); };
  return <Page eyebrow="MERCADO O.M.C.E" title="Explore componentes" actions={<Link className="button primary" to="/produto/novo">+ Anunciar produto</Link>}><form className="filters" onSubmit={update}><input name="nome" placeholder="Busque por nome" defaultValue={filters.nome} /><select name="categoria" defaultValue={filters.categoria}><option value="">Todas as categorias</option>{['ESP32', 'ARDUINO', 'REGISTORES', 'SENSORES', 'BATERIA', 'CABOS', 'MOTORES', 'CONECTORES', 'OUTRO'].map((item) => <option key={item}>{item}</option>)}</select><input name="precoMin" type="number" min="0" placeholder="Preço mínimo" defaultValue={filters.precoMin} /><input name="precoMax" type="number" min="0" placeholder="Preço máximo" defaultValue={filters.precoMax} /><button className="button primary">Filtrar</button></form>{error ? <ErrorMessage error={error} /> : result === null ? <Loading /> : products.length ? <><ProductGrid products={products} /><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Nenhum produto encontrado" text="Tente ajustar seus filtros para descobrir mais peças." />}</Page>;
}
