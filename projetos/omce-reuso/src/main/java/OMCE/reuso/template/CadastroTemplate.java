package OMCE.reuso.template;

public abstract class CadastroTemplate<D, U, E> {

    public E cadastrar(D dados) {
        validar(dados);
        U usuario = buscarUsuario(dados);
        E entidade = criar(dados);
        definirUsuario(entidade, usuario);
        configurar(entidade, dados);
        return salvar(entidade);
    }

    protected abstract void validar(D dados);

    protected abstract U buscarUsuario(D dados);

    protected abstract E criar(D dados);

    protected abstract void definirUsuario(E entidade, U usuario);



    protected abstract void configurar(E entidade, D dados);

    protected abstract E salvar(E entidade);

}
