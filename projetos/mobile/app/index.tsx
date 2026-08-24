import { Redirect } from 'expo-router';
import { useApp } from '@/src/context/AppContext';
export default function Index() { const { session } = useApp(); return <Redirect href={session ? '/(tabs)' : '/(auth)/login'} />; }
