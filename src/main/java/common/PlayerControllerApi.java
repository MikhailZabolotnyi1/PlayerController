package common;

import config.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import java.util.Map;

public class PlayerControllerApi {

    public PlayerControllerApi() {
        RestAssured.baseURI = Config.getBaseURL();
    }

    private RequestSpecification baseRequest() {
        return RestAssured
                .given()
                .log().uri()
                .log().method()
                .log().body();
    }

    private Response extractResponse(ValidatableResponse validatable) {
        return validatable
                .log().status()
                .log().body()
                .extract()
                .response();
    }

    public Response getAllPlayersNew() {
        return baseRequest()
                .when()
                .get("/player/get/all")
                .then()
                .log().status()
                .log().body()
                .extract().response();
    }

    public Response deletePlayer(String editor, int playerId) {
        return extractResponse(
                baseRequest()
                        .contentType("application/json")
                        .body(Map.of("playerId", playerId))
                        .when()
                        .delete("/player/delete/" + editor)
                        .then()
        );
    }

    public Response createPlayer(String editor, Map<String, Object> params) {
        return extractResponse(
                baseRequest()
                        .queryParams(params)
                        .when()
                        .get("/player/create/" + editor)
                        .then()
        );
    }

    public Response getPlayerByPlayerId(int playerId) {
        return extractResponse(
                baseRequest()
                        .contentType("application/json")
                        .body(Map.of("playerId", playerId))
                        .when()
                        .post("/player/get")
                        .then()
        );

    }

    public Response updatePlayer(String editor, Map<String, Object> params, int playerId) {
        return extractResponse(
                baseRequest()
                        .contentType("application/json")
                        .body(params)
                        .when()
                        .patch("/player/update/" + editor + "/" + playerId)
                        .then()
        );
    }
}
