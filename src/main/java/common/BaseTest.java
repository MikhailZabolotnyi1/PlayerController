package common;

import apiCalls.PlayerControllerApi;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.response.Response;
import models.PlayerDto;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Listeners({AllureTestNg.class})
public class BaseTest extends PlayerControllerApi {

    public ArrayList<Integer> playerIdsToClean = new ArrayList<>();


    public Integer createPlayer(PlayerDto player, String editorRole) {
        return getPlayerIdFromResponse(createPlayerWithResponse(player, editorRole));
    }

    public Integer getPlayerIdFromResponse(Response response) {
        try {
            return response.jsonPath().getInt("id");
        } catch (
                Exception noBodyOrIdInResponse) { //probably should change with more specific exception, i just don't know it for now
            return null;
        }
    }

    public int getStatusCodeFromResponse(Response response) {
        return response.getStatusCode();
    }

    public Response createPlayerWithResponse(PlayerDto player, String editorRole) {
        Response response = createPlayer(editorRole, player);
        Integer id = getPlayerIdFromResponse(response);
        if (id != null) {
            System.out.println("Id added to array: " + id);
            playerIdsToClean.add(id); // adding to cleaner
        }
        return response;
    }

    public void deletePlayer(int playerId, String editorRole) {
        Response response = deletePlayer(editorRole, playerId);
        System.out.println("Player successfully deleted" + playerId);
        playerIdsToClean.remove(Integer.valueOf(playerId));
    }

    public String getRandomUserName(String userName) {
        String randomString = UUID.randomUUID().toString();
        return userName + randomString;
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        List<Integer> idsCopied = new ArrayList<>(playerIdsToClean);
        idsCopied.forEach(id -> deletePlayer(id, "supervisor"));
    }
}
