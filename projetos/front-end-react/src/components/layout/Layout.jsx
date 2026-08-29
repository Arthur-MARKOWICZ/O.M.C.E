import { useEffect, useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { auth, getCart } from '../../api';
import { useTheme } from '../../contexts/ThemeContext';
import ThemeToggle from './ThemeToggle';

export default function Layout({ children }) {
  const navigate = useNavigate();
  const { theme, toggleTheme } = useTheme();
  const [cartSize, setCartSize] = useState(getCart().length);
  useEffect(() => {
    const update = () => setCartSize(getCart().length);
    window.addEventListener('storage', update);
    window.addEventListener('cart-updated', update);
    return () => { window.removeEventListener('storage', update); window.removeEventListener('cart-updated', update); };
  }, []);
  const logout = () => { auth.clear(); navigate('/login'); };
  const canSell = auth.isVendedor();
  const nav = [['/', 'Início'], ['/feed', 'Explorar'], ...(canSell ? [['/produto/novo', 'Anunciar']] : []), ['/minha-conta', 'Minha conta']];
  return <><header className="topbar"><Link className="brand" to="/"><span className="brand-mark">◈</span><span>O.M.C.E</span></Link><nav>{nav.map(([to, label]) => <NavLink key={to} to={to} end={to === '/'}>{label}</NavLink>)}</nav><div className="top-actions"><ThemeToggle theme={theme} onToggle={toggleTheme} /><Link className="cart-button" to="/carrinho" aria-label="Carrinho">🛒<b>{cartSize}</b></Link><details className="account-menu"><summary>{auth.name || 'Minha conta'}</summary><div>{canSell && <Link to="/meus-produtos">Meus produtos</Link>}<Link to="/historico/compras">Minhas compras</Link>{canSell && <Link to="/historico/vendas">Minhas vendas</Link>}{canSell && <Link to="/avaliacoes">Minhas avaliações</Link>}<button onClick={logout}>Sair</button></div></details></div></header><main>{children}</main></>;
}
