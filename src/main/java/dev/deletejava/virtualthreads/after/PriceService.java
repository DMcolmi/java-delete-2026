package dev.deletejava.virtualthreads.after;

import dev.deletejava.virtualthreads.PriceClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Gatherers;

/// One virtual thread per task: no pool to size, no shutdown to remember, plain blocking code.
public class PriceService {

    private final PriceClient client;

    public PriceService(PriceClient client) {
        this.client = client;
    }

    // Fetches prices concurrently and returns them in input order.
    public List<BigDecimal> prices(List<String> productIds) {
        List<Future<BigDecimal>> futures;
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            futures = productIds.stream()
                    .map(id -> executor.submit(() -> client.price(id)))
                    .toList();
        } // ExecutorService is AutoCloseable since Java 19: close() waits for every task
        return futures.stream().map(Future::resultNow).toList();
    }

    /// When the downstream service can't take unbounded load: a concurrency limit in one line,
    /// results in input order, and each call runs on its own virtual thread.
    // Fetches prices concurrently while limiting the number of active calls.
    public List<BigDecimal> pricesAtMost(int maxConcurrency, List<String> productIds) {
        return productIds.stream()
                .gather(Gatherers.mapConcurrent(maxConcurrency, client::price))
                .toList();
    }
}
