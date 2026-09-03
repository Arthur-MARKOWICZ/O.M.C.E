import { Link } from 'react-router-dom';

export default function AdminMenu() {
    return <nav className="admin-menu">
        <Link to="/admin">Dashboard</Link>
        <Link to="/admin/usuarios">Usuários</Link>
        <Link to="/admin/produtos">Produtos</Link>
    </nav>;
}