import { PropsWithChildren, createContext, useContext, useEffect, useMemo, useState } from 'react';
import { CartItem, Session } from '@/src/types/models';
import { clearSession, loadCart, loadSession, loadTheme, saveCart, saveSession, saveTheme } from '@/src/lib/storage';
import { setApiToken } from '@/src/lib/api';
import { Colors, palettes, ThemeName } from '@/src/constants/theme';

type Notice = { message: string; type?: 'success' | 'error' } | null;
type AppValue = {
  ready: boolean; session: Session | null; cart: CartItem[]; theme: ThemeName; colors: Colors; notice: Notice;
  signIn: (value: Session) => Promise<void>; signOut: () => Promise<void>; toggleTheme: () => void;
  addToCart: (item: CartItem) => Promise<boolean>; removeFromCart: (id: number) => Promise<void>; clearCart: () => Promise<void>;
  showNotice: (message: string, type?: 'success' | 'error') => void;
};
const AppContext = createContext<AppValue | null>(null);

export function AppProvider({ children }: PropsWithChildren) {
  const [ready, setReady] = useState(false); const [session, setSession] = useState<Session | null>(null);
  const [cart, setCart] = useState<CartItem[]>([]); const [theme, setTheme] = useState<ThemeName>('light'); const [notice, setNotice] = useState<Notice>(null);
  useEffect(() => { (async () => {
    const [storedSession, storedTheme] = await Promise.all([loadSession(), loadTheme()]);
    if (storedTheme === 'dark') setTheme('dark');
    if (storedSession) { setSession(storedSession); setApiToken(storedSession.token); setCart(await loadCart(storedSession.id)); }
    setReady(true);
  })(); }, []);
  const signIn = async (value: Session) => { setApiToken(value.token); setSession(value); setCart(await loadCart(value.id)); await saveSession(value); };
  const signOut = async () => { setApiToken(null); setSession(null); setCart([]); await clearSession(); };
  const persistCart = async (items: CartItem[]) => { setCart(items); if (session) await saveCart(session.id, items); };
  const addToCart = async (item: CartItem) => { if (cart.some((current) => current.id === item.id)) return false; await persistCart([...cart, item]); return true; };
  const removeFromCart = (id: number) => persistCart(cart.filter((item) => item.id !== id));
  const clearCart = () => persistCart([]);
  const toggleTheme = () => setTheme((current) => { const next = current === 'light' ? 'dark' : 'light'; void saveTheme(next); return next; });
  const showNotice = (message: string, type: 'success' | 'error' = 'success') => { setNotice({ message, type }); setTimeout(() => setNotice(null), 3500); };
  const value = useMemo(() => ({ ready, session, cart, theme, colors: palettes[theme], notice, signIn, signOut, toggleTheme, addToCart, removeFromCart, clearCart, showNotice }), [ready, session, cart, theme, notice]);
  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
}
export function useApp() { const value = useContext(AppContext); if (!value) throw new Error('useApp must be used inside AppProvider'); return value; }
