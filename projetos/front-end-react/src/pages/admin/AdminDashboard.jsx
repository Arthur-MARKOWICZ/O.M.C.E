import { useEffect, useState } from 'react';
import { request } from '../../api';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

export default function AdminDashboard() {
    const [data, setData] = useState(null);
    const [error, setError] = useState('');

    useEffect(() => {
        async function loadDashboard() {
            try {
                setData({
                    usuarios: 0,
                    vendedores: 0,
                    compradores: 0,
                    produtos: 0
                });
            } catch (requestError) {
                setError(requestError.message);
            }
        }

        loadDashboard();
    }, []);

    if (!data && !error) return <Loading />;

    return (
        <Page eyebrow="ADMINISTRAÇÃO" title="Dashboard">
            {error ? (
                <ErrorMessage error={error} />
            ) : (
                <div className="admin-dashboard">
                    <div className="admin-card">
                        <h2>Usuários</h2>
                        <strong>{data.usuarios}</strong>
                    </div>

                    <div className="admin-card">
                        <h2>Vendedores</h2>
                        <strong>{data.vendedores}</strong>
                    </div>

                    <div className="admin-card">
                        <h2>Compradores</h2>
                        <strong>{data.compradores}</strong>
                    </div>

                    <div className="admin-card">
                        <h2>Produtos</h2>
                        <strong>{data.produtos}</strong>
                    </div>
                </div>
            )}
        </Page>
    );
}