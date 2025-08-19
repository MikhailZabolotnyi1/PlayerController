package tests;

import common.PlayerControllerApi;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class PlayerControllerTests {
    private PlayerControllerApi playerApi;

    @BeforeMethod
    public void setup(){
        playerApi = new PlayerControllerApi();
    }

    private int createPlayer(PlayerDto player) {
        Response response = playerApi.createPlayer("supervisor", player);
        return response.jsonPath().getInt("id");
    }

    private void deletePlayer(int playerId) {
        Response response = playerApi.deletePlayer("supervisor", playerId);
        response.then().statusCode(204);
    }

    @Test
    public void testCreatePlayer() throws InterruptedException {

        Thread.sleep(3000);
        System.out.println("First");

        PlayerDto player = new PlayerDto(22, "Rumba", "Rumba43", "male", "user", "password123");
        int playerId = createPlayer(player);

        Response getResponse = playerApi.getPlayerById(playerId);
        getResponse.then().statusCode(200);

        deletePlayer(playerId);
    }

    @Test
    public void testUpdatePlayer() throws InterruptedException {

        Thread.sleep(3000);
        System.out.println("Second");

        PlayerDto player = new PlayerDto(22, "verycoolmax", "Max", "male", "user", "password123");
        int playerId = createPlayer(player);

        PlayerDto updatedPlayer = new PlayerDto(25, "JackieChan", "Rumba29", "male", "user", "password123");
        Response updateResponse = playerApi.updatePlayer("supervisor", updatedPlayer, playerId);
        updateResponse.then().statusCode(200);

        Response getUpdatedResponse = playerApi.getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);

        deletePlayer(playerId);
    }

    @Test
    public void testGetAllPlayers() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Last");

        Response response = new PlayerControllerApi().getAllPlayers();
    }
}
