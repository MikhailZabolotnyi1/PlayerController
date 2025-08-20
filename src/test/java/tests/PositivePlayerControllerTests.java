package tests;

import common.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Listeners({AllureTestNg.class})
public class PositivePlayerControllerTests extends BaseTest {

    @Test
    public void testUpdatePlayer() {
        Allure.step("Starting test: testUpdatePlayer");

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");

        int playerId = createPlayer(new PlayerDto(
                22, login, screenName, "male", "user", "password123"));

        Allure.step("Updating player with new age");
        PlayerDto updatedPlayer = new PlayerDto(18, login, screenName, "male", "user", "password123");
        Response updateResponse = updatePlayer("supervisor", updatedPlayer, playerId);
        updateResponse.then().statusCode(200);

        Allure.step("Getting updated player by ID");
        Response getUpdatedResponse = getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);
        PlayerDto getResponse = getUpdatedResponse.as(PlayerDto.class);

        Allure.step("Validating updated player details");
        assertEquals(getResponse.login, login, "Login does not match");
        assertEquals(getResponse.screenName, screenName, "ScreenName does not match");
        assertEquals(getResponse.age, 18, "Age does not match");
        assertEquals(getResponse.gender, "male", "Gender does not match");
        assertEquals(getResponse.role, "user", "Role does not match");
        Allure.step("Test testUpdatePlayer finished successfully");
    }

    @Test
    public void testCreateUser() {
        Allure.step("Starting test: testCreateUserPositive");

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");

        int playerId = createPlayer(new PlayerDto(
                20, login, screenName, "male", "user", "password123"));

        Allure.step("Fetching player by ID");
        Response getResponse = getPlayerById(playerId);
        getResponse.then().statusCode(200);

        PlayerDto createdPlayer = getResponse.as(PlayerDto.class);

        Allure.step("Validating created player details");
        assertEquals(createdPlayer.login, login, "Login does not match");
        assertEquals(createdPlayer.screenName, screenName, "ScreenName does not match");
        assertEquals(createdPlayer.age, 20, "Age does not match");
        assertEquals(createdPlayer.gender, "male", "Gender does not match");
        assertEquals(createdPlayer.role, "user", "Role does not match");
        Allure.step("Test testCreateUser finished successfully");
    }

    @Test
    public void testGetPlayerById() {
        Allure.step("Starting test: testGetPlayerById");

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");
        int playerId = createPlayer(new PlayerDto(25, login, screenName, "male", "user", "password123"));

        Allure.step("Fetching player by ID: " + playerId);
        Response response = getPlayerById(playerId);
        response.then().statusCode(200);

        PlayerDto fetchedPlayer = response.as(PlayerDto.class);

        Allure.step("Validating fetched player details");
        assertEquals(fetchedPlayer.login, login, "Login does not match");
        assertEquals(fetchedPlayer.screenName, screenName, "ScreenName does not match");
        assertEquals(fetchedPlayer.age, 25, "Age does not match");
        assertEquals(fetchedPlayer.gender, "male", "Gender does not match");
        assertEquals(fetchedPlayer.role, "user", "Role does not match");

        Allure.step("Test testGetPlayerById finished successfully");
    }

    @Test
    public void testGetAllPlayers(){

        Allure.step("Fetching all players from API");
        Response response = getAllPlayers();
        assertEquals(response.statusCode(), 200, "Expected status code 200");

        int playersCount = response.jsonPath().getList("players").size();
        Allure.step("Total players fetched: " + playersCount);

        assertEquals(playersCount >= 0, true, "Expected non-negative number of players");
    }

    @Test //expected to fail
    public void testDeletePlayer() {
        Allure.step("Starting test: testDeletePlayer");

        String login = getRandomUserName("User");
        String screenName = getRandomUserName("screenNameUser");
        int currentPlayerId = getPlayerIdFromResponse(createPlayerWithResponse(new PlayerDto(25, login, screenName, "male", "user", "password123"), false));

        deletePlayer(currentPlayerId);

        Allure.step("Validating player is deleted");
        Response getResponse = getPlayerById(currentPlayerId);
        assertEquals(getResponse.statusCode(), 404, "Expected 404 after deletion");

        Allure.step("Test testDeletePlayer finished successfully");
    }

}
