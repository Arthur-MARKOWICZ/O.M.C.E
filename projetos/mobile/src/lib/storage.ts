import AsyncStorage from '@react-native-async-storage/async-storage';
import * as SecureStore from 'expo-secure-store';
import { Platform } from 'react-native';
import { CartItem, Session } from '@/src/types/models';

const sessionKey = 'omce_session';
const cartKey = (userId: string | number) => `omce_cart_${userId}`;

const getSessionValue = () => Platform.OS === 'web' ? AsyncStorage.getItem(sessionKey) : SecureStore.getItemAsync(sessionKey);
const setSessionValue = (value: string) => Platform.OS === 'web' ? AsyncStorage.setItem(sessionKey, value) : SecureStore.setItemAsync(sessionKey, value);
const deleteSessionValue = () => Platform.OS === 'web' ? AsyncStorage.removeItem(sessionKey) : SecureStore.deleteItemAsync(sessionKey);

export async function loadSession(): Promise<Session | null> {
  const raw = await getSessionValue();
  return raw ? JSON.parse(raw) : null;
}
export const saveSession = (session: Session) => setSessionValue(JSON.stringify(session));
export const clearSession = () => deleteSessionValue();
export async function loadCart(userId: string | number): Promise<CartItem[]> {
  const raw = await AsyncStorage.getItem(cartKey(userId));
  return raw ? JSON.parse(raw) : [];
}
export const saveCart = (userId: string | number, items: CartItem[]) => AsyncStorage.setItem(cartKey(userId), JSON.stringify(items));
export const loadTheme = () => AsyncStorage.getItem('omce_theme');
export const saveTheme = (value: string) => AsyncStorage.setItem('omce_theme', value);
