package bench;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.HttpClients;

/// A stand-in for an application's startup: touch a few libraries and a good part of the JDK,
/// the way a service does while it wires itself up, then exit. Run it with bench/startup.sh.
public class Startup {

    record Route(String path, String handler) {}

    // Initializes representative libraries and JDK services to model startup work.
    public static void main(String[] args) throws Exception {
        var routes = List.of(new Route("/orders", "OrderHandler"), new Route("/prices", "PriceHandler"));
        var byPath = ImmutableMap.copyOf(routes.stream().collect(Collectors.toMap(Route::path, Route::handler)));
        var cache = CacheBuilder.newBuilder().maximumSize(1_000).expireAfterWrite(Duration.ofMinutes(5)).build();
        cache.put("today", LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        var valid = Pattern.compile("^/[a-z]+$").asPredicate();

        try (var apache = HttpClients.createDefault();
             var jdk = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build()) {
            String banner = StringUtils.center(" ready ", 30, '=');
            if (args.length > 0) {
                System.out.println(banner + " " + byPath.keySet().stream().filter(valid).toList()
                        + " " + cache.size() + " " + apache.getClass().getSimpleName() + " " + jdk.version());
            }
        }
    }
}
