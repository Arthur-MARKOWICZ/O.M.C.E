import { Link } from 'react-router-dom';
import { imageSource, money } from '../../api';

export default function HistoryItem({ product, purchases }) { const image = imageSource(product); return <article className="history-item">{image && <img src={image} alt="" />}<div><p className="category">{product.categoria || 'PRODUTO'}</p><h2>{product.nome}</h2><strong>{money(product.preco)}</strong></div><div className="history-actions"><Link to={`/produto/${product.id}`}>Ver produto</Link>{purchases && <><Link to={`/produto/${product.id}/avaliacoes`}>Avaliar produto</Link>{product.id_vendedor && <Link to={`/vendedor/${product.id_vendedor}/avaliar`}>Avaliar vendedor</Link>}</>}</div></article>; }
