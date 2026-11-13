package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HttpTest {

    private HttpServer server;
    private URI baseUri;

    @BeforeEach
    // Starts a local HTTP server for the client tests.
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/forecast", exchange -> {
            String body = switch (exchange.getRequestURI().getRawQuery()) {
                case "city=Milan" -> "{\"city\":\"Milan\",\"sky\":\"sunny\"}";
                case "city=New+York" -> "{\"city\":\"New York\",\"sky\":\"rain\"}";
                case null, default -> null;
            };
            boolean known = body != null;
            byte[] bytes = (known ? body : "not found").getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(known ? 200 : 404, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();
        baseUri = URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    }

    @AfterEach
    // Stops the local HTTP server after each test.
    void stopServer() {
        server.stop(0);
    }

    @Test
    // Verifies that both clients return the same successful response body.
    void sameBody() throws Exception {
        try (var before = new dev.deletejava.http.before.WeatherClient(baseUri);
             var after = new dev.deletejava.http.after.WeatherClient(baseUri)) {
            assertEquals(before.forecast("Milan"), after.forecast("Milan"));
            assertEquals(before.forecast("New York"), after.forecast("New York")); // the city goes in the URL encoded
        }
    }

    @Test
    // Verifies that both clients report unsuccessful HTTP status codes.
    void bothFailOnErrorStatus() throws Exception {
        try (var before = new dev.deletejava.http.before.WeatherClient(baseUri);
             var after = new dev.deletejava.http.after.WeatherClient(baseUri)) {
            assertThrows(IOException.class, () -> before.forecast("Atlantis"));
            assertThrows(IOException.class, () -> after.forecast("Atlantis"));
        }
    }
}
