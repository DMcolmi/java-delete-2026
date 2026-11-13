package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;

class ConcurrentMapsTest {

    private final List<String> pages = List.of("/", "/pricing", "/docs");

    @Test
    // Verifies that concurrent updates produce the expected page counts.
    void sameCountsUnderConcurrency() {
        var before = new dev.deletejava.concurrentmaps.before.HitCounter();
        var after = new dev.deletejava.concurrentmaps.after.HitCounter();
        int perPage = 10_000;

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < perPage * pages.size(); i++) {
                String page = pages.get(i % pages.size());
                executor.submit(() -> {
                    before.record(page);
                    after.record(page);
                    before.snapshot(); // iterating while others write must be safe too
                    after.snapshot();
                });
            }
        }

        var expected = Map.of("/", perPage, "/pricing", perPage, "/docs", perPage);
        assertEquals(expected, before.snapshot());
        assertEquals(expected, after.snapshot());
        assertEquals(0, after.count("/missing"));
    }
}
