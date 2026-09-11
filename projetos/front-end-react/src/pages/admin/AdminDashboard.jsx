import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../../api';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function AdminDashboard() {
    const [data, setData] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        request('/admin/dashboard')
            .then(setData)
            .catch((requestError) => setError(requestError.message));
    }, []);

    if (!data && !error) return <Loading />;

    return <Page eyebrow="ADMINISTRAÇÃO" title="Dashboard">
        {error ? <ErrorMessage error={error} /> : <>
            <div className="admin-dashboard">
                <div className="admin-card">
                    <h2>Usuários</h2>
                    <strong>{data.totalUsuarios}</strong>
                </div>

                <div className="admin-card">
                    <h2>Compradores</h2>
                    <strong>{data.totalCompradores}</strong>
                </div>

                <div className="admin-card">
                    <h2>Vendedores</h2>
                    <strong>{data.totalVendedores}</strong>
                </div>

                <div className="admin-card">
                    <h2>Produtos</h2>
                    <strong>{data.totalProdutos}</strong>
                </div>
            </div>

            <div className="admin-actions">
                <Link to="/admin/usuarios" className="button primary">
                    Gerenciar usuários
                </Link>

                <Link to="/admin/produtos" className="button primary">
                    Gerenciar produtos
                </Link>
            </div>
        </>}
    </Page>;
}