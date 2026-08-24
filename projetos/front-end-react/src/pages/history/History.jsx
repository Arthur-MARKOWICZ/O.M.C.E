import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { auth, request } from '../../api';
import HistoryItem from '../../components/product/HistoryItem';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function History() {
  const { type } = useParams(); const [result, setResult] = useState(null); const [page, setPage] = useState(0); const [error, setError] = useState(''); const purchases = type === 'compras';
  useEffect(() => { setResult(null); request(`/historico/${purchases ? 'compra' : 'vendas'}?page=${page}`, { headers: { 'Id-Usuario': auth.userId } }).then(setResult).catch((requestError) => setError(requestError.message)); }, [page, purchases]);
  return <Page eyebrow="HISTÓRICO" title={purchases ? 'Minhas compras' : 'Minhas vendas'}>{error ? <ErrorMessage error={error} /> : !result ? <Loading /> : result.content?.length ? <><div className="history-list">{result.content.map((product) => <HistoryItem key={product.id} product={product} purchases={purchases} />)}</div><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Ainda não há movimentações" text={purchases ? 'As suas compras aparecerão aqui.' : 'As vendas dos seus produtos aparecerão aqui.'} />}</Page>;
}
