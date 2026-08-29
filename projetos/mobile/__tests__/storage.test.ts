jest.mock('expo-secure-store', () => ({ getItemAsync: jest.fn(), setItemAsync: jest.fn(), deleteItemAsync: jest.fn() }));
jest.mock('@react-native-async-storage/async-storage', () => ({ getItem: jest.fn(), setItem: jest.fn(), removeItem: jest.fn() }));
import * as SecureStore from 'expo-secure-store';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { clearSession, loadCart, loadSession, saveCart, saveSession } from '@/src/lib/storage';

describe('persistent app data', () => {
  beforeEach(() => jest.clearAllMocks());
  it('stores the login session securely', async () => {
    await saveSession({ token: 'jwt', id: 4, nome: 'Ana', role: 'COMPRADOR' });
    expect(SecureStore.setItemAsync).toHaveBeenCalledWith('omce_session', JSON.stringify({ token: 'jwt', id: 4, nome: 'Ana', role: 'COMPRADOR' }));
    (SecureStore.getItemAsync as jest.Mock).mockResolvedValue(JSON.stringify({ token: 'jwt', id: 4, nome: 'Ana', role: 'COMPRADOR' }));
    await expect(loadSession()).resolves.toEqual({ token: 'jwt', id: 4, nome: 'Ana', role: 'COMPRADOR' });
    await clearSession();
    expect(SecureStore.deleteItemAsync).toHaveBeenCalledWith('omce_session');
  });
  it('keeps carts isolated by user', async () => {
    const items = [{ id: 1, nome: 'Sensor', preco: 8 }];
    await saveCart(22, items);
    expect(AsyncStorage.setItem).toHaveBeenCalledWith('omce_cart_22', JSON.stringify(items));
    (AsyncStorage.getItem as jest.Mock).mockResolvedValue(JSON.stringify(items));
    await expect(loadCart(22)).resolves.toEqual(items);
  });
});
