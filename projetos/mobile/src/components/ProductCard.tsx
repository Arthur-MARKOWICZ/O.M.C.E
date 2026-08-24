import { Image, Pressable, StyleSheet, Text, View } from 'react-native';
import { Link } from 'expo-router';
import { Product } from '@/src/types/models';
import { imageSource, money } from '@/src/lib/format';
import { useApp } from '@/src/context/AppContext';

export function ProductCard({ product, actions }: { product: Product; actions?: React.ReactNode }) {
  const { colors } = useApp(); const image = imageSource(product);
  const cardStyle = StyleSheet.flatten([styles.card, { borderColor: colors.line, backgroundColor: colors.surface }]);
  const imageBoxStyle = StyleSheet.flatten([styles.imageBox, { backgroundColor: colors.surfaceMuted }]);
  const badgeStyle = StyleSheet.flatten([styles.badge, { backgroundColor: colors.surface }]);
  const badgeTextStyle = StyleSheet.flatten([styles.badgeText, { color: colors.green }]);
  const categoryStyle = StyleSheet.flatten([styles.category, { color: colors.green }]);
  const nameStyle = StyleSheet.flatten([styles.name, { color: colors.ink }]);
  const sellerStyle = StyleSheet.flatten([styles.seller, { color: colors.muted }]);
  const priceStyle = StyleSheet.flatten([styles.price, { color: colors.greenDark }]);
  return <Link href={{ pathname: '/produto/[id]', params: { id: product.id } }} asChild><Pressable style={cardStyle}><View style={imageBoxStyle}>{image ? <Image source={{ uri: image }} style={styles.image} /> : <Text style={{ color: colors.muted }}>Sem imagem</Text>}<View style={badgeStyle}><Text style={badgeTextStyle}>{product.condicao || 'DISPONÍVEL'}</Text></View></View><View style={styles.info}><Text style={categoryStyle}>{product.categoria || 'ELETRÔNICOS'}</Text><Text numberOfLines={2} style={nameStyle}>{product.nome}</Text><Text numberOfLines={1} style={sellerStyle}>por {product.nomeUsuario || product.nome_do_usuario || 'Vendedor O.M.C.E'}</Text><View style={styles.footer}><Text style={priceStyle}>{money(product.preco)}</Text>{actions}</View></View></Pressable></Link>;
}
const styles = StyleSheet.create({ card: { borderWidth: 1, borderRadius: 9, overflow: 'hidden' }, imageBox: { height: 180, justifyContent: 'center', alignItems: 'center' }, image: { width: '100%', height: '100%', resizeMode: 'cover' }, badge: { position: 'absolute', top: 10, left: 10, borderRadius: 4, paddingHorizontal: 7, paddingVertical: 4 }, badgeText: { fontFamily: 'DMMono_500Medium', fontSize: 9, letterSpacing: .5 }, info: { padding: 15, gap: 7 }, category: { fontFamily: 'DMMono_500Medium', fontSize: 10, letterSpacing: .8 }, name: { fontFamily: 'Manrope_800ExtraBold', fontSize: 16, lineHeight: 21 }, seller: { fontFamily: 'Manrope_400Regular', fontSize: 12 }, footer: { marginTop: 4, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }, price: { fontFamily: 'Manrope_800ExtraBold', fontSize: 17 } });
