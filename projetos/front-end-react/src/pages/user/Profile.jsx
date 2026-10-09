import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auth, request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';
import { semNumeros, formatarTelefone, formatarCpf, formatarCep, cpfValido, telefoneValido, idadeMinimaValida, senhaForte, PATTERN_SENHA_FORTE, DICA_SENHA_FORTE, PATTERN_TELEFONE } from '../../utils/validation';

export default function Profile() {
  const notice = useNotice();
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [address, setAddress] = useState({});
  const [nome, setNome] = useState('');
  const [telefone, setTelefone] = useState('');
  const [cpf, setCpf] = useState('');
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const [senhaExclusao, setSenhaExclusao] = useState('');
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    request(`/user/${auth.userId}`)
      .then((data) => {
        setUser(data);
        setAddress(data.endereco || {});
        setNome(data.nome || '');
        setTelefone(data.telefone || '');
        setCpf(data.cpf || '');
      })
      .catch((requestError) => setError(requestError.message));
  }, []);

  const submit = async (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget));
    if (!telefoneValido(values.telefone)) return setError('Informe o telefone completo, com DDD.');
    if (!cpfValido(values.cpf)) return setError('CPF inválido. Confira o número informado.');
    if (!idadeMinimaValida(values.dataNasc)) return setError('É preciso ter pelo menos 14 anos.');
    if (values.novaSenha && !senhaForte(values.novaSenha)) return setError('A nova senha precisa ter ao menos 8 caracteres, com letra maiúscula, minúscula, número e símbolo.');
    setSaving(true);
    setError('');
    try {
      await request('/user/alterardados', {
        method: 'PUT',
        body: JSON.stringify({
          ...user,
          ...values,
          id: Number(auth.userId),
          endereco: { ...address, pais: 'Brasil' },
          novaSenha: values.novaSenha,
        }),
      });
      localStorage.setItem('nome', values.nome);
      notice({ message: 'Dados atualizados com sucesso.' });
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  const excluirConta = async () => {
    if (!senhaExclusao) return setError('Informe sua senha para excluir a conta.');
    if (!window.confirm('Tem certeza que deseja excluir sua conta? Esta ação não pode ser desfeita.')) return;
    setDeleting(true);
    setError('');
    try {
      await request(`/user/deletar/${auth.userId}`, {
        method: 'DELETE',
        body: JSON.stringify({ senha: senhaExclusao }),
      });
      auth.clear();
      notice({ message: 'Conta excluída com sucesso.' });
      navigate('/login');
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setDeleting(false);
    }
  };

  if (!user && !error) return <Loading />;

  return (
    <Page eyebrow="MINHA CONTA" title="Seus dados">
      {error && <ErrorMessage error={error} />}
      {user && (
        <>
          <form className="form-card profile-form" onSubmit={submit}>
            <div className="form-row">
              <label>Nome<input name="nome" required value={nome} onChange={(event) => setNome(semNumeros(event.target.value))} /></label>
              <label>Nome de usuário<input name="nomeUser" defaultValue={user.nomeUser} required /></label>
            </div>
            <div className="form-row">
              <label>E-mail<input name="email" type="email" defaultValue={user.email} required /></label>
              <label>Telefone<input name="telefone" required inputMode="numeric" value={telefone} onChange={(event) => setTelefone(formatarTelefone(event.target.value))} placeholder="(00) 00000-0000" maxLength={15} pattern={PATTERN_TELEFONE} title="Preencha o telefone completo, com DDD." /></label>
            </div>
            <div className="form-row">
              <label>CPF<input name="cpf" inputMode="numeric" required value={cpf} onChange={(event) => setCpf(formatarCpf(event.target.value))} placeholder="000.000.000-00" maxLength={14} /></label>
              <label>Data de nascimento<input name="dataNasc" type="date" defaultValue={user.dataNasc} required min="1900-01-01" max={new Date().toISOString().slice(0, 10)} /></label>
            </div>
            <h2>Endereço</h2>
            <label>CEP<input value={address.cep || ''} onChange={(event) => setAddress({ ...address, cep: formatarCep(event.target.value) })} placeholder="00000-000" maxLength={9} /></label>
            <div className="form-row">
              <label>Logradouro<input value={address.logradouro || ''} onChange={(event) => setAddress({ ...address, logradouro: event.target.value })} /></label>
              <label>Cidade<input value={address.cidade || ''} onChange={(event) => setAddress({ ...address, cidade: event.target.value })} /></label>
              <label>Estado<input value={address.estado || ''} onChange={(event) => setAddress({ ...address, estado: event.target.value })} /></label>
            </div>
            <h2>Segurança</h2>
            <div className="form-row">
              <label>Senha atual<input type="password" name="senha" required /></label>
              <label>Nova senha <small>(opcional)</small><input type="password" name="novaSenha" minLength={8} pattern={PATTERN_SENHA_FORTE} title={DICA_SENHA_FORTE} /></label>
            </div>
            <small>Se for trocar, {DICA_SENHA_FORTE.charAt(0).toLowerCase() + DICA_SENHA_FORTE.slice(1)}</small>
            <button className="button primary" disabled={saving}>{saving ? 'Salvando…' : 'Salvar alterações'}</button>
          </form>

          <section className="danger-zone">
            <h2>Excluir conta</h2>
            <p>A exclusão remove sua conta e os dados vinculados do banco de dados. Informe sua senha atual para confirmar.</p>
            <label>
              Senha atual
              <input
                type="password"
                value={senhaExclusao}
                onChange={(event) => setSenhaExclusao(event.target.value)}
                placeholder="Digite sua senha"
                autoComplete="current-password"
              />
            </label>
            <button type="button" className="button danger" disabled={deleting} onClick={excluirConta}>
              {deleting ? 'Excluindo…' : 'Excluir minha conta'}
            </button>
          </section>
        </>
      )}
    </Page>
  );
}
