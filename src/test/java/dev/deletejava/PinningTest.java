package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.pinning.after.TokenCache;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class PinningTest {

    @Test
    // Verifies that both token caches refresh and reuse tokens identically.
    void sameCachingBehaviour() {
        var clock = Clock.fixed(Instant.parse("2026-10-21T10:00:00Z"), ZoneOffset.UTC);
        var fetches = new AtomicInteger();
        var before = new dev.deletejava.pinning.before.TokenCache(() -> new dev.deletejava.pinning.before.TokenCache.Token(
                "t" + fetches.incrementAndGet(), clock.instant().plusSeconds(60)), clock);
        assertEquals("t1", before.token());
        assertEquals("t1", before.token());

        var afterFetches = new AtomicInteger();
        var after = new TokenCache(() -> new TokenCache.Token(
                "t" + afterFetches.incrementAndGet(), clock.instant().plusSeconds(60)), clock);
        assertEquals("t1", after.token());
        assertEquals("t1", after.token());
    }

    /// Every token() call blocks for 50 ms inside synchronized, on more virtual threads than there are carriers.
    /// On Java 21 the waiting threads would pin all the carriers; since Java 24 unrelated virtual threads keep running.
    @Test
    // Checks that synchronized blocking does not starve other virtual threads.
    void synchronizedDoesNotStarveOtherVirtualThreads() {
        var clock = Clock.systemUTC();
        var cache = new TokenCache(() -> {
            sleep(50);
            return new TokenCache.Token("t", Instant.EPOCH); // always expired: every call blocks in the lock
        }, clock);

        int carriers = Runtime.getRuntime().availableProcessors();
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < carriers * 4; i++) {
                    executor.submit(cache::token);
                }
                long start = System.nanoTime();
                executor.submit(() -> "unrelated work").get();
                assertTrue(Duration.ofNanos(System.nanoTime() - start).toMillis() < 500,
                        "an unrelated virtual thread should not wait for the lock holders");
            }
        });
    }

    // Sleeps for the requested duration while preserving interruption.
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
