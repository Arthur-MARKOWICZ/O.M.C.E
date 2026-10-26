export const CATEGORIAS = ['ESP32', 'ARDUINO', 'REGISTORES', 'SENSORES', 'BATERIA', 'CABOS', 'MOTORES', 'CONECTORES', 'OUTRO'];
export const FILTROS_VAZIOS = { nome: '', categoria: '', precoMin: '', precoMax: '' };

export const temFiltro = (filters) => Object.values(filters).some(Boolean);

export const montarConsulta = (page, filters) => {
  const query = new URLSearchParams({ page });
  Object.entries(filters).forEach(([key, value]) => value && query.set(key, value));
  return query;
};

export default function FilterBar({ filters, onSubmit }) {
  const submit = (event) => {
    event.preventDefault();
    onSubmit(Object.fromEntries(new FormData(event.currentTarget)));
  };

  return (
    <form className="filters" onSubmit={submit}>
      <input name="nome" placeholder="Busque por nome" defaultValue={filters.nome} />
      <select name="categoria" defaultValue={filters.categoria}>
        <option value="">Todas as categorias</option>
        {CATEGORIAS.map((item) => <option key={item}>{item}</option>)}
      </select>
      <input name="precoMin" type="number" min="0" step="0.01" placeholder="Preço mínimo" defaultValue={filters.precoMin} />
      <input name="precoMax" type="number" min="0" step="0.01" placeholder="Preço máximo" defaultValue={filters.precoMax} />
      <button className="button primary">Filtrar</button>
    </form>
  );
}