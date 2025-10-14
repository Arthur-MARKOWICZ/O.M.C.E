package OMCE.OMCE.NotificacaoEmail;

import OMCE.OMCE.Pedido.Pedido;
import OMCE.OMCE.Pedido.repository.ItemPedidoRepository;
import OMCE.OMCE.Produto.Produto;
import OMCE.OMCE.User.Service.UserService;
import OMCE.OMCE.User.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailCompra implements Compra {

    private final JavaMailSender mailSender;
    private final ItemPedidoRepository itemPedidoRepository;
    private final UserService userService;

    public EmailCompra(JavaMailSender mailSender, ItemPedidoRepository itemPedidoRepository, UserService userService) {
        this.mailSender = mailSender;
        this.itemPedidoRepository = itemPedidoRepository;
        this.userService = userService;
    }

    @Override
    public void atualizar(Pedido pedido) {
        List<Produto> produtos = itemPedidoRepository.findProdutosByPedidoId(pedido.getId());
        User comprador = userService.pegarUserPorId(pedido.getCompradorId());
        for (Produto produto : produtos) {
            User vendedor = produto.getUsuario();
            enviarEmail(
                    comprador.getEmail(),
                    "Compra realizada com sucesso!",
                    "Você comprou o produto: " + produto.getNome() +
                            "\nValor: R$" + produto.getPreco() +
                            "\nVendedor: " + vendedor.getNome()
            );
            enviarEmail(
                    vendedor.getEmail(),
                    "Seu produto foi vendido!",
                    "O produto '" + produto.getNome() + "' foi vendido para " + comprador.getNome() +
                            ".\nValor: R$" + produto.getPreco()
            );
        }
    }
    private void enviarEmail(String destinatario, String assunto, String corpo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(destinatario);
        mensagem.setSubject(assunto);
        mensagem.setText(corpo);
        mailSender.send(mensagem);
    }
}
