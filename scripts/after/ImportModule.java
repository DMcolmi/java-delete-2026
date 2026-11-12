// In a regular class, `import module` (final in Java 25) replaces a wall of single-type imports.
import module java.net.http;
import java.net.URI;

public class ImportModule {
    // Demonstrates importing and using a class from a module.
    public static void main(String[] args) {
        // HttpClient, HttpRequest and HttpResponse all come from the one module import above
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://openjdk.org")).build();
        System.out.println(request.method() + " " + request.uri() + " via " + HttpClient.Version.HTTP_2);
    }
}
