package tests;

import common.PlayerControllerApi;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

@Listeners
public class PositivePlayerControllerTests {
    private PlayerControllerApi playerApi;
    private int playerId;

    @BeforeMethod
    public void setup() throws InterruptedException {
        playerApi = new PlayerControllerApi();
        playerId = -1;

        Thread.sleep(3000);
        System.out.println("Threads check");
    }

    private int createPlayer(PlayerDto player) {
        Response response = playerApi.createPlayer("supervisor", player);
        playerId = response.jsonPath().getInt("id");
        return playerId;
    }

    private void deletePlayer(int playerId) {
        Response response = playerApi.deletePlayer("supervisor", playerId);
        response.then().statusCode(204);
    }

    private String getRandomUserName (String userName) {
       String randomString = UUID.randomUUID().toString();
        return userName + randomString;
    }

    @Test
    public void testUpdatePlayer() {

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");

        int playerId = createPlayer(new PlayerDto(
                22, login, screenName, "male", "user", "password123"));

        PlayerDto updatedPlayer = new PlayerDto(18, login, screenName, "male", "user", "password123");
        Response updateResponse = playerApi.updatePlayer("supervisor", updatedPlayer, playerId);
        updateResponse.then().statusCode(200);

        Response getUpdatedResponse = playerApi.getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);
        PlayerDto getResponse = getUpdatedResponse.as(PlayerDto.class);


        assertEquals(getResponse.login, login, "Login does not match");
        assertEquals(getResponse.screenName, screenName, "ScreenName does not match");
        assertEquals(getResponse.age, 18, "Age does not match");
        assertEquals(getResponse.gender, "male", "Gender does not match");
        assertEquals(getResponse.role, "user", "Role does not match");
    }

    @Test
    public void testCreateUserPositive() {

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");

        int playerId = createPlayer(new PlayerDto(
                20, login, screenName, "male", "user", "password123"));

        Response getResponse = playerApi.getPlayerById(playerId);
        getResponse.then().statusCode(200);

        PlayerDto createdPlayer = getResponse.as(PlayerDto.class);

        assertEquals(createdPlayer.login, login, "Login does not match");
        assertEquals(createdPlayer.screenName, screenName, "ScreenName does not match");
        assertEquals(createdPlayer.age, 20, "Age does not match");
        assertEquals(createdPlayer.gender, "male", "Gender does not match");
        assertEquals(createdPlayer.role, "user", "Role does not match");
    }

    @Test
    public void testGetAllPlayers(){
        new PlayerControllerApi().getAllPlayers();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (playerId != -1) {
            Response deleteResponse = playerApi.deletePlayer("supervisor", playerId);
            deleteResponse.then().statusCode(204);
        }
    }
}
