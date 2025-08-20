package tests;

import common.BaseTest;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;


import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;


public class NegativePlayerControllerTests extends BaseTest {

    @Test
    public void testUpdatePlayerNegative() {
        Integer playerId = createPlayer(new PlayerDto(
                22, "verycoolmax", "max", "male", "user", "password123"));

        PlayerDto updatedPlayer = new PlayerDto(15, "JackieChan", "Rumba29", "male", "user", "password123");
        Response updateResponse = updatePlayer("supervisor", updatedPlayer, playerId);

        assertEquals(updateResponse.statusCode(), 403, "Expected status 403 when trying to set invalid age");

        Response getUpdatedResponse = getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);
        PlayerDto actualPlayer = getUpdatedResponse.as(PlayerDto.class);

        assertEquals(actualPlayer.age, 22, "Age should not have changed after invalid update");
        assertEquals(actualPlayer.login, "verycoolmax", "Login should not have changed after invalid update");
        assertEquals(actualPlayer.screenName, "Max", "ScreenName should not have changed after invalid update");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidAge() {

        Integer statusCode = getStatusCodeFromResponse(createPlayerWithResponse(new PlayerDto(
                14, "Rumba", "Rumba43", "male", "user", "password123")));

        assertEquals(statusCode, 400, "Wrong status code for underage");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyLogin() {
        Integer statusCode = createPlayerWithResponse(new PlayerDto(
                20, "", "NoLogin", "male", "user", "password123")).statusCode();

        assertEquals(statusCode, 400, "Wrong status code for empty login");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidRole() {
        Integer statusCode = createPlayerWithResponse(new PlayerDto(
                20, "User123", "NoLogin", "male", "worker", "password123")).statusCode();

        assertEquals(statusCode, 400, "Wrong status code for invalid role");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyPassword() {
        Integer statusCode = createPlayerWithResponse(new PlayerDto(
                20, "Mucha", "Mucha33", "male", "user", "")).statusCode();
        assertEquals(statusCode, 400, "Wrong status code for empty password");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidPassword() {
        Integer playerId = getPlayerIdFromResponse(createPlayerWithResponse(new PlayerDto(
                20, "Mucha", "Mucha33", "male", "user", "22")));

        assertEquals(getStatusCodeFromResponse(getPlayerById(playerId)), 400, "Wrong status code for invalid password");
    }

    @Test //expected to fail
    public void testCreatePlayerNegativeWithInvalidGender() {
        Integer playerId = createPlayer(new PlayerDto(
                20, "Mucha", "Mucha33", "Helicopter", "user", "22"));

        Integer statusCode = getStatusCodeFromResponse(getPlayerById(playerId));
        assertEquals(statusCode, 400, "Wrong status code for invalid gender");
    }

    @Test // expected to fail due to backend bug
    public void testUpdatePlayerNegativeWithEmptyLogin() {
        Integer playerId = createPlayer(new PlayerDto(
                25, "Rumba", "Rumba4", "male", "user", "password123"));

        PlayerDto updatedPlayer = new PlayerDto(
                25, "", "Rumba4", "male", "user", "password123");

        Response response = updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Wrong status code for empty login update");
    }

    @Test // expected to fail due to backend bug
    public void testUpdatePlayerNegativeWithInvalidRole() {
        Integer playerId = createPlayer(new PlayerDto(
                25, "validUser3", "Max", "male", "user", "password123"
        ));

        PlayerDto updatedPlayer = new PlayerDto(
                25, "Max", "verycoolmax", "male", "worker", "password123"
        );

        Response response = updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Wrong status code for invalid role update");
    }
}
