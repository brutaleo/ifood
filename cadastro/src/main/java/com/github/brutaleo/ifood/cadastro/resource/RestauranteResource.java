package com.github.brutaleo.ifood.cadastro.resource;

import com.github.brutaleo.ifood.cadastro.dto.AdicionarPratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.AdicionarRestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.AtualizarPratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.AtualizarRestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.PratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.PratoMapper;
import com.github.brutaleo.ifood.cadastro.dto.RestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.RestauranteMapper;
import com.github.brutaleo.ifood.cadastro.model.Prato;
import com.github.brutaleo.ifood.cadastro.model.Restaurante;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Tag(name = "Restaurantes") //organização da documentação
@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;
    @Inject
    PratoMapper pratoMapper;

    @GET
    public List<RestauranteDTO> listarRestaurante() {
        Stream<Restaurante> restaurantes = Restaurante.streamAll();
        return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response adicionarRestaurante(AdicionarRestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizarRestaurante(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Restaurante restaurante = restauranteOptional.get();

        restauranteMapper.toRestaurante(dto, restaurante);
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
    public List<PratoDTO> listarPrato(@PathParam("restaurante_id") Long restaurante_id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Stream<Prato> pratos = Prato.stream("restaurante", restauranteOptional.get());
        return pratos.map(p -> pratoMapper.toDTO(p)).collect(Collectors.toList());
    }

    @POST
    @Path("{restaurante_id}/pratos")
    @Tag(name = "Pratos")
    @Transactional
    public Response adicionarPrato(@PathParam("restaurante_id") Long restaurante_id, AdicionarPratoDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }
        Prato prato = pratoMapper.toPrato(dto);
        prato.restaurante = restauranteOptional.get();
        prato.persist();
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{restaurante_id}/pratos/{id}")
    @Tag(name = "Pratos")
    @Transactional
    public void atualizarPrato(@PathParam("restaurante_id") Long restaurante_id, @PathParam("id") Long id, AtualizarPratoDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado.");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato não encontrado.");
        }

        Prato prato = pratoOptional.get();
        pratoMapper.toPrato(dto, prato);
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

