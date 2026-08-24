export function Empty({ title, text, action }) { return <div className="empty"><span>⌁</span><h2>{title}</h2><p>{text}</p>{action}</div>; }
export function Loading() { return <div className="loading">Carregando…</div>; }
export function ErrorMessage({ error }) { return <div className="feedback error">{error}</div>; }
