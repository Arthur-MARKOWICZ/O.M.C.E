import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { auth, request } from '../../api';
import AuthShell from '../../components/layout/AuthShell';
import { ErrorMessage } from '../../components/ui/Feedback';

export default function Login() {
  const navigate = useNavigate();
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const submit = async (event) => { event.preventDefault(); const values = Object.fromEntries(new FormData(event.currentTarget)); setLoading(true); setError(''); try { const data = await request('/auth/login', { method: 'POST', body: JSON.stringify(values) }); auth.save(data); navigate('/'); } catch (requestError) { setError(requestError.message || 'E-mail ou senha inválidos.'); } finally { setLoading(false); } };
  return <AuthShell title="Acesse sua conta" subtitle="Entre para comprar, vender e criar."><form onSubmit={submit}>{error && <ErrorMessage error={error} />}<label>E-mail<input name="email" type="email" required autoComplete="email" /></label><label>Senha<input name="senha" type="password" required autoComplete="current-password" /></label><button className="button primary full" disabled={loading}>{loading ? 'Entrando…' : 'Entrar'}</button></form><div className="auth-links"><Link to="/redefinir-senha">Esqueci minha senha</Link><p>Ainda não tem conta? <Link to="/cadastro">Cadastre-se</Link></p></div></AuthShell>;
}
