import { useEffect, useState } from 'react';
import { auth, request } from '../../api';
import ReviewsList from '../../components/product/ReviewsList';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Pagination from '../../components/ui/Pagination';
import Page from '../../components/ui/Page';

export default function SellerReviews() {
  const [result, setResult] = useState(null); const [page, setPage] = useState(0); const [error, setError] = useState(''); const [media, setMedia] = useState(null);
  useEffect(() => { setResult(null); request(`/avaliacaoVendedor/${auth.userId}?page=${page}`).then(setResult).catch((requestError) => setError(requestError.message)); }, [page]);
  useEffect(() => { request(`/avaliacaoVendedor/media/${auth.userId}`).then(setMedia).catch(() => {}); }, []);
  const mediaLabel = media !== null && <span className="rating-average">★ {Number(media).toFixed(1)} de média</span>;
  return <Page eyebrow="MINHAS AVALIAÇÕES" title="Sua reputação" actions={mediaLabel}>{error ? <ErrorMessage error={error} /> : !result ? <Loading /> : result.content?.length ? <><ReviewsList reviews={result.content} /><Pagination page={page} result={result} onPage={setPage} /></> : <Empty title="Ainda não há avaliações" text="Quando alguém avaliar, os comentários aparecerão aqui." />}</Page>;
}