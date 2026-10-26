import { useEffect, useState } from 'react';
import { Navigate, useParams } from 'react-router-dom';
import { auth, request } from '../../api';
import ExportBar from '../../components/product/ExportBar';
import FilterBar, { FILTROS_VAZIOS, montarConsulta, temFiltro } from '../../components/product/FilterBar';
import HistoryItem from '../../components/product/HistoryItem';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function History() {
  const { type } = useParams();
  return <HistoryList key={type} type={type} />;
}

function HistoryList({ type }) {
  const purchases = type === 'compras';
  const allowed = purchases ? auth.isComprador() : auth.isVendedor();
  const [result, setResult] = useState(null);
  const [page, setPage] = useState(0);
  const [filters, setFilters] = useState(FILTROS_VAZIOS);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!allowed) return;
    setResult(null);
    setError('');
    request(`/historico/${purchases ? 'compra' : 'vendas'}?${montarConsulta(page, filters)}`, { headers: { 'Id-Usuario': auth.userId } })
      .then(setResult)
      .catch((requestError) => setError(requestError.message));
  }, [page, filters, purchases, allowed]);

  if (!allowed) return <Navigate to="/feed" replace />;

  const filter = (values) => { setPage(0); setFilters(values); };
  const vazio = temFiltro(filters)
    ? <Empty title="Nenhum resultado encontrado" text="Tente ajustar seus filtros." />
    : <Empty title="Ainda não há movimentações" text={purchases ? 'As suas compras aparecerão aqui.' : 'As vendas dos seus produtos aparecerão aqui.'} />;

  return (
    <Page eyebrow="HISTÓRICO" title={purchases ? 'Minhas compras' : 'Minhas vendas'}>
      {purchases && <ExportBar />}
      <FilterBar filters={filters} onSubmit={filter} />
      {error && <ErrorMessage error={error} />}
      {!result ? <Loading /> : result.content?.length
        ? <><div className="history-list">{result.content.map((product) => <HistoryItem key={product.id} product={product} purchases={purchases} />)}</div><Pagination page={page} result={result} onPage={setPage} /></>
        : vazio}
    </Page>
  );
}