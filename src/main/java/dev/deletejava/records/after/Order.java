package dev.deletejava.records.after;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/// Same value object as a record: no annotation processor, no IDE plugin.
/// The compact constructor holds the validation and the defensive copy.
public record Order(String id, String customer, List<String> items, BigDecimal total) {

    public Order {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(customer, "customer");
        Objects.requireNonNull(total, "total");
        if (total.signum() < 0) {
            throw new IllegalArgumentException("total must be >= 0");
        }
        items = List.copyOf(items);
    }
}
