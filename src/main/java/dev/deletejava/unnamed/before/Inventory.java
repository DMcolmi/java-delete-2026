package dev.deletejava.unnamed.before;

import java.util.List;
import java.util.Map;

/** Names that exist only because the syntax demands one: ignored, unused, e, k. */
public final class Inventory {

    public sealed interface Event permits Restocked, Sold {}
    public record Restocked(String sku, int quantity, String warehouse) implements Event {}
    public record Sold(String sku, int quantity, String channel) implements Event {}

    // Parses an integer, returning zero if the input is not numeric.
    public static int parseOrZero(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    // Counts the number of SKU entries.
    public static int count(Iterable<String> skus) {
        int n = 0;
        for (String unused : skus) {
            n++;
        }
        return n;
    }

    // Sums the stock quantities across all SKUs.
    public static int totalStock(Map<String, Integer> stockBySku) {
        int[] total = {0};
        stockBySku.forEach((k, v) -> total[0] += v);
        return total[0];
    }

    // Calculates net stock change from restock and sale events.
    public static int netChange(List<Event> events) {
        int net = 0;
        for (Event e : events) {
            net += switch (e) {
                case Restocked(String sku, int quantity, String warehouse) -> quantity;
                case Sold(String sku, int quantity, String channel) -> -quantity;
            };
        }
        return net;
    }

    private Inventory() {}
}
