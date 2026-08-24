import { useState } from 'react';
import { Text } from 'react-native';
import { Link } from 'expo-router';
import { AuthShell } from '@/src/components/AuthShell';
import { Button, Field, Message } from '@/src/components/ui';
import { api } from '@/src/lib/api';
export default function PasswordReset() { const [email, setEmail] = useState(''); const [error, setError] = useState(''); const [success, setSuccess] = useState(''); const submit = async () => { try { await api.requestPasswordReset(email); setSuccess('Enviamos as instruções para seu e-mail.'); setError(''); } catch (e) { setError(e instanceof Error ? e.message : 'Não foi possível enviar as instruções.'); } }; return <AuthShell title="Redefinir senha" subtitle="Informe seu e-mail para receber as instruções.">{error && <Message>{error}</Message>}{success && <Message type="success">{success}</Message>}<Field label="E-mail" value={email} onChangeText={setEmail} keyboardType="email-address" autoCapitalize="none" /><Button onPress={submit}>Enviar instruções</Button><Text style={{ textAlign: 'center' }}><Link href="/(auth)/login" style={{ color: '#176b42', fontFamily: 'Manrope_800ExtraBold' }}>Voltar para entrar</Link></Text></AuthShell>; }
