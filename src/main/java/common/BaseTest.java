package common;

import apiCalls.PlayerControllerApi;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import models.PlayerDto;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;
import java.util.UUID;

public class BaseTest extends PlayerControllerApi {

    public ArrayList playerIdsToClean = new ArrayList<>();


    public int createPlayer(PlayerDto player1) {
        return createPlayerWithResponse(player1, true).jsonPath().getInt("id");

    }

    public int getPlayerIdFromResponse(Response response) {
        return response.jsonPath().getInt("id");
    }

    public int getStatusCodeFromResponse(Response response) {
        return response.getStatusCode();
    }

    public Response createPlayerWithResponse(PlayerDto player, boolean clean) {
        Response response = createPlayer("supervisor", player);
        if (clean)
           playerIdsToClean.add(getPlayerIdFromResponse(response)); // adding to cleaner
        return response;
    }

    public void deletePlayer(int playerId) {
        Response response = deletePlayer("supervisor", playerId);
        response.then().statusCode(204);
    }

    public String getRandomUserName (String userName) {
        String randomString = UUID.randomUUID().toString();
        return userName + randomString;
    }

    @AfterSuite(alwaysRun = true)
    public void cleanup() {


        for (Object playerIdToClean : playerIdsToClean ) {
            Allure.step("Cleanup: deleting player with ID" + playerIdToClean);
            Response deleteResponse = deletePlayer("supervisor", (Integer) playerIdToClean);
            deleteResponse.then().statusCode(204);
            Allure.step("Player successfully deleted" + playerIdToClean);
        }
    }
}
