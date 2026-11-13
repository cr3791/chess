package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;
    public ServerFacade(){
        serverURL = "http://localhost:8080";
    }

    public void register(UserData userData) throws Exception{
        try {
            var request = buildRequest("POST", "/user", userData);
            var response = sendRequest(request);
            handleResponse(response);
        } catch (IOException | InterruptedException ex){
            System.out.println("Http Request has failed");
        }
    }

    public void logIn(UserData userData) throws Exception{
        try {
            var request = buildRequest("POST","/session",userData);
            var response = sendRequest(request);
            handleResponse(response);
        } catch (IOException | InterruptedException ex){
            System.out.println("Http Request has failed");
        }
    }

    public void logOut(){
        var request = buildRequest("DELETE","/session",null);
    }

    public void createGame(){
        var request = buildRequest("POST","/game",null);
    }

    public void playGame(){
        var request = buildRequest("PUT","/game",null);
    }

    public void observeGame(){
        var request = buildRequest("PUT","/game",null);
    }

    public void getGameList(){
        var request = buildRequest("GET","/game",null);
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

    private void handleResponse(HttpResponse<String> response) throws Exception {
        var statusCode = response.statusCode();

        switch(statusCode){
            case 400:
                throw new Exception("This operation is invalid. Please check the help menu for the proper formatting.");
            case 401:
                throw new Exception("Your authorization is invalid. Please try again!");
            case 402:
                throw new Exception("What you just requested has already been taken. Please try something else!");
            case 500:
                throw new Exception("The server failed to connect. Sorry for the inconvenience!");

        }
    }

}
