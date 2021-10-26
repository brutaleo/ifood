package com.github.brutaleo.ifood.marketplace.resource;

import com.github.brutaleo.ifood.marketplace.dto.PratoDTO;
import com.github.brutaleo.ifood.marketplace.service.PratoService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PratoResouce {

    private final PratoService pratoService;

    public PratoResouce(PratoService pratoService) {
        this.pratoService = pratoService;

    }

    @GET
    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PratoDTO.class)))
    public Uni<Response> listarPrato() {
        return pratoService.findAll().map(prato -> Response.ok(prato).build());
    }

    @APIResponse(responseCode = "200", content = @Content(schema = @Schema(type = SchemaType.ARRAY, implementation = PratoDTO.class)))
    @GET
    @Path("{restaurante_id}")
    public Uni<Response> listarPratosPorRestaurante(@PathParam("restaurante_id") Long restaurante_id) {
        return pratoService
                .findByRestaurante(restaurante_id)
                .map(pratoPorRestaurante ->
                        Response.ok(pratoPorRestaurante).build()
                );
    }
}
