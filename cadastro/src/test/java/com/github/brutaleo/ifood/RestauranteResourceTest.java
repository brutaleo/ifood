package com.github.brutaleo.ifood;

import com.github.brutaleo.ifood.cadastro.model.Restaurante;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
// previne as checagens do postgres vs as definições no arquivo *.yml
@QuarkusTest
@QuarkusTestResource(CadastroTestLifeCycleManager.class)
public class RestauranteResourceTest {

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testandoSeOEndpointGetRetornaStatusCode200NoCenarioDefinidoNoDataset() {
        String resultado = given()
                .when().get("/restaurantes")
                .then()
                .statusCode(200)
                .extract()
                .asString();
        Approvals.verifyJson(resultado);
    }

    //utilizar o given do RestAssured sem definir o tipo de conteúdo gera um erro.
    private RequestSpecification given() {
        return RestAssured.given().contentType(ContentType.JSON); //Definição do método given() para receber o conteúdo JSON.
    }

    @Test
    @DataSet("restaurantes-cenario-1.yml")
    public void testandoSeOEndpointPutAlteraUmRestauranteNoCenarioDefinidoNoDataset() {
        Restaurante dto = new Restaurante();
        dto.nome = "Novo-Nome";
        Long parameterValue = 123L;
        given()
                .with()
                .pathParam("id", parameterValue)
                .body(dto)
                .when()
                .put("/restaurantes/{id}")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode()) //não possui resposta para exibir
                .extract()
                .asString();

        Restaurante findById = Restaurante.findById(parameterValue); //consulta no banco de dados para verificar se a alteração foi realizada

        Assertions.assertEquals(dto.nome, findById.nome); //assertion que verifica se a alteração
    }

}
