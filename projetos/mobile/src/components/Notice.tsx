import { Pressable, StyleSheet, Text, View } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useApp } from '@/src/context/AppContext';
export function Notice() { const { notice, colors } = useApp(); if (!notice) return null; const color = notice.type === 'error' ? colors.danger : colors.green; return <View style={[styles.notice, { backgroundColor: color }]}><Text style={styles.text}>{notice.message}</Text><Ionicons name="checkmark" size={18} color="#fff" /></View>; }
const styles = StyleSheet.create({ notice: { position: 'absolute', bottom: 28, left: 20, right: 20, zIndex: 20, padding: 14, borderRadius: 8, flexDirection: 'row', gap: 10, justifyContent: 'space-between', alignItems: 'center' }, text: { flex: 1, color: '#fff', fontFamily: 'Manrope_700Bold', fontSize: 13 } });
