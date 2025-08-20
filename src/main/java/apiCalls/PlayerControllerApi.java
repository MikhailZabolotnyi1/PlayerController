package apiCalls;

import config.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import models.PlayerDto;


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

    public Response getAllPlayers() {
        return extractResponse(
                baseRequest()
                        .when()
                        .get("/player/get/all")
                        .then()
        );
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

    public Response createPlayer(String editor, PlayerDto player) {
        return extractResponse(
                baseRequest()
                        .queryParams(Map.of(
                                "age", player.age,
                                "login", player.login,
                                "screenName", player.screenName,
                                "gender", player.gender,
                                "role", player.role,
                                "password", player.password
                        ))
                        .when()
                        .get("/player/create/" + editor)
                        .then()
        );
    }

    public Response getPlayerById(int playerId) {
        return extractResponse(
                baseRequest()
                        .contentType("application/json")
                        .body(Map.of("playerId", playerId))
                        .when()
                        .post("/player/get")
                        .then()
        );
    }

    public Response updatePlayer(String editor, PlayerDto player, int playerId) {
        return extractResponse(
                baseRequest()
                        .contentType("application/json")
                        .body(player)
                        .when()
                        .patch("/player/update/" + editor + "/" + playerId)
                        .then()
        );
    }
}
