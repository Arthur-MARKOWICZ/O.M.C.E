import { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { ErrorMessage } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function SellerReviewForm() {
  const { id } = useParams(); const navigate = useNavigate(); const notice = useNotice(); const [error, setError] = useState(''); const [saving, setSaving] = useState(false);
  const submit = async (event) => { event.preventDefault(); const values = Object.fromEntries(new FormData(event.currentTarget)); setSaving(true); try { await request('/avaliacaoVendedor/cadastro', { method: 'POST', body: JSON.stringify({ nota: Number(values.nota), comentario: values.comentario, vendedor_id: Number(id) }) }); notice({ message: 'Avaliação enviada. Obrigado!' }); navigate('/historico/compras'); } catch (requestError) { setError(requestError.message); } finally { setSaving(false); } };
  return <Page eyebrow="AVALIAR VENDEDOR" title="Como foi sua experiência?"><form className="form-card compact-form" onSubmit={submit}>{error && <ErrorMessage error={error} />}<label>Nota<select name="nota" required defaultValue=""><option value="" disabled>Selecione de 1 a 5</option>{[1, 2, 3, 4, 5].map((number) => <option key={number} value={number}>{number} estrela{number > 1 ? 's' : ''}</option>)}</select></label><label>Comentário<textarea name="comentario" required rows="5" placeholder="Conte como foi a compra." /></label><button className="button primary" disabled={saving}>{saving ? 'Enviando…' : 'Enviar avaliação'}</button></form></Page>;
}
