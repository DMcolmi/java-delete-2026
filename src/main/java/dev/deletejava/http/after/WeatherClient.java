package dev.deletejava.http.after;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/// java.net.http.HttpClient: in the JDK since Java 11, HTTP/2, async API, AutoCloseable since Java 21.
/// On JDK 26+ it speaks HTTP/3 too: `HttpClient.newBuilder().version(HttpClient.Version.HTTP_3)`.
public class WeatherClient implements AutoCloseable {

    private final HttpClient http = HttpClient.newHttpClient();
    private final URI baseUri;

    public WeatherClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    // Requests and returns the forecast for a city.
    public String forecast(String city) throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder(baseUri.resolve("/forecast?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8)))
                .header("Accept", "application/json")
                .build();
        var response = http.send(request, HttpResponse.BodyHandlers.ofString());
        // The JDK client doesn't treat error statuses as exceptions: check them yourself
        if (response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode());
        }
        return response.body();
    }

    @Override
    // Releases the HTTP client's resources.
    public void close() {
        http.close();
    }
}
