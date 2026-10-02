import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auth, cartKey, getCart, money, request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { Empty, ErrorMessage } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';

const PIX_DESTINO = 'https://www.youtube.com/watch?v=dQw4w9WgXcQ';
const PIX_QR = `https://api.qrserver.com/v1/create-qr-code/?size=190x190&data=${encodeURIComponent(PIX_DESTINO)}`;

export default function Checkout() {
  const navigate = useNavigate();
  const notice = useNotice();
  const [address, setAddress] = useState({});
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);
  const items = getCart();
  const total = items.reduce((sum, item) => sum + Number(item.preco), 0);
  const [opcoesEntrega, setOpcoesEntrega] = useState([]);
  const [tipoEntrega, setTipoEntrega] = useState('PADRAO');
  const [metodoPagamento, setMetodoPagamento] = useState('');
  const [cartao, setCartao] = useState({ numero: '', nome: '', validade: '', cvv: '' });
  useEffect(() => {
    if (!total) return;
    request(`/entrega/opcoes?valor=${total}&estado=${address.estado || ''}`)
      .then(setOpcoesEntrega)
      .catch(() => {});
  }, [total, address.estado]);
  const entregaEscolhida = opcoesEntrega.find((opcao) => opcao.tipo === tipoEntrega);
  const frete = entregaEscolhida?.valorFrete || 0;
  const totalComFrete = total + frete;
  const lookupCep = async (cep) => {
    const cepLimpo = cep.replace(/\D/g, '');
    if (cepLimpo.length !== 8) return;
    try {
      const data = await fetch(`https://viacep.com.br/ws/${cepLimpo}/json/`).then((res) => res.json());
      if (data.erro) throw new Error();
      setAddress((current) => ({
        ...current,
        cep,
        logradouro: data.logradouro,
        estado: data.uf,
        cidade: data.localidade,
      }));
    } catch {
      setError('CEP não encontrado. Confira o número informado.');
    }
  };
  const setCartaoCampo = (campo, transformar) => (event) => {
    setCartao({
      ...cartao,
      [campo]: transformar ? transformar(event.target.value) : event.target.value,
    });
  };
  const soNumeros = (valor) => valor.replace(/\D/g, '');
  const semNumeros = (valor) => valor.replace(/[0-9]/g, '');
  const cartaoExpirado = (validade) => {
    if (!validade) return true;
    const [anoStr, mesStr] = validade.split('-');
    if (!anoStr || anoStr.length !== 4) return true;
    const ano = Number(anoStr);
    const mes = Number(mesStr);
    const agora = new Date();
    if (ano > agora.getFullYear() + 15) return true;
    return ano < agora.getFullYear() || (ano === agora.getFullYear() && mes < agora.getMonth() + 1);
  };
  const mesAtual = new Date().toISOString().slice(0, 7);
  const mesMaximo = `${new Date().getFullYear() + 15}-12`;
  const submit = async (event) => {
    event.preventDefault();
    if (!items.length) return navigate('/carrinho');
    if (
      (metodoPagamento === 'CARTAO_CREDITO' || metodoPagamento === 'CARTAO_DEBITO') &&
      cartaoExpirado(cartao.validade)
    ) {
      return setError('Cartão vencido. Confira a validade informada.');
    }
    setSaving(true);
    setError('');
    try {
      await request('/pedido/cadastro', {
        method: 'POST',
        body: JSON.stringify({
          id_produtos: items.map((item) => item.id),
          id_comprador: Number(auth.userId),
          endereco: { ...address, pais: 'Brasil' },
          valor: totalComFrete,
          tipoEntrega,
          metodoPagamento,
          ...(metodoPagamento !== 'PIX' && { cartao }),
        }),
      });
      localStorage.removeItem(cartKey());
      window.dispatchEvent(new Event('cart-updated'));
      notice({ message: 'Pedido realizado com sucesso.' });
      navigate('/historico/compras');
    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };
  return (
    <Page eyebrow="FINALIZAR PEDIDO" title="Para onde enviamos?">
      {!items.length ? (
        <Empty
          title="Não há itens para finalizar"
          text="Adicione produtos ao carrinho antes de continuar."
        />
      ) : (
        <form className="form-card checkout-form" onSubmit={submit}>
          {error && <ErrorMessage error={error} />}
          {/* Endereço */}
          <label>
            CEP
            <input
              required
              value={address.cep || ''}
              onChange={(event) => setAddress({ ...address, cep: event.target.value })}
              onBlur={(event) => lookupCep(event.target.value)}
              placeholder="00000-000"
            />
          </label>
          <label>
            Logradouro
            <input
              required
              value={address.logradouro || ''}
              onChange={(event) => setAddress({ ...address, logradouro: event.target.value })}
            />
          </label>
          <div className="form-row">
            <label>
              Estado
              <input
                required
                value={address.estado || ''}
                onChange={(event) => setAddress({ ...address, estado: event.target.value })}
              />
            </label>
            <label>
              Cidade
              <input
                required
                value={address.cidade || ''}
                onChange={(event) => setAddress({ ...address, cidade: event.target.value })}
              />
            </label>
          </div>
          <label>
            Entrega
            <div className="delivery-options">
              {opcoesEntrega.map((opcao) => (
                <label className="delivery-option" key={opcao.tipo}>
                  <input
                    type="radio"
                    name="tipoEntrega"
                    checked={tipoEntrega === opcao.tipo}
                    onChange={() => setTipoEntrega(opcao.tipo)}
                  />
                  <span>
                    {opcao.descricao} — {opcao.valorFrete ? money(opcao.valorFrete) : 'Grátis'} ·{' '}
                    {opcao.prazoDias} dia(s)
                  </span>
                </label>
              ))}
            </div>
          </label>
          <label>
            Forma de pagamento
            <select
              required
              value={metodoPagamento}
              onChange={(event) => {
                setMetodoPagamento(event.target.value);
                setCartao({ numero: '', nome: '', validade: '', cvv: '' });
              }}
            >
              <option value="" disabled>
                Selecione
              </option>
              <option value="CARTAO_CREDITO">Cartão de crédito</option>
              <option value="CARTAO_DEBITO">Cartão de débito</option>
              <option value="PIX">Pix</option>
            </select>
          </label>
          {(metodoPagamento === 'CARTAO_CREDITO' || metodoPagamento === 'CARTAO_DEBITO') && (
            <>
              <label>
                Número do cartão
                <input
                  required
                  inputMode="numeric"
                  value={cartao.numero}
                  onChange={setCartaoCampo('numero', soNumeros)}
                  placeholder="0000000000000000"
                  maxLength={19}
                />
              </label>
              <label>
                Nome impresso no cartão
                <input
                  required
                  value={cartao.nome}
                  onChange={setCartaoCampo('nome', semNumeros)}
                />
              </label>
              <div className="form-row">
                <label>
                  Validade
                  <input
                    required
                    type="month"
                    min={mesAtual}
                    max={mesMaximo}
                    value={cartao.validade}
                    onChange={setCartaoCampo('validade')}
                  />
                </label>
                <label>
                  Código de segurança
                  <input
                    required
                    inputMode="numeric"
                    value={cartao.cvv}
                    onChange={setCartaoCampo('cvv', soNumeros)}
                    placeholder="CVV"
                    maxLength={4}
                  />
                </label>
              </div>
            </>
          )}
          {metodoPagamento === 'PIX' && (
            <div className="empty pix-box">
              <a href={PIX_DESTINO} target="_blank" rel="noreferrer">
                <img src={PIX_QR} alt="QR Code do Pix" width={190} height={190} />
              </a>
              <h2>QR Code</h2>
              <small>Escaneie o código para concluir o pagamento via Pix.</small>
            </div>
          )}
          <div className="checkout-total">
            Frete <strong>{money(frete)}</strong>
          </div>
          <div className="checkout-total">
            Total do pedido <strong>{money(totalComFrete)}</strong>
          </div>
          <button className="button primary" disabled={saving}>
            {saving ? 'Finalizando…' : 'Confirmar pedido'}
          </button>
        </form>
      )}
    </Page>
  );
}
