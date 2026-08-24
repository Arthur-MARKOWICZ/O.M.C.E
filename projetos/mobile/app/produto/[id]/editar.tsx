import { useLocalSearchParams } from 'expo-router';
import { ProductForm } from '@/src/components/ProductForm';
export default function EditProduct() { const { id } = useLocalSearchParams<{ id: string }>(); return <ProductForm id={id} />; }
