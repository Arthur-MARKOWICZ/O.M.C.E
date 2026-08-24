import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { request } from '../../api';
import AuthShell from '../../components/layout/AuthShell';
import { ErrorMessage } from '../../components/ui/Feedback';

export default function NewPassword() {
  const navigate = useNavigate(); const [error, setError] = useState(''); const [params] = useSearchParams();
  const submit = async (event) => { event.preventDefault(); const values = Object.fromEntries(new FormData(event.currentTarget)); if (values.senha !== values.confirmarSenha) return setError('As senhas não coincidem.'); try { await request('/user/novaSenha', { method: 'PUT', body: JSON.stringify({ token: params.get('token'), senha: values.senha }) }); navigate('/login'); } catch (requestError) { setError(requestError.message); } };
  return <AuthShell title="Escolha uma nova senha" subtitle="Use uma senha forte e que você não use em outros sites."><form onSubmit={submit}>{error && <ErrorMessage error={error} />}<label>Nova senha<input name="senha" type="password" required /></label><label>Confirme a nova senha<input name="confirmarSenha" type="password" required /></label><button className="button primary full">Atualizar senha</button></form></AuthShell>;
}
