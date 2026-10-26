package dev.deletejava.gatherers.before;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

/** Batching and moving averages: Guava for one, a hand-written loop for the other. */
public final class Batches {

    // Splits the given IDs into fixed-size batches.
    public static List<List<Integer>> inBatchesOf(List<Integer> ids, int size) {
        return Lists.partition(ids, size);
    }

    // Calculates the average of each sliding window of values.
    public static List<Double> movingAverage(List<Integer> values, int window) {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i + window <= values.size(); i++) {
            double sum = 0;
            for (int j = i; j < i + window; j++) {
                sum += values.get(j);
            }
            result.add(sum / window);
        }
        return result;
    }

    private Batches() {}
}
