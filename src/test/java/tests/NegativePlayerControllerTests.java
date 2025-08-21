package tests;

import common.BaseTest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;


public class NegativePlayerControllerTests extends BaseTest {

    @Test
    public void testUpdatePlayerNegativeInvalidAge() {
        Allure.step("Starting test: testUpdatePlayerNegativeInvalidAge");

        Allure.step("Creating player with valid data");
        Integer playerId = createPlayer(new PlayerDto(22, "ValidUser", "ValidUserOne", "male", "user", "password123"), "supervisor");

        Allure.step("Trying to update player with invalid age");
        PlayerDto updatedPlayer = new PlayerDto(15, "PatchedUser", "PatchedUserOne", "male", "user", "password123");
        Response updateResponse = updatePlayer("supervisor", updatedPlayer, playerId);

        assertEquals(updateResponse.statusCode(), 400, "Expected status 400 when trying to set invalid age");

        Allure.step("Verifying player data has not been changed");
        Response getUpdatedResponse = getPlayerById(playerId);
        getUpdatedResponse.then().statusCode(200);
        PlayerDto actualPlayer = getUpdatedResponse.as(PlayerDto.class);

        assertEquals(actualPlayer.age, 22, "Age should not have changed after invalid update");
        assertEquals(actualPlayer.login, "ValidUser", "Login should not have changed after invalid update");
        assertEquals(actualPlayer.screenName, "ValidUserOne", "ScreenName should not have changed after invalid update");
    }

    @Test
    public void testCreateNegativeSelfAsUser() {
        Allure.step("Starting test: testCreateSelfAsUser");

        Integer statusCode = getStatusCodeFromResponse(createPlayerWithResponse(new PlayerDto(14, "Rumba", "Rumba43", "male", "user", "password123"), "user"));
        assertEquals(statusCode, 403, "Wrong status code for invalid editor role");

    }

    @Test
    public void testCreatePlayerNegativeWithInvalidAge() {
        Allure.step("Starting test: testCreatePlayerNegativeWithInvalidAge");

        Allure.step("Trying to create player with underage");
        Integer statusCode = getStatusCodeFromResponse(createPlayerWithResponse(new PlayerDto(14, "Rumba", "Rumba43", "male", "user", "password123"), "supervisor"));

        assertEquals(statusCode, 400, "Wrong status code for underage");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyLogin() {
        Allure.step("Starting test: testCreatePlayerNegativeWithEmptyLogin");

        Allure.step("Trying to create player with empty login");
        Integer statusCode = createPlayerWithResponse(new PlayerDto(20, "", "NoLogin", "male", "user", "password123"), "supervisor").statusCode();

        assertEquals(statusCode, 400, "Wrong status code for empty login");
    }

    @Test
    public void testCreatePlayerNegativeWithInvalidRole() {
        Allure.step("Starting test: testCreatePlayerNegativeWithInvalidRole");

        Allure.step("Trying to create player with invalid role");
        Integer statusCode = createPlayerWithResponse(new PlayerDto(20, "User123", "NoLogin", "male", "worker", "password123"), "supervisor").statusCode();

        assertEquals(statusCode, 400, "Wrong status code for invalid role");
    }

    @Test
    public void testCreatePlayersNegativeWithInvalidPassword() {
        Allure.step("Starting test: testCreatePlayersNegativeWithInvalidPassword");

        SoftAssert softAssert = new SoftAssert();

        Allure.step("Trying to create player with empty password");

        int statusCode = createPlayerWithResponse(new PlayerDto(20, "UserWithEmptyPass", "UserWithEmptyPass", "male", "user", ""), "supervisor").statusCode();
        softAssert.assertEquals(statusCode, 400, "Wrong status code for empty password");

        Allure.step("Trying to create player with too short password");
        statusCode = createPlayerWithResponse(new PlayerDto(20, "UserWithShortPass", "UserWithShortPass", "male", "user", "12d"), "supervisor").statusCode();
        softAssert.assertEquals(statusCode, 400, "Wrong status code for too short password");

        Allure.step("Trying to create player with too long password");
        String longPassword = "a12".repeat(6);
        statusCode = createPlayerWithResponse(new PlayerDto(20, "UserWithLongPass", "UserWithLongPass", "male", "user", longPassword), "supervisor").statusCode();
        softAssert.assertEquals(statusCode, 400, "Wrong status code for too long password");

        Allure.step("Trying to create player with password without latin letters");
        statusCode = createPlayerWithResponse(new PlayerDto(20, "UserWithPassWOLatin", "UserWithPassWOLatin", "male", "user", "1234567"), "supervisor").statusCode();
        softAssert.assertEquals(statusCode, 400, "Wrong status code for password without latin letters");

        Allure.step("Trying to create player with password without digits");
        statusCode = createPlayerWithResponse(new PlayerDto(20, "UserWithPassWODigits", "UserWithPassWODigits", "male", "user", "withoutdigits"), "supervisor").statusCode();
        softAssert.assertEquals(statusCode, 400, "Wrong status code for password without digits");

        softAssert.assertAll();
    }

    @Test // expected to fail (backend bug?)
    public void testCreatePlayerNegativeWithInvalidGender() {
        Allure.step("Starting test: testCreatePlayerNegativeWithInvalidGender");

        Allure.step("Trying to create player with invalid gender");
        Response response = createPlayerWithResponse(new PlayerDto(20, "Mucha", "Mucha33", "Helicopter", "user", "password123"), "supervisor");

        assertEquals(response.statusCode(), 400, "Wrong status code for invalid gender");
    }

    @Test // expected to fail (backend bug?)
    public void testUpdatePlayerNegativeWithEmptyLogin() {
        Allure.step("Starting test: testUpdatePlayerNegativeWithEmptyLogin");

        Allure.step("Creating player with valid data");
        Integer playerId = createPlayer(new PlayerDto(25, "Rumba", "Rumba4", "male", "user", "password123"), "supervisor");

        Allure.step("Trying to update player with empty login");
        PlayerDto updatedPlayer = new PlayerDto(25, "", "Rumba4", "male", "user", "password123");

        Response response = updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Wrong status code for empty login update");
    }

    @Test // expected to fail (backend bug?)
    public void testUpdatePlayerNegativeWithInvalidRole() {
        Allure.step("Starting test: testUpdatePlayerNegativeWithInvalidRole");

        Allure.step("Creating player with valid data");
        Integer playerId = createPlayer(new PlayerDto(25, "validUser3", "Max", "male", "user", "password123"), "supervisor");

        Allure.step("Trying to update player with invalid role");
        PlayerDto updatedPlayer = new PlayerDto(25, "validUser3", "Max", "male", "worker", "password123");

        Response response = updatePlayer("supervisor", updatedPlayer, playerId);
        assertEquals(response.statusCode(), 400, "Wrong status code for invalid role update");
    }

    @Test
    public void testCreatePlayerNegativeWithTooLongLogin() {
        Allure.step("Starting test: testCreatePlayerNegativeWithTooLongLogin");

        Allure.step("Trying to create player with too long login (>50 chars)");
        String longLogin = "A".repeat(60);
        Integer statusCode = createPlayerWithResponse(new PlayerDto(20, longLogin, "TooLong", "male", "user", "password123"), "supervisor").statusCode();

        assertEquals(statusCode, 400, "Wrong status code for too long login");
    }

    @Test
    public void testCreatePlayerNegativeWithEmptyScreenName() {
        Allure.step("Starting test: testCreatePlayerNegativeWithEmptyScreenName");

        Allure.step("Trying to create player with empty screenName");
        Integer statusCode = createPlayerWithResponse(new PlayerDto(20, "ScreenEmpty", "", "male", "user", "password123"), "supervisor").statusCode();

        assertEquals(statusCode, 400, "Wrong status code for empty screenName");
    }
}
