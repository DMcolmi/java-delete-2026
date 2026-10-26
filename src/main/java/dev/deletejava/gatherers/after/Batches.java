package dev.deletejava.gatherers.after;

import java.util.List;
import java.util.stream.Gatherers;

/// Stream Gatherers (final in Java 24, so available in the Java 25 LTS):
/// custom intermediate operations, with a few built in.
public final class Batches {

  // Splits the given IDs into fixed-size batches.
  public static List<List<Integer>> inBatchesOf(List<Integer> ids, int size) {
    return ids.stream().gather(Gatherers.windowFixed(size)).toList();
  }

  // Calculates the average of each sliding window of values.
  public static List<Double> movingAverage(List<Integer> values, int window) {
    return values.stream()
        .gather(Gatherers.windowSliding(window))
        .map(w -> w.stream().mapToInt(Integer::intValue).average().orElseThrow())
        .toList();
  }

  private Batches() {}
}
