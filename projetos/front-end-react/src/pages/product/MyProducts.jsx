import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { auth, request } from '../../api';
import ProductGrid from '../../components/product/ProductGrid';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';
import { useNotice } from '../../contexts/NoticeContext';

export default function MyProducts() {
  const [result, setResult] = useState(null); const [page, setPage] = useState(0); const [error, setError] = useState(''); const notice = useNotice();
  const load = () => request(`/produto/todosProdutosUsuario?page=${page}`, { headers: { 'Id-Usuario': auth.userId } }).then(setResult).catch((requestError) => setError(requestError.message));
  useEffect(() => { load(); }, [page]);
  const remove = async (product) => { if (!window.confirm(`Excluir o anúncio “${product.nome}”?`)) return; try { await request(`/produto/deletar/${product.id}`, { method: 'DELETE' }); notice({ message: 'Anúncio excluído.' }); load(); } catch (requestError) { setError(requestError.message); } };
  return <Page eyebrow="MEUS ANÚNCIOS" title="Produtos publicados" actions={<Link className="button primary" to="/produto/novo">+ Novo anúncio</Link>}>{error ? <ErrorMessage error={error} /> : !result ? <Loading /> : result.content?.length ? <><ProductGrid products={result.content} onDelete={remove} /><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Você ainda não publicou nada" text="Seu próximo anúncio pode ajudar alguém a criar algo novo." action={<Link className="button primary" to="/produto/novo">Anunciar produto</Link>} />}</Page>;
}