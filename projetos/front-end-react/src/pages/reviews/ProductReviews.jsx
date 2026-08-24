import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { request } from '../../api';
import ReviewsList from '../../components/product/ReviewsList';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function ProductReviews() {
  const { id } = useParams(); const [result, setResult] = useState(null); const [page, setPage] = useState(0); const [error, setError] = useState('');
  useEffect(() => { setResult(null); request(`/avaliacoes/produto/${id}?page=${page}`).then(setResult).catch((requestError) => setError(requestError.message)); }, [id, page]);
  return <Page eyebrow="AVALIAÇÕES" title="O que dizem sobre o produto">{error ? <ErrorMessage error={error} /> : !result ? <Loading /> : result.content?.length ? <><ReviewsList reviews={result.content} /><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Ainda não há avaliações" text="Quando alguém avaliar, os comentários aparecerão aqui." />}</Page>;
}
