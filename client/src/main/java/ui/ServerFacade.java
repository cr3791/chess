package ui;

import com.google.gson.Gson;
import model.AuthData;
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
    public ServerFacade(){
        serverURL = "http://localhost:8080";
    }

    public void register(UserData userData) {
        try {
            var request = buildRequest("POST", "/user", userData);
            var response = sendRequest(request);
            handleResponse(response);
        } catch (IOException | InterruptedException ex){
            System.out.println("Http Request has failed");
        }
    }

    public void logIn(){
        var request = buildRequest(".post","/session",null);
    }

    public void logOut(){
        var request = buildRequest(".delete","/session",null);
    }

    public void createGame(){
        var request = buildRequest(".post","/game",null);
    }

    public void playGame(){
        var request = buildRequest(".put","/game",null);
    }

    public void observeGame(){
        var request = buildRequest(".put","/game",null);
    }

    public void getGameList(){
        var request = buildRequest(".get","/game",null);
    }

    private HttpRequest buildRequest(String method, String path, Object body){
        var request = HttpRequest.newBuilder(URI.create(serverURL+path))
                .method(method, makeBody(body))
                .build();
        return request;
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

    private void handleResponse(HttpResponse<String> response){
        var statusCode = response.statusCode();

        if(statusCode == 200) {
            System.out.println(response.body());
            var gson = new Gson();
            AuthData authData = gson.fromJson(response.body(), AuthData.class);
            System.out.println(authData.toString());
        } else {

        }
    }

}
