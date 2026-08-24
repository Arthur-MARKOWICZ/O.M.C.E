import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { useFonts } from 'expo-font';
import { Manrope_400Regular, Manrope_600SemiBold, Manrope_700Bold, Manrope_800ExtraBold } from '@expo-google-fonts/manrope';
import { DMMono_400Regular, DMMono_500Medium } from '@expo-google-fonts/dm-mono';
import { PlayfairDisplay_600SemiBold, PlayfairDisplay_700Bold } from '@expo-google-fonts/playfair-display';
import { ActivityIndicator, View } from 'react-native';
import { AppProvider, useApp } from '@/src/context/AppContext';
import { Notice } from '@/src/components/Notice';

function Navigator() {
  const { ready, theme, colors } = useApp();
  if (!ready) return <View style={{ flex: 1, justifyContent: 'center', backgroundColor: colors.paper }}><ActivityIndicator color={colors.green} /></View>;
  return <><StatusBar style={theme === 'dark' ? 'light' : 'dark'} /><Stack screenOptions={{ headerShown: false, animation: 'slide_from_right', contentStyle: { backgroundColor: colors.paper } }}><Stack.Screen name="index" /><Stack.Screen name="(auth)" /><Stack.Screen name="(tabs)" /><Stack.Screen name="carrinho" /><Stack.Screen name="pedido" /><Stack.Screen name="produto/[id]" /><Stack.Screen name="meus-produtos" /><Stack.Screen name="historico/[type]" /><Stack.Screen name="avaliacoes/index" /><Stack.Screen name="vendedor/[id]/avaliar" /></Stack><Notice /></>;
}
export default function RootLayout() {
  const [fontsLoaded] = useFonts({ Manrope_400Regular, Manrope_600SemiBold, Manrope_700Bold, Manrope_800ExtraBold, DMMono_400Regular, DMMono_500Medium, PlayfairDisplay_600SemiBold, PlayfairDisplay_700Bold });
  if (!fontsLoaded) return null;
  return <SafeAreaProvider><AppProvider><Navigator /></AppProvider></SafeAreaProvider>;
}
