package dev.deletejava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class GatherersTest {

    private final List<Integer> values = List.of(1, 2, 3, 4, 5, 6, 7);

    @Test
    // Verifies that both implementations produce the same fixed-size batches.
    void sameBatches() {
        assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7)),
                dev.deletejava.gatherers.after.Batches.inBatchesOf(values, 3));
        assertEquals(dev.deletejava.gatherers.before.Batches.inBatchesOf(values, 3),
                dev.deletejava.gatherers.after.Batches.inBatchesOf(values, 3));
    }

    @Test
    // Verifies that both implementations calculate the same sliding averages.
    void sameMovingAverage() {
        assertEquals(dev.deletejava.gatherers.before.Batches.movingAverage(values, 3),
                dev.deletejava.gatherers.after.Batches.movingAverage(values, 3));
    }
}
