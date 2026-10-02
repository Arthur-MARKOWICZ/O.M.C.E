export const semNumeros = (valor) => valor.replace(/[0-9]/g, '');

export const formatarTelefone = (valor) => {
  const digitos = valor.replace(/\D/g, '').slice(0, 11);
  if (!digitos) return '';
  if (digitos.length <= 2) return `(${digitos}`;
  if (digitos.length <= 6) return `(${digitos.slice(0, 2)}) ${digitos.slice(2)}`;
  if (digitos.length <= 10) return `(${digitos.slice(0, 2)}) ${digitos.slice(2, 6)}-${digitos.slice(6)}`;
  return `(${digitos.slice(0, 2)}) ${digitos.slice(2, 7)}-${digitos.slice(7)}`;
};

export const formatarCpf = (valor) => valor.replace(/\D/g, '').slice(0, 11)
  .replace(/(\d{3})(\d)/, '$1.$2')
  .replace(/(\d{3})(\d)/, '$1.$2')
  .replace(/(\d{3})(\d{1,2})$/, '$1-$2');

export const formatarCep = (valor) => valor.replace(/[^\d-]/g, '');

export const telefoneValido = (valor) => {
  const digitos = (valor || '').replace(/\D/g, '');
  return digitos.length === 10 || digitos.length === 11;
};

export const PATTERN_TELEFONE = '\\(\\d{2}\\) \\d{4,5}-\\d{4}';

export const cpfValido = (valorFormatado) => {
  const digitos = (valorFormatado || '').replace(/\D/g, '');
  if (digitos.length !== 11 || /^(\d)\1{10}$/.test(digitos)) return false;
  const numeros = digitos.split('').map(Number);
  const digitoVerificador = (tamanho) => {
    let soma = 0;
    for (let i = 0; i < tamanho; i++) soma += numeros[i] * (tamanho + 1 - i);
    const resto = (soma * 10) % 11;
    return resto === 10 ? 0 : resto;
  };
  return digitoVerificador(9) === numeros[9] && digitoVerificador(10) === numeros[10];
};

export const idadeMinimaValida = (dataNasc) => {
  if (!dataNasc) return false;
  const nascimento = new Date(`${dataNasc}T00:00:00`);
  const hoje = new Date();
  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const aniversarioJaPassou = hoje.getMonth() > nascimento.getMonth()
    || (hoje.getMonth() === nascimento.getMonth() && hoje.getDate() >= nascimento.getDate());
  if (!aniversarioJaPassou) idade -= 1;
  return idade >= 14;
};

export const senhaForte = (senha) => /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/.test(senha || '');

export const PATTERN_SENHA_FORTE = '(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}';
export const DICA_SENHA_FORTE = 'Só aceitamos senhas fortes: ao menos 8 caracteres, com letra maiúscula, minúscula, número e símbolo.';
