import { PropsWithChildren, ReactNode } from 'react';
import { ActivityIndicator, Pressable, ScrollView, StyleSheet, Text, TextInput, TextInputProps, View, ViewStyle } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useApp } from '@/src/context/AppContext';

export function Screen({ children, title, eyebrow, scroll = true, back = false, action }: PropsWithChildren<{ title?: string; eyebrow?: string; scroll?: boolean; back?: boolean; action?: ReactNode }>) {
  const { colors } = useApp(); const insets = useSafeAreaInsets(); const router = useRouter();
  const content = <View style={[styles.content, { paddingTop: insets.top + 14 }]}>{(title || back) && <View style={styles.header}>{back && <Pressable accessibilityLabel="Voltar" onPress={() => router.back()} hitSlop={10}><Ionicons name="arrow-back" size={23} color={colors.ink} /></Pressable>}<View style={styles.headerTitle}>{eyebrow && <Text style={[styles.eyebrow, { color: colors.green }]}>{eyebrow}</Text>}{title && <Text style={[styles.title, { color: colors.ink }]}>{title}</Text>}</View>{action}</View>}{children}</View>;
  return <View style={[styles.page, { backgroundColor: colors.paper }]}>{scroll ? <ScrollView keyboardShouldPersistTaps="handled" contentContainerStyle={styles.scroll}>{content}</ScrollView> : content}</View>;
}

export function Button({ children, onPress, variant = 'primary', disabled, style }: PropsWithChildren<{ onPress: () => void; variant?: 'primary' | 'secondary' | 'danger' | 'text'; disabled?: boolean; style?: ViewStyle }>) {
  const { colors } = useApp(); const palette = variant === 'primary' ? { backgroundColor: colors.green, borderColor: colors.green, color: colors.onAction } : variant === 'danger' ? { backgroundColor: colors.danger, borderColor: colors.danger, color: '#fff' } : variant === 'text' ? { backgroundColor: 'transparent', borderColor: 'transparent', color: colors.green } : { backgroundColor: 'transparent', borderColor: colors.green, color: colors.green };
  return <Pressable disabled={disabled} onPress={onPress} style={({ pressed }) => [styles.button, palette, disabled && styles.disabled, pressed && !disabled && styles.pressed, style]}><Text style={[styles.buttonText, { color: palette.color }]}>{children}</Text></Pressable>;
}

export function Field({ label, error, ...props }: TextInputProps & { label: string; error?: string }) {
  const { colors } = useApp();
  return <View style={styles.field}><Text style={[styles.label, { color: colors.ink }]}>{label}</Text><TextInput placeholderTextColor={colors.muted} style={[styles.input, { color: colors.ink, borderColor: error ? colors.danger : colors.input, backgroundColor: colors.surface }]} {...props} />{error && <Text style={[styles.errorText, { color: colors.danger }]}>{error}</Text>}</View>;
}

export function Choice({ label, value, options, onChange }: { label: string; value: string; options: string[]; onChange: (value: string) => void }) {
  const { colors } = useApp(); return <View style={styles.field}><Text style={[styles.label, { color: colors.ink }]}>{label}</Text><View style={styles.choices}>{options.map((option) => <Pressable key={option} onPress={() => onChange(option)} style={[styles.choice, { borderColor: value === option ? colors.green : colors.input, backgroundColor: value === option ? colors.cream : colors.surface }]}><Text style={[styles.choiceText, { color: value === option ? colors.green : colors.ink }]}>{option}</Text></Pressable>)}</View></View>;
}

export function Message({ children, type = 'error' }: PropsWithChildren<{ type?: 'error' | 'success' }>) { const { colors } = useApp(); const color = type === 'error' ? colors.danger : colors.green; return <View style={[styles.message, { borderColor: color, backgroundColor: colors.surfaceMuted }]}><Text style={{ color }}>{children}</Text></View>; }
export function Loading() { const { colors } = useApp(); return <View style={styles.center}><ActivityIndicator color={colors.green} /><Text style={[styles.muted, { color: colors.muted }]}>Carregando…</Text></View>; }
export function Empty({ title, text, action }: { title: string; text: string; action?: ReactNode }) { const { colors } = useApp(); return <View style={[styles.empty, { borderColor: colors.line, backgroundColor: colors.surfaceMuted }]}><Ionicons name="hardware-chip-outline" size={38} color={colors.green} /><Text style={[styles.emptyTitle, { color: colors.ink }]}>{title}</Text><Text style={[styles.muted, { color: colors.muted }]}>{text}</Text>{action && <View style={{ marginTop: 18 }}>{action}</View>}</View>; }
export function Pagination({ page, totalPages, first, last, onPage }: { page: number; totalPages: number; first: boolean; last: boolean; onPage: (value: number) => void }) { const { colors } = useApp(); if (totalPages <= 1) return null; return <View style={styles.pagination}><Button variant="text" disabled={first} onPress={() => onPage(page - 1)}>← Anterior</Button><Text style={[styles.paginationText, { color: colors.muted }]}>Página {page + 1} de {totalPages}</Text><Button variant="text" disabled={last} onPress={() => onPage(page + 1)}>Próxima →</Button></View>; }

const styles = StyleSheet.create({
  page: { flex: 1 }, scroll: { flexGrow: 1 }, content: { paddingHorizontal: 20, paddingBottom: 38, gap: 18 }, header: { flexDirection: 'row', alignItems: 'flex-start', gap: 14, marginBottom: 8 }, headerTitle: { flex: 1, gap: 4 }, eyebrow: { fontFamily: 'DMMono_500Medium', fontSize: 10, letterSpacing: 1.2 }, title: { fontFamily: 'PlayfairDisplay_700Bold', fontSize: 32, lineHeight: 37 },
  button: { minHeight: 45, paddingHorizontal: 16, borderRadius: 7, borderWidth: 1, justifyContent: 'center', alignItems: 'center' }, buttonText: { fontFamily: 'Manrope_800ExtraBold', fontSize: 13 }, pressed: { opacity: .82 }, disabled: { opacity: .45 },
  field: { gap: 7 }, label: { fontFamily: 'Manrope_700Bold', fontSize: 13 }, input: { minHeight: 46, borderWidth: 1, borderRadius: 7, paddingHorizontal: 12, fontFamily: 'Manrope_400Regular', fontSize: 15 }, errorText: { fontSize: 12 }, choices: { flexDirection: 'row', flexWrap: 'wrap', gap: 8 }, choice: { borderWidth: 1, borderRadius: 7, paddingVertical: 10, paddingHorizontal: 12 }, choiceText: { fontFamily: 'Manrope_700Bold', fontSize: 12 },
  message: { borderWidth: 1, borderRadius: 7, padding: 12 }, center: { minHeight: 180, alignItems: 'center', justifyContent: 'center', gap: 12 }, muted: { fontFamily: 'Manrope_400Regular', fontSize: 14, textAlign: 'center', lineHeight: 21 }, empty: { alignItems: 'center', gap: 9, borderWidth: 1, borderStyle: 'dashed', borderRadius: 9, padding: 34 }, emptyTitle: { fontFamily: 'PlayfairDisplay_700Bold', fontSize: 24, textAlign: 'center' }, pagination: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', gap: 6, marginTop: 8 }, paginationText: { flex: 1, textAlign: 'center', fontFamily: 'DMMono_400Regular', fontSize: 10 },
});
