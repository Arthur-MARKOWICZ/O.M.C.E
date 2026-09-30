import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { auth, request } from '../../api';
import { useNotice } from '../../contexts/NoticeContext';
import { ErrorMessage, Loading } from '../../components/ui/Feedback';
import Page from '../../components/ui/Page';
import fileToData from '../../utils/fileToData';

export default function ProductForm({ edit = false }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const notice = useNotice();

  const [current, setCurrent] = useState(null);
  const [file, setFile] = useState(null);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  // NOVO: controla a categoria selecionada
  const [categoria, setCategoria] = useState('');

  useEffect(() => {
    if (edit) {
      request(`/produto/visualizarDetalhesProduto/${id}`)
          .then((product) => {
            setCurrent(product);
            setCategoria(product.categoria || '');
          })
          .catch((requestError) => setError(requestError.message));
    }
  }, [edit, id]);

  const submit = async (event) => {
    event.preventDefault();

    const values = Object.fromEntries(
        new FormData(event.currentTarget)
    );

    if (!edit && !file) {
      return setError('Escolha uma imagem para anunciar o produto.');
    }

    setSaving(true);
    setError('');

    try {
      const base64 = file ? await fileToData(file) : null;

      const product = {
        id: current?.id,

        nome: values.nome,
        preco: Number(values.preco),
        detalhes: values.detalhes,
        condicao: values.condicao,
        categoria: values.categoria,

        id_usuario: Number(auth.userId),

        imagem:
            base64?.data ||
            current?.imagem ||
            current?.Imagem,

        imagem_tipo:
            base64?.type ||
            current?.imagem_tipo ||
            current?.Imagem_tipo,

        // CAMPOS ESPECÍFICOS
        modelo: values.modelo || null,
        voltagem: values.voltagem || null,
        carga: values.carga || null,
        comprimento: values.comprimento || null,
        tipo: values.tipo || null
      };

      await request(
          edit
              ? '/produto/alterarDadosProduto'
              : '/produto/cadastroProduto',
          {
            method: edit ? 'PUT' : 'POST',
            body: JSON.stringify(product)
          }
      );

      notice({
        message: edit
            ? 'Produto atualizado com sucesso.'
            : 'Produto anunciado com sucesso.'
      });

      navigate(edit ? `/produto/${id}` : '/feed');

    } catch (requestError) {
      setError(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  if (edit && !current && !error) {
    return <Loading />;
  }

  return (
      <Page
          eyebrow={edit ? 'GERENCIAR ANÚNCIO' : 'NOVO ANÚNCIO'}
          title={edit ? 'Edite seu produto' : 'Anuncie uma peça'}
      >

        <form
            className="form-card product-form"
            onSubmit={submit}
        >

          {error && <ErrorMessage error={error} />}

          <label>
            Nome do produto

            <input
                name="nome"
                required
                defaultValue={current?.nome}
                placeholder="Ex.: Kit ESP32 DevKit"
            />
          </label>


          <div className="form-row">

            <label>
              Preço

              <input
                  name="preco"
                  type="number"
                  min="0.01"
                  step="0.01"
                  required
                  defaultValue={current?.preco}
                  placeholder="0,00"
              />
            </label>


            <label>
              Condição

              <select
                  name="condicao"
                  required
                  defaultValue={current?.condicao || ''}
              >
                <option value="" disabled>
                  Selecione
                </option>

                <option value="NOVO">
                  Novo
                </option>

                <option value="USADO">
                  Usado
                </option>
              </select>
            </label>


            <label>
              Categoria

              <select
                  name="categoria"
                  required
                  value={categoria}
                  onChange={(event) => setCategoria(event.target.value)}
              >
                <option value="" disabled>
                  Selecione
                </option>

                {[
                  'ESP32',
                  'ARDUINO',
                  'REGISTORES',
                  'SENSORES',
                  'BATERIA',
                  'CABOS',
                  'MOTORES',
                  'CONECTORES',
                  'OUTRO'
                ].map((item) => (
                    <option key={item} value={item}>
                      {item}
                    </option>
                ))}
              </select>
            </label>

          </div>


          {/* ============================= */}
          {/* CAMPOS ESPECÍFICOS DA CATEGORIA */}
          {/* ============================= */}

          {(categoria === 'ESP32' || categoria === 'ARDUINO') && (
              <label>
                Modelo

                <input
                    name="modelo"
                    defaultValue={current?.modelo || ''}
                    placeholder="Ex.: ESP32-WROOM-32"
                />
              </label>
          )}


          {categoria === 'REGISTORES' && (
              <label>
                Voltagem

                <input
                    name="voltagem"
                    defaultValue={current?.voltagem || ''}
                    placeholder="Ex.: 5V"
                />
              </label>
          )}


          {categoria === 'SENSORES' && (
              <label>
                Tipo

                <input
                    name="tipo"
                    defaultValue={current?.tipo || ''}
                    placeholder="Ex.: Temperatura, umidade, presença..."
                />
              </label>
          )}


          {categoria === 'BATERIA' && (
              <label>
                Carga

                <input
                    name="carga"
                    defaultValue={current?.carga || ''}
                    placeholder="Ex.: 5000mAh"
                />
              </label>
          )}


          {categoria === 'CABOS' && (
              <div className="form-row">

                <label>
                  Comprimento

                  <input
                      name="comprimento"
                      defaultValue={current?.comprimento || ''}
                      placeholder="Ex.: 2 metros"
                  />
                </label>

                <label>
                  Tipo

                  <input
                      name="tipo"
                      defaultValue={current?.tipo || ''}
                      placeholder="Ex.: USB-C, HDMI..."
                  />
                </label>

              </div>
          )}


          {categoria === 'MOTORES' && (
              <label>
                Tipo

                <input
                    name="tipo"
                    defaultValue={current?.tipo || ''}
                    placeholder="Ex.: Motor DC, Servo, Passo..."
                />
              </label>
          )}


          {categoria === 'CONECTORES' && (
              <label>
                Tipo

                <input
                    name="tipo"
                    defaultValue={current?.tipo || ''}
                    placeholder="Ex.: P2, USB, borne..."
                />
              </label>
          )}


          <label>
            Detalhes

            <textarea
                name="detalhes"
                required
                defaultValue={current?.detalhes}
                placeholder="Descreva o estado, especificações e o que acompanha o produto."
                rows="5"
            />
          </label>


          <label>
            Imagem

            <input
                type="file"
                accept="image/*"
                onChange={(event) =>
                    setFile(event.target.files?.[0])
                }
            />

            {current && !file && (
                <small>
                  Deixe em branco para manter a imagem atual.
                </small>
            )}
          </label>


          <button
              className="button primary"
              disabled={saving}
          >
            {saving
                ? 'Salvando…'
                : edit
                    ? 'Salvar alterações'
                    : 'Publicar anúncio'}
          </button>

        </form>

      </Page>
  );
}