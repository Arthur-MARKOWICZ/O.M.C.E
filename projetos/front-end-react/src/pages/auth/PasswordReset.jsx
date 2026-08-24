import { useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../../api';
import AuthShell from '../../components/layout/AuthShell';
import { ErrorMessage } from '../../components/ui/Feedback';

export default function PasswordReset() {
  const [message, setMessage] = useState(''); const [error, setError] = useState('');
  const submit = async (event) => { event.preventDefault(); try { await request('/user/redefinirSenha', { method: 'POST', body: JSON.stringify(Object.fromEntries(new FormData(event.currentTarget))) }); setMessage('Enviamos as instruções para seu e-mail.'); } catch (requestError) { setError(requestError.message); } };
  return <AuthShell title="Redefinir senha" subtitle="Informe seu e-mail para receber as instruções."><form onSubmit={submit}>{error && <ErrorMessage error={error} />}{message && <div className="feedback success">{message}</div>}<label>E-mail<input name="email" type="email" required /></label><button className="button primary full">Enviar instruções</button></form><div className="auth-links"><Link to="/login">Voltar para entrar</Link></div></AuthShell>;
}
