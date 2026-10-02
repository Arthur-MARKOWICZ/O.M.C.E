import { useState } from 'react';
import { auth, downloadBlob, requestFile } from '../../api';
import { ErrorMessage } from '../ui/Feedback';

const FORMATOS = { CSV: 'csv', XLSX: 'xlsx', PDF: 'pdf' };
const iso = (date) => `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;

export default function ExportBar() {
  const [dataInicio, setDataInicio] = useState(() => { const hoje = new Date(); return iso(new Date(hoje.getFullYear(), hoje.getMonth(), 1)); });
  const [dataFim, setDataFim] = useState(() => iso(new Date()));
  const [exportando, setExportando] = useState('');
  const [error, setError] = useState('');
  const hoje = iso(new Date());
  const dataValida = (valor) => /^\d{4}-\d{2}-\d{2}$/.test(valor || '');

  async function exportar(formato) {
    setError('');
    if (!dataValida(dataInicio) || !dataValida(dataFim)) return setError('Informe um período de data válido.');
    setExportando(formato);
    try {
      const { blob, filename } = await requestFile(`/historico/compra/exportar?formato=${formato}&dataInicio=${dataInicio}&dataFim=${dataFim}`, { headers: { 'Id-Usuario': auth.userId } });
      downloadBlob(blob, filename || `historico-compras_${dataInicio}_a_${dataFim}.${FORMATOS[formato]}`);
    } catch (exportError) {
      setError(exportError.message);
    } finally {
      setExportando('');
    }
  }

  return <>
    <div className="export-bar">
      <label>De<input type="date" value={dataInicio} min="2000-01-01" max={dataFim} onChange={(event) => setDataInicio(event.target.value)} /></label>
      <label>Até<input type="date" value={dataFim} min={dataInicio} max={hoje} onChange={(event) => setDataFim(event.target.value)} /></label>
      <div className="export-actions">{Object.keys(FORMATOS).map((formato) => <button key={formato} type="button" className="button secondary" disabled={Boolean(exportando) || !dataInicio || !dataFim} onClick={() => exportar(formato)}>{exportando === formato ? 'Gerando…' : formato}</button>)}</div>
    </div>
    {error && <ErrorMessage error={error} />}
  </>;
}
