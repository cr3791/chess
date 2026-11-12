package ui;

import com.google.gson.Gson;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;
    public ServerFacade(){
        serverURL = "http://localhost.8080";
    }

    public void register(UserData userData) {
        var request = buildRequest("POST","/user",userData);

    }

    public void logIn(){
        var request = buildRequest("POST","/session",null);
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
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL+path))
                .method(method, makeRequestBody(body));
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request){
        if (request != null){
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request){
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Throwable ex){
            throw new RuntimeException("");
        }
    }

    private void handleResponse(){}

}
