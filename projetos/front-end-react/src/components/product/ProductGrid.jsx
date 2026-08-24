import ProductCard from './ProductCard';

export default function ProductGrid({ products, onDelete }) { return <div className="product-grid">{products.map((product) => <ProductCard key={product.id} product={product} onDelete={onDelete} />)}</div>; }
