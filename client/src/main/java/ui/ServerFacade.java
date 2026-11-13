package ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;
    private String authToken;

    public ServerFacade(){
        serverURL = "http://localhost:8080";
        authToken = null;
    }

    public String register(UserData userData) throws Exception{
        var request = buildRequest("POST", "/user", userData);
        var response = sendRequest(request);
        checkResponse(response);
        Gson gson = new Gson();
        AuthData authdata = gson.fromJson(response.body(),AuthData.class);
        return authdata.username();
    }

    public String logIn(UserData userData) throws Exception{
        var request = buildRequest("POST","/session",userData);
        var response = sendRequest(request);
        checkResponse(response);
        Gson gson = new Gson();
        AuthData authdata = gson.fromJson(response.body(),AuthData.class);
        authToken = authdata.authToken();
        return authdata.username();
    }

    public void logOut() throws Exception{
        var request = buildRequest("DELETE","/session",null);
        var response = sendRequest(request);
        checkResponse(response);
        authToken = null;
    }

    public int createGame(String gameName) throws Exception{
        var request = buildRequest("POST","/game",gameName);
        var response = sendRequest(request);
        checkResponse(response);
        Gson gson = new Gson();
        int gameID = gson.fromJson(response.body(),Integer.class);
        return  gameID;
    }

    public void playGame(){
        var request = buildRequest("PUT","/game",null);
    }

    public void observeGame(){
        var request = buildRequest("PUT","/game",null);
    }

    public ArrayList<GameData> getGameList() throws Exception{
        var request = buildRequest("GET","/game",null);
        var response = sendRequest(request);
        checkResponse(response);
        Gson gson = new Gson();
        GameList games = gson.fromJson(response.body(),GameList.class);
        return games.games();
    }

    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder(URI.create(serverURL+path))
                .method(method, makeBody(body));

        if(authToken != null){
            request.setHeader("authorization",authToken);
        }

        return request.build();
    }

    private static HttpRequest.BodyPublisher makeBody(Object body){
        if(body != null){
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(body));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void checkResponse(HttpResponse<String> response) throws Exception {
        var statusCode = response.statusCode();

        switch(statusCode){
            case 400:
                throw new Exception("This operation is invalid. Please check the help menu for the proper formatting.\n\n");
            case 401:
                throw new Exception("Your authorization is invalid. Please try again!\n\n");
            case 402:
                throw new Exception("What you just requested has already been taken. Please try something else!\n\n");
            case 500:
                throw new Exception("The server failed to connect. Sorry for the inconvenience!\n\n");

        }
    }

}
