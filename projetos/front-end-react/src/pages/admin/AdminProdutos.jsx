import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../../api';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';
import { useNotice } from '../../contexts/NoticeContext';

export default function AdminProdutos() {
    const notice = useNotice();

    const [produtos, setProdutos] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(true);

    async function carregarProdutos() {
        try {
            setLoading(true);

            const data = await request('/admin/produtos');

            setProdutos(data);
        } catch (requestError) {
            setError(requestError.message);
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        carregarProdutos();
    }, []);

    async function excluirProduto(id) {
        const confirmar = window.confirm(
            'Tem certeza que deseja excluir este produto?'
        );

        if (!confirmar) {
            return;
        }

        try {
            await request(`/admin/produtos/${id}`, {
                method: 'DELETE'
            });

            notice({
                message: 'Produto excluído com sucesso.'
            });

            carregarProdutos();
        } catch (requestError) {
            setError(requestError.message);
        }
    }

    if (loading) {
        return <Loading />;
    }

    return (
        <Page eyebrow="ADMINISTRAÇÃO" title="Produtos">

            <Link to="/admin" className="button">
                ← Voltar ao Dashboard
            </Link>

            {error && <ErrorMessage error={error} />}

            <div className="admin-table">

                <table>

                    <thead>
                    <tr>
                        <th>Produto</th>
                        <th>Preço</th>
                        <th>Categoria</th>
                        <th>Vendido</th>
                        <th>Ações</th>
                    </tr>
                    </thead>

                    <tbody>

                    {produtos.map((produto) => (

                        <tr key={produto.id}>

                            <td>{produto.nome}</td>

                            <td>
                                R$ {Number(produto.preco).toFixed(2)}
                            </td>

                            <td>{produto.categoria}</td>

                            <td>
                                {produto.vendido ? 'Sim' : 'Não'}
                            </td>

                            <td>

                                <button
                                    className="button"
                                    onClick={() => excluirProduto(produto.id)}
                                >
                                    Excluir
                                </button>

                            </td>

                        </tr>

                    ))}

                    </tbody>

                </table>

            </div>

        </Page>
    );
}