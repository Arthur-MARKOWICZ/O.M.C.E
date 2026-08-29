import { useLocalSearchParams, useRouter } from 'expo-router';
import { ProductForm } from '@/src/components/ProductForm';
import { Button, Message, Screen } from '@/src/components/ui';
import { useApp } from '@/src/context/AppContext';
export default function EditProduct() { const { id } = useLocalSearchParams<{ id: string }>(); const { isVendedor } = useApp(); const router = useRouter(); if (!isVendedor) { return <Screen eyebrow="ACESSO NEGADO" title="Acesso restrito"><Message>Apenas vendedores podem editar produtos.</Message><Button onPress={() => router.replace('/(tabs)/feed')}>Voltar ao feed</Button></Screen>; } return <ProductForm id={id} />; }
