import { useEffect, useState } from 'react';
import { request, auth } from '../../api';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';
import { useNotice } from '../../contexts/NoticeContext';
import { Link } from 'react-router-dom';

export default function AdminUsuarios() {
    const notice = useNotice();

    const [usuarios, setUsuarios] = useState([]);
    const [usuarioSelecionado, setUsuarioSelecionado] = useState(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    async function carregarUsuarios() {
        try {
            setLoading(true);

            const data = await request('/admin/usuarios');

            setUsuarios(data);
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        carregarUsuarios();
    }, []);

    async function alterarRole(id, role) {
        if (Number(id) === Number(auth.userId)) {
            setError('Você não pode alterar a própria role.');
            carregarUsuarios();
            return;
        }

        try {
            await request(`/admin/usuarios/${id}/role`, {
                method: 'PUT',
                body: JSON.stringify({ role })
            });

            notice({
                message: 'Role alterada com sucesso.'
            });

            carregarUsuarios();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    async function alterarStatus(usuario) {
        if (Number(usuario.id) === Number(auth.userId)) {
            setError('Você não pode alterar o status da própria conta.');
            return;
        }

        const acao = usuario.ativo ? 'desativar' : 'ativar';

        const confirmar = window.confirm(
            `Deseja realmente ${acao} o usuário ${usuario.nome}?`
        );

        if (!confirmar) {
            return;
        }

        try {
            await request(`/admin/usuarios/${usuario.id}/status`, {
                method: 'PUT'
            });

            notice({
                message: `Usuário ${usuario.ativo ? 'desativado' : 'ativado'} com sucesso.`
            });

            carregarUsuarios();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    function visualizarUsuario(usuario) {
        setUsuarioSelecionado(usuario);
    }

    if (loading) {
        return <Loading />;
    }

    return <Page eyebrow="ADMINISTRAÇÃO" title="Usuários">
        <Link to="/admin" className="button">
            ← Voltar ao Dashboard
        </Link>

        {error && <ErrorMessage error={error} />}

        <div className="admin-table">
            <table>
                <thead>
                <tr>
                    <th>Nome</th>
                    <th>Usuário</th>
                    <th>E-mail</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
                </thead>

                <tbody>
                {usuarios.map((usuario) => (
                    <tr key={usuario.id}>

                        <td>{usuario.nome}</td>

                        <td>{usuario.nomeUser}</td>

                        <td>{usuario.email}</td>

                        <td>
                            <select
                                value={usuario.role || 'COMPRADOR'}
                                disabled={Number(usuario.id) === Number(auth.userId)}
                                onChange={(event) =>
                                    alterarRole(usuario.id, event.target.value)
                                }
                            >
                                <option value="COMPRADOR">Comprador</option>
                                <option value="VENDEDOR">Vendedor</option>
                                <option value="MISTO">Misto</option>
                                <option value="ADMIN">Admin</option>
                            </select>
                        </td>

                        <td>
                            {usuario.ativo ? 'Ativo' : 'Inativo'}
                        </td>

                        <td>
                            <button
                                className="button"
                                onClick={() => visualizarUsuario(usuario)}
                            >
                                Visualizar
                            </button>

                            <button
                                className="button"
                                disabled={Number(usuario.id) === Number(auth.userId)}
                                onClick={() => alterarStatus(usuario)}
                            >
                                {usuario.ativo ? 'Desativar' : 'Ativar'}
                            </button>
                        </td>

                    </tr>
                ))}
                </tbody>
            </table>
        </div>

        {usuarioSelecionado && (
            <div className="admin-user-details">
                <h2>Detalhes do usuário</h2>

                <p><strong>ID:</strong> {usuarioSelecionado.id}</p>
                <p><strong>Nome:</strong> {usuarioSelecionado.nome}</p>
                <p><strong>Usuário:</strong> {usuarioSelecionado.nomeUser}</p>
                <p><strong>E-mail:</strong> {usuarioSelecionado.email}</p>
                <p><strong>Telefone:</strong> {usuarioSelecionado.telefone}</p>
                <p><strong>Role:</strong> {usuarioSelecionado.role}</p>
                <p>
                    <strong>Status:</strong>{' '}
                    {usuarioSelecionado.ativo ? 'Ativo' : 'Inativo'}
                </p>

                <button
                    className="button"
                    onClick={() => setUsuarioSelecionado(null)}
                >
                    Fechar
                </button>
            </div>
        )}
    </Page>;
}