package dev.deletejava.virtualthreads.before;

import dev.deletejava.virtualthreads.PriceClient;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** A sized pool of platform threads plus CompletableFuture plumbing, all to wait on I/O in parallel. */
public class PriceService {

    // Sized by guesswork: too small and requests queue, too large and memory goes up.
    private final ExecutorService pool = Executors.newFixedThreadPool(50);
    private final PriceClient client;

    public PriceService(PriceClient client) {
        this.client = client;
    }

    // Fetches prices concurrently and returns them in input order.
    public List<BigDecimal> prices(List<String> productIds) {
        List<CompletableFuture<BigDecimal>> futures = productIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> client.price(id), pool))
                .toList();
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
        return futures.stream().map(CompletableFuture::join).toList();
    }

    // Stops the executor used for asynchronous price requests.
    public void shutdown() {
        pool.shutdown();
    }
}
