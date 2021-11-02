package com.github.brutaleo.ifood.marketplace.resource;

import com.github.brutaleo.ifood.marketplace.dto.CarrinhoDTO;
import com.github.brutaleo.ifood.marketplace.dto.CarrinhoMapper;
import com.github.brutaleo.ifood.marketplace.dto.PratoMapper;
import com.github.brutaleo.ifood.marketplace.model.Carrinho;
import com.github.brutaleo.ifood.marketplace.model.Prato;
import com.github.brutaleo.ifood.marketplace.service.CarrinhoService;
import com.github.brutaleo.ifood.marketplace.service.PratoService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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
import javax.ws.rs.core.Response;

@Path("/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CarrinhoResource {
    private final String CLIENTE = "Teste";

    @Inject
    CarrinhoService carrinhoService;
    @Inject
    CarrinhoMapper carrinhoMapper;
    @Inject
    PratoMapper pratoMapper;

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
    public Uni<Response> listarCarrinhosPorCliente(@PathParam("cliente") String cliente) {
        return carrinhoService
                .findByCliente(cliente)
                .map(carrinhoPorCliente ->
                        Response.ok(carrinhoPorCliente).build()
                );
    }

    @POST
    @Path("{prato_id}/pratos")
    @Transactional
    public Uni<Response> adicionaPratonoCarrinho(@PathParam("prato_id") Long prato_id, CarrinhoDTO dto) {

        return null;
    }
}
