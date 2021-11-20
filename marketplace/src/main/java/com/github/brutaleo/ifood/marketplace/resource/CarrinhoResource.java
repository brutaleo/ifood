package com.github.brutaleo.ifood.marketplace.resource;

import com.github.brutaleo.ifood.marketplace.dto.CarrinhoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PedidoRealizadoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.dto.PratoPedidoDTO;
import com.github.brutaleo.ifood.marketplace.dto.RestauranteDTO;
import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import com.github.brutaleo.ifood.marketplace.service.CarrinhoService;
import com.github.brutaleo.ifood.marketplace.service.PratoService;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/carrinhos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CarrinhoResource {
    private final String CLIENTE = "Teste";

    @Inject
    CarrinhoService carrinhoService;
    @Inject
    PratoService pratoService;
    @Inject
    PgPool client;
    @Inject
    @Channel("pedidos")
    Emitter<PedidoRealizadoDTO> emitterPedido;

    @APIResponse(
            responseCode = "200",
            content = @Content(
                    schema = @Schema(
                            type = SchemaType.ARRAY,
                            implementation = CarrinhoDTO.class)
            )
    )
    @GET
    @Path("{cliente}")
    public Uni<List<Carrinho>> listarCarrinhosPorCliente(@PathParam("cliente") String cliente) {
        return carrinhoService
                .buscarCarrinhoPorCliente(client, cliente);
    }

    @POST
    @Path("adiciona/{prato_id}")
    public Uni<String> adicionaPratoAoCarrinho(@PathParam("prato_id") Long prato_id) {

        Carrinho carrinho = new Carrinho();
        carrinho.cliente = CLIENTE;
        carrinho.prato = prato_id;

        return carrinhoService
                .salvarPratoNoCarrinho(
                        client,
                        CLIENTE,
                        prato_id
                );
    }

    @POST
    @Transactional
    @Path("/realizar-pedido")
    public Uni<Boolean> finalizarPedido() {
        PedidoRealizadoDTO pedido = new PedidoRealizadoDTO();
        String cliente  = CLIENTE;
        pedido.cliente = cliente;

        List<Carrinho> carrinho = carrinhoService
                .buscarCarrinhoPorCliente(client, cliente)
                .await()
                .indefinitely();

        List<PratoPedidoDTO>
                pratos = carrinho
                .stream()
                .map(
                        pratoCarrinho -> from(pratoCarrinho))
                .collect(Collectors.toList()
                );

        pedido.pratos = pratos;

        RestauranteDTO restaurante = new RestauranteDTO();
        restaurante.nome = "Nome restaurante";
        pedido.restaurante = restaurante;

        emitterPedido.send(pedido);

        return carrinhoService.deletarCarrinho(client, cliente);
    }

    private PratoPedidoDTO from(Carrinho carrinho) {
        PratoDTO dto = pratoService
                .findById(client, carrinho.prato)
                .await()
                .indefinitely();

        return new PratoPedidoDTO(dto.nome, dto.descricao, dto.preco);
    }

}
