package com.github.brutaleo.ifood.cadastro.resource;

import com.github.brutaleo.ifood.cadastro.model.Prato;
import com.github.brutaleo.ifood.cadastro.model.Restaurante;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Optional;

@Tag(name = "Restaurantes")
@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @GET
    public List<Restaurante> listarRestaurante() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionarRestaurante(Restaurante dto) {
        dto.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizarRestaurante(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Restaurante restaurante = restauranteOptional.get();

        restaurante.nome = dto.nome; //cada campo Optional que será persistido deverá ser "bindado" com o dto
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletarRestaurante(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);

        restauranteOptional.ifPresentOrElse(
                Restaurante::delete, () -> {
                    throw new NotFoundException("Restaurante não encontrado.");
                });
    }

    //Endpoints de Prato
    @GET
    @Path("{restaurante_id}/pratos")
    @Tag(name = "Pratos")
    public List<Restaurante> listarPrato(@PathParam("restaurante_id") Long restaurante_id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        return Prato.list("restaurante", restauranteOptional.get());
    }

    @POST
    @Path("{restaurante_id}/pratos")
    @Tag(name = "Pratos")
    @Transactional
    public Response adicionarPrato(@PathParam("restaurante_id") Long restaurante_id, Prato dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Prato prato = new Prato();

        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;
        prato.restaurante = restauranteOptional.get();

        prato.persist();

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{restaurante_id}/pratos/{id}")
    @Tag(name = "Pratos")
    @Transactional
    public void atualizarPrato(@PathParam("restaurante_id") Long restaurante_id, @PathParam("id") Long id, Prato dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não encontrado.");
        }

        Prato prato = pratoOptional.get();
        prato.preco = dto.preco;
        prato.persist();
    }

    @DELETE
    @Path("{restaurante_id}/pratos/{id}")
    @Tag(name = "Pratos")
    @Transactional
    public void deletarPrato(@PathParam("restaurante_id") Long restaurante_id, @PathParam("id") Long id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);

        pratoOptional.ifPresentOrElse(
                Prato::delete, () -> {
                    throw new NotFoundException("Prato não encontrado.");
                });
    }

}

