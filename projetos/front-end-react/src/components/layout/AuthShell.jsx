import { Link } from 'react-router-dom';
import { useTheme } from '../../contexts/ThemeContext';
import ThemeToggle from './ThemeToggle';

export default function AuthShell({ title, subtitle, children }) {
  const { theme, toggleTheme } = useTheme();
  return <div className="auth-page"><Link className="brand auth-brand" to="/login"><span className="brand-mark">◈</span> O.M.C.E</Link><ThemeToggle theme={theme} onToggle={toggleTheme} className="auth-theme-toggle" /><section className="auth-card"><p className="eyebrow">MERCADO DE ELETRÔNICOS</p><h1>{title}</h1><p className="auth-subtitle">{subtitle}</p>{children}</section><p className="auth-aside">Componentes, projetos e possibilidades.<br /><strong>Todo circuito começa com uma peça.</strong></p></div>;
}
