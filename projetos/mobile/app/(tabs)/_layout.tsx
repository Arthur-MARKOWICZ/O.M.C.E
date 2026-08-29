import { Tabs, Redirect, Link } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';
import { Pressable, Text, View } from 'react-native';
import { useApp } from '@/src/context/AppContext';
export default function TabsLayout() {
  const { session, colors, cart, isVendedor } = useApp(); if (!session) return <Redirect href="/(auth)/login" />;
  const cartButton = <Link href="/carrinho" asChild><Pressable accessibilityLabel="Carrinho" style={{ marginRight: 16 }}><Ionicons name="cart-outline" size={24} color={colors.ink} />{cart.length > 0 && <View style={{ position: 'absolute', top: -7, right: -10, minWidth: 16, height: 16, borderRadius: 8, alignItems: 'center', justifyContent: 'center', backgroundColor: colors.lime }}><Text style={{ fontSize: 9, fontFamily: 'DMMono_500Medium', color: colors.ink }}>{cart.length}</Text></View>}</Pressable></Link>;
  const base = { headerStyle: { backgroundColor: colors.paper }, headerShadowVisible: false, headerTitleStyle: { fontFamily: 'Manrope_800ExtraBold', color: colors.ink }, tabBarStyle: { backgroundColor: colors.surface, borderTopColor: colors.line }, tabBarActiveTintColor: colors.green, tabBarInactiveTintColor: colors.muted, tabBarLabelStyle: { fontFamily: 'Manrope_700Bold', fontSize: 10 } };
  return <Tabs screenOptions={({ route }) => ({ ...base, headerRight: () => cartButton, tabBarIcon: ({ color, size }) => <Ionicons name={route.name === 'index' ? 'home-outline' : route.name === 'feed' ? 'search-outline' : route.name === 'anunciar' ? 'add-circle-outline' : 'person-outline'} color={color} size={size} /> })}><Tabs.Screen name="index" options={{ title: 'O.M.C.E' }} /><Tabs.Screen name="feed" options={{ title: 'Explorar' }} /><Tabs.Screen name="anunciar" options={{ title: 'Anunciar', href: isVendedor ? undefined : null }} /><Tabs.Screen name="conta" options={{ title: 'Conta' }} /></Tabs>;
}
