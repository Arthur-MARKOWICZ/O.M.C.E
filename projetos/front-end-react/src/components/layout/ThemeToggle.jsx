export default function ThemeToggle({ theme, onToggle, className = '' }) {
  const isDark = theme === 'dark';
  return <button type="button" className={`theme-toggle ${className}`} onClick={onToggle} aria-label={`Ativar tema ${isDark ? 'claro' : 'escuro'}`} aria-pressed={isDark} title={`Ativar tema ${isDark ? 'claro' : 'escuro'}`}><span aria-hidden="true">{isDark ? '☀' : '☾'}</span><span className="theme-toggle-label">{isDark ? 'Claro' : 'Escuro'}</span></button>;
}
