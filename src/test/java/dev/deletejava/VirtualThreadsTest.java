package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.virtualthreads.PriceClient;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class VirtualThreadsTest {

    // Simulates a 100 ms remote call
    private final PriceClient slowClient = id -> {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new BigDecimal(id.substring(1));
    };

    private final List<String> ids = IntStream.range(0, 1_000).mapToObj(i -> "p" + i).toList();

    @Test
    // Verifies that both services return prices in the order of the product IDs.
    void sameResultsInOrder() {
        var before = new dev.deletejava.virtualthreads.before.PriceService(slowClient);
        try {
            var expected = before.prices(ids.subList(0, 100));
            var after = new dev.deletejava.virtualthreads.after.PriceService(slowClient);
            assertEquals(expected, after.prices(ids.subList(0, 100)));
            assertEquals(expected, after.pricesAtMost(20, ids.subList(0, 100)));
        } finally {
            before.shutdown();
        }
    }

    @Test
    // Checks that virtual threads overlap many blocking price requests efficiently.
    void thousandBlockingCallsFinishInAboutOneCallTime() {
        var after = new dev.deletejava.virtualthreads.after.PriceService(slowClient);
        // 1000 x 100 ms: the fixed pool of 50 needs ~2 s, one virtual thread per task ~100 ms
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> assertEquals(1_000, after.prices(ids).size()));
    }
}
