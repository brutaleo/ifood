package com.github.brutaleo.ifood.cadastro.resource;

import com.github.brutaleo.ifood.cadastro.dto.AdicionarPratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.AdicionarRestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.AtualizarPratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.AtualizarRestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.PratoDTO;
import com.github.brutaleo.ifood.cadastro.dto.PratoMapper;
import com.github.brutaleo.ifood.cadastro.dto.RestauranteDTO;
import com.github.brutaleo.ifood.cadastro.dto.RestauranteMapper;
import com.github.brutaleo.ifood.cadastro.infra.ConstraintViolationResponse;
import com.github.brutaleo.ifood.cadastro.model.Prato;
import com.github.brutaleo.ifood.cadastro.model.Restaurante;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import javax.validation.Valid;
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


@Tag(name = "Restaurantes") //organiza????o da documenta????o
@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("proprietario")
@SecurityScheme(
        securitySchemeName = "ifood-oauth",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")
        )
)
@SecurityRequirement(name = "ifood-oauth", scopes = {})//pode ser utilizado a n??vel de m??todo tamb??m.
public class RestauranteResource {

    @Inject
    RestauranteMapper restauranteMapper;
    @Inject
    PratoMapper pratoMapper;
    @Inject
    @Channel("restaurantes-out")
    Emitter<String> emitter;

    @GET
    @Counted(name = "Quantidade buscas Restaurantes", description = "Quantos m??todos get est??o sendo acionados.")
    @SimplyTimed(name = "Tempo medio de execu????es", unit = MetricUnits.MILLISECONDS)
    @Timed(name = "Tempo completo de busca", description = "Quanto tempo leva para realizar uma tarefa.", unit = MetricUnits.MILLISECONDS)
    public List<RestauranteDTO> listarRestaurante() {
        Stream<Restaurante> restaurantes = Restaurante.streamAll();
        return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
    }

    @POST
    @Transactional
    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
    @APIResponse(responseCode = "201", description = "Caso o Restaurante seja cadastrado com sucesso.")
    public Response adicionarRestaurante(@Valid AdicionarRestauranteDTO dto) {
        Restaurante restaurante = restauranteMapper.toRestaurante(dto);
        restaurante.persist();

        Jsonb create = JsonbBuilder.create();
        String json = create.toJson(restaurante);

        emitter.send(json);

        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizarRestaurante(@PathParam("id") Long id, AtualizarRestauranteDTO dto) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante n??o encontrado.");
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
                    throw new NotFoundException("Restaurante n??o encontrado.");
                });
    }

    //Endpoints de Prato
    @GET
    @Path("{restaurante_id}/pratos")
    @Tag(name = "Pratos")
    public List<PratoDTO> listarPrato(@PathParam("restaurante_id") Long restaurante_id) {
        Optional<Restaurante> restauranteOptional = Restaurante.findByIdOptional(restaurante_id);
        if (restauranteOptional.isEmpty()) {
            throw new NotFoundException("Restaurante n??o encontrado.");
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
            throw new NotFoundException("Restaurante n??o encontrado.");
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
            throw new NotFoundException("Restaurante n??o encontrado.");
        }

        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);
        if (pratoOptional.isEmpty()) {
            throw new NotFoundException("Prato n??o encontrado.");
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
            throw new NotFoundException("Restaurante n??o encontrado.");
        }
        Optional<Prato> pratoOptional = Prato.findByIdOptional(id);

        pratoOptional.ifPresentOrElse(
                Prato::delete, () -> {
                    throw new NotFoundException("Prato n??o encontrado.");
                });
    }

}

