import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { Empty, ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function MyReviews() {
  const notice = useNotice();
  const [tab, setTab] = useState('produto');
  const [productReviews, setProductReviews] = useState(null);
  const [sellerReviews, setSellerReviews] = useState(null);
  const [error, setError] = useState('');

  const load = () => {
    setError('');
    setProductReviews(null);
    setSellerReviews(null);
    Promise.all([
      request('/avaliacoes/minhas'),
      request('/avaliacaoVendedor/minhas'),
    ])
      .then(([products, sellers]) => {
        setProductReviews(products?.content || []);
        setSellerReviews(sellers?.content || []);
      })
      .catch((requestError) => setError(requestError.message));
  };

  useEffect(() => { load(); }, []);

  const remove = async (type, id) => {
    if (!window.confirm('Excluir esta avaliação? Esta ação não pode ser desfeita.')) return;
    try {
      const path = type === 'produto'
        ? `/avaliacoes/deletar/${id}`
        : `/avaliacaoVendedor/deletar/${id}`;
      await request(path, { method: 'DELETE' });
      notice({ message: 'Avaliação excluída.' });
      load();
    } catch (requestError) {
      setError(requestError.message);
    }
  };

  const loading = !productReviews || !sellerReviews;
  const items = loading
    ? []
    : (tab === 'produto' ? productReviews : sellerReviews).map((review) => ({
      ...review,
      tipo: tab,
    }));

  const emptyTitle = tab === 'produto'
    ? 'Nenhuma avaliação de produto'
    : 'Nenhuma avaliação de vendedor';
  const emptyText = tab === 'produto'
    ? 'Quando você avaliar um produto, ele aparecerá aqui.'
    : 'Quando você avaliar um vendedor, ele aparecerá aqui.';

  return (
    <Page
      eyebrow="SUAS AVALIAÇÕES"
      title="O que você escreveu"
      actions={(
        <div className="export-actions" style={{ marginLeft: 0 }}>
          <button
            type="button"
            className={`button ${tab === 'produto' ? 'primary' : 'secondary'}`}
            onClick={() => setTab('produto')}
          >
            Produtos
          </button>
          <button
            type="button"
            className={`button ${tab === 'vendedor' ? 'primary' : 'secondary'}`}
            onClick={() => setTab('vendedor')}
          >
            Vendedores
          </button>
        </div>
      )}
    >
      {error && <ErrorMessage error={error} />}
      {loading && !error ? <Loading /> : null}
      {!loading && !items.length ? (
        <Empty title={emptyTitle} text={emptyText} />
      ) : null}
      {!loading && items.length ? (
        <div className="reviews">
          {items.map((review) => (
            <article className="review" key={`${review.tipo}-${review.id}`}>
              <strong>
                ★ {review.nota}/5 · {review.tipo === 'produto'
                  ? (review.nome_produto || 'Produto')
                  : (review.nome_vendedor || 'Vendedor')}
              </strong>
              <p>{review.comentario || 'Sem comentário.'}</p>
              <div className="history-actions" style={{ marginLeft: 0, marginTop: 12 }}>
                <Link to={`/suas-avaliacoes/${review.tipo}/${review.id}/editar`} state={{ review }}>
                  Editar
                </Link>
                <button className="danger-text" type="button" onClick={() => remove(review.tipo, review.id)}>
                  Excluir
                </button>
              </div>
            </article>
          ))}
        </div>
      ) : null}
    </Page>
  );
}
