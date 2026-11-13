package dev.deletejava;

import static org.junit.jupiter.api.Assertions.*;

import dev.deletejava.datetime.after.Dates;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class DateTimeTest {

    private final List<String> days = IntStream.rangeClosed(1, 28)
            .mapToObj(d -> "2026-02-%02d".formatted(d))
            .toList();

    @Test
    // Compares the date helpers for ordinary weekday calculations.
    void sameResultsOnOneThread() {
        for (String day : days) {
            assertEquals(dev.deletejava.datetime.before.Dates.plusBusinessDays(day, 5), Dates.plusBusinessDays(day, 5), day);
        }
        assertEquals("2026-01-05", Dates.plusBusinessDays("2026-01-02", 1)); // Friday -> Monday
        assertEquals(dev.deletejava.datetime.before.Dates.daysBetween("2026-01-01", "2026-02-01"),
                Dates.daysBetween("2026-01-01", "2026-02-01"));
    }

    @Test
    // Demonstrates how elapsed milliseconds can miscount days across DST.
    void daylightSavingTimeEatsADay() {
        // Clocks go forward in Rome on 29 March 2026: that "day" is 23 hours long
        assertEquals(1, dev.deletejava.datetime.before.Dates.daysBetween("2026-03-28", "2026-03-30"));
        assertEquals(2, Dates.daysBetween("2026-03-28", "2026-03-30"));
    }

    @Test
    // Shows that sharing a mutable date formatter can produce incorrect results.
    void sharedSimpleDateFormatBreaksUnderLoad() {
        int calls = 10_000;
        int beforeWrong = wrongResults(calls, day -> dev.deletejava.datetime.before.Dates.plusBusinessDays(day, 5));
        int afterWrong = wrongResults(calls, day -> Dates.plusBusinessDays(day, 5));

        // How badly the shared formatter breaks depends on the machine, so it's reported, not asserted
        System.out.printf("Shared SimpleDateFormat: %d wrong results or exceptions out of %d calls%n", beforeWrong, calls);
        assertEquals(0, afterWrong);
    }

    // Counts incorrect date results produced by a formatter under concurrent load.
    private int wrongResults(int calls, java.util.function.UnaryOperator<String> plusFive) {
        var wrong = new AtomicInteger();
        try (var executor = Executors.newFixedThreadPool(8)) {
            for (int i = 0; i < calls; i++) {
                String day = days.get(i % days.size());
                String expected = Dates.plusBusinessDays(day, 5);
                executor.submit(() -> {
                    try {
                        if (!expected.equals(plusFive.apply(day))) {
                            wrong.incrementAndGet();
                        }
                    } catch (RuntimeException _) {
                        wrong.incrementAndGet();
                    }
                });
            }
        }
        return wrong.get();
    }
}
