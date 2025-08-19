package tests;

import common.PlayerControllerApi;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class PlayerControllerTests {
    private PlayerControllerApi playerApi;

    @BeforeClass
    public void setup(){
        playerApi = new PlayerControllerApi();
    }

    @Test
    public void testCreatePlayer(){
        Map<String, Object> playerData = Map.of(
                "age", 22,
                "login", "Rumba",
                "screenName", "Rumba43",
                "gender", "male",
                "role", "user",
                "password", "password123"
        );
        Response createResponse = new PlayerControllerApi().createPlayer("supervisor", playerData);
        int playerId = createResponse.jsonPath().getInt("id");

        Response getPlayerResponse = new PlayerControllerApi().getPlayerByPlayerId(playerId);

        Response deleteResponse = new PlayerControllerApi().deletePlayer("supervisor", playerId);
        deleteResponse.then().statusCode(204);
    }

    @Test void testUpdatePlayer(){
        Map<String, Object> playerData = Map.of(
                "age", 27,
                "login", "Rumba",
                "screenName", "Rumba43",
                "gender", "male",
                "role", "user",
                "password", "password123"
        );
        Response createResponse = new PlayerControllerApi().createPlayer("supervisor", playerData);
        int playerId = createResponse.jsonPath().getInt("id");

        Response getPlayerResponse = new PlayerControllerApi().getPlayerByPlayerId(playerId);

        Map<String, Object> updatedPlayerData = Map.of(
                "age", 25,
                "gender", "male",
                "login", "JackieChan",
                "password", "password123",
                "role", "user",
                "screenName", "Rumba29"
                );
        Response updatePlayerResponse = new PlayerControllerApi().updatePlayer("supervisor",updatedPlayerData, playerId);
        Response getUpdatedPlayerResponse = new PlayerControllerApi().getPlayerByPlayerId(playerId);

        Response deleteResponse = new PlayerControllerApi().deletePlayer("supervisor", playerId);
        deleteResponse.then().statusCode(204);
    }

    @Test
    public void testGetAllPlayers(){
        Response response = new PlayerControllerApi().getAllPlayersNew();
    }
}
