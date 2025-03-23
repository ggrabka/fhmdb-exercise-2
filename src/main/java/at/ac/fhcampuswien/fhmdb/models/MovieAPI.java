package at.ac.fhcampuswien.fhmdb.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.utils.URIBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;

public class MovieAPI {

    Gson gson = new Gson();
    String url = "https://prog2.fh-campuswien.ac.at/movies" ;
    URIBuilder uriBuilder;

    public Collection<Movie> getFilmList(String query, String genre) throws Exception {
        uriBuilder = new URIBuilder(url);
        if (query != null && !query.trim().isEmpty()) {
            uriBuilder.addParameter("query", query.trim());
        }
        if ((genre != null && genre != "No filter") && !genre.trim().isEmpty()) {
            uriBuilder.addParameter("genre", genre.trim().toUpperCase());
        }
        URI uri = uriBuilder.build();

        uriBuilder.build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type","application/json")
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());

        Type type = new TypeToken<ArrayList<Movie>>() {}.getType();
        ArrayList<Movie> jsonResponse = gson.fromJson(getResponse.body(), type);

        return jsonResponse;
    }
}
