package dev.deletejava.http.before;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

/** Apache HttpClient 5 for a plain GET: one more dependency (plus httpcore5) to keep patched. */
public class WeatherClient implements AutoCloseable {

    private final CloseableHttpClient http = HttpClients.createDefault();
    private final URI baseUri;

    public WeatherClient(URI baseUri) {
        this.baseUri = baseUri;
    }

    // Requests and returns the forecast for a city.
    public String forecast(String city) throws IOException {
        var request = new HttpGet(baseUri.resolve("/forecast?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8)));
        request.addHeader("Accept", "application/json");
        // Throws HttpResponseException on status >= 300
        return http.execute(request, new BasicHttpClientResponseHandler());
    }

    @Override
    // Releases the HTTP client's resources.
    public void close() throws IOException {
        http.close();
    }
}
