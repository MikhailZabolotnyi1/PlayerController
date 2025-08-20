package tests;

import common.PlayerControllerApi;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.testng.Assert.assertEquals;


public class NegativePlayerControllerTests {
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
        Random rand = new Random();
        int bigRandomNumber = 100000 + rand.nextInt(1000000);
        return userName + bigRandomNumber;
    }

    @Test
    public void testUpdatePlayerNegative() {
        int playerId = createPlayer(new PlayerDto(
                22, "verycoolmax", "max", "male", "user", "password123"));

        PlayerDto updatedPlayer = new PlayerDto(15, "JackieChan", "Rumba29", "male", "user", "password123");
        Response updateResponse = playerApi.updatePlayer("supervisor", updatedPlayer, playerId);

        assertEquals(updateResponse.statusCode(), 403, "Expected status 403 when trying to set invalid age");

        Response getUpdatedResponse = playerApi.getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);
        PlayerDto actualPlayer = getUpdatedResponse.as(PlayerDto.class);

        assertEquals(actualPlayer.age, 22, "Age should not have changed after invalid update");
        assertEquals(actualPlayer.login, "verycoolmax", "Login should not have changed after invalid update");
        assertEquals(actualPlayer.screenName, "Max", "ScreenName should not have changed after invalid update");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidAge() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("First");

        int playerId = createPlayer(new PlayerDto(
                14, "Rumba", "Rumba43", "male", "user", "password123"));

        Response getResponse = playerApi.getPlayerById(playerId);
        assertEquals(getResponse.statusCode(), 400, "Expected 403 for underage");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyLogin() {
        int playerId = createPlayer(new PlayerDto(
                20, "", "NoLogin", "male", "user", "password123"));

        Response response = playerApi.getPlayerById(playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for empty login");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidRole() {
        int playerId = createPlayer(new PlayerDto(
                20, "User123", "NoLogin", "male", "worker", "password123"));

        Response response = playerApi.getPlayerById(playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for invalid role");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyPassword() {
        int playerId = createPlayer(new PlayerDto(
                20, "Mucha", "Mucha33", "male", "user", ""));

        Response response = playerApi.getPlayerById(playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for empty password");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidPassword() {
        int playerId = createPlayer(new PlayerDto(
                20, "Mucha", "Mucha33", "male", "user", "22"));

        Response response = playerApi.getPlayerById(playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for invalid password");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidGender() {
        int playerId = createPlayer(new PlayerDto(
                20, "Mucha", "Mucha33", "Helicopter", "user", "22"));

        Response response = playerApi.getPlayerById(playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for invalid gender");
    }

    @Test
    public void testUpdatePlayerNegativeWithEmptyLogin() {
        int playerId = createPlayer(new PlayerDto(
                25, "Rumba", "Rumba4", "male", "user", "password123"));

        PlayerDto updatedPlayer = new PlayerDto(
                25, "", "Rumba4", "male", "user", "password123");

        Response response = playerApi.updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for empty login update");
    }

    @Test
    public void testUpdatePlayerNegativeWithInvalidRole() {
        int playerId = createPlayer(new PlayerDto(
                25, "validUser3", "Max", "male", "user", "password123"
        ));

        PlayerDto updatedPlayer = new PlayerDto(
                25, "Max", "verycoolmax", "male", "worker", "password123"
        );

        Response response = playerApi.updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Expected 400 for invalid role update");
    }

    @Test
    public void testGetAllPlayers() throws InterruptedException {
        Thread.sleep(3000);
        System.out.println("Last");

        getRandomUserName("Jamm");

        Response response = new PlayerControllerApi().getAllPlayers();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (playerId != -1) {
            Response deleteResponse = playerApi.deletePlayer("supervisor", playerId);
            deleteResponse.then().statusCode(204);
        }
    }
}
