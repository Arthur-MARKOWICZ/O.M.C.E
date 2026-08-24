import { Redirect, Stack } from 'expo-router';
import { useApp } from '@/src/context/AppContext';
export default function AuthLayout() { const { session } = useApp(); if (session) return <Redirect href="/(tabs)" />; return <Stack screenOptions={{ headerShown: false }} />; }
