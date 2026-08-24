import { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from '../routes/AppRoutes';
import { NoticeContext } from '../contexts/NoticeContext';
import { ThemeContext } from '../contexts/ThemeContext';

export default function App() {
  const [notice, setNotice] = useState(null);
  const [theme, setTheme] = useState(() => localStorage.getItem('theme') || (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'));

  useEffect(() => {
    document.documentElement.dataset.theme = theme;
    document.documentElement.style.colorScheme = theme;
    localStorage.setItem('theme', theme);
  }, [theme]);

  const toggleTheme = () => setTheme((current) => current === 'dark' ? 'light' : 'dark');
  return <ThemeContext.Provider value={{ theme, toggleTheme }}><NoticeContext.Provider value={setNotice}><BrowserRouter><AppRoutes /></BrowserRouter>{notice && <div className={`toast ${notice.type || 'success'}`} role="status">{notice.message}<button onClick={() => setNotice(null)}>×</button></div>}</NoticeContext.Provider></ThemeContext.Provider>;
}
