import { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function ProductReviewEdit() {
  const { id } = useParams();
  const navigate = useNavigate();
  const notice = useNotice();
  const location = useLocation();
  const [review, setReview] = useState(location.state?.review || null);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (review) return;
    request('/avaliacoes/minhas')
      .then((result) => {
        const found = (result?.content || []).find((item) => String(item.id) === String(id));
        if (!found) throw new Error('Avaliação não encontrada.');
        setReview(found);
      })
      .catch((requestError) => setError(requestError.message));
  }, [id, review]);

  const submit = async (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget));
    setSaving(true);
    try {
      await request(`/avaliacoes/atualizar/${id}`, {
        method: 'PUT',
        body: JSON.stringify({
          nota: Number(values.nota),
          comentario: values.comentario,
        }),
      });
      notice({ message: 'Avaliação atualizada.' });
      navigate('/suas-avaliacoes');
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <Page eyebrow="EDITAR AVALIAÇÃO" title="Atualize sua opinião sobre o produto">
      {error && <ErrorMessage error={error} />}
      {!review && !error ? <Loading /> : null}
      {review ? (
        <form className="form-card compact-form" onSubmit={submit}>
          <label>
            Nota
            <select name="nota" required defaultValue={review.nota}>
              {[1, 2, 3, 4, 5].map((number) => (
                <option key={number} value={number}>{number} estrela{number > 1 ? 's' : ''}</option>
              ))}
            </select>
          </label>
          <label>
            Comentário
            <textarea
              name="comentario"
              required
              rows="5"
              placeholder="Conte como foi a compra."
              defaultValue={review.comentario || ''}
            />
          </label>
          <button className="button primary" disabled={saving}>
            {saving ? 'Salvando…' : 'Salvar alterações'}
          </button>
        </form>
      ) : null}
    </Page>
  );
}
