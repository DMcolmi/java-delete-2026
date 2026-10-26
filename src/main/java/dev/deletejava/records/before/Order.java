package dev.deletejava.records.before;

import java.math.BigDecimal;
import java.util.List;
import lombok.NonNull;
import lombok.Value;

/** Classic Lombok value object: immutable, equals/hashCode/toString generated at compile time. */
@Value
public class Order {
    String id;
    String customer;
    List<String> items;
    BigDecimal total;

    // A hand-written constructor is needed for the validation, so @NonNull goes on the parameters:
    // on the fields Lombok only checks inside the constructors it generates itself.
    public Order(@NonNull String id, @NonNull String customer, @NonNull List<String> items, @NonNull BigDecimal total) {
        if (total.signum() < 0) {
            throw new IllegalArgumentException("total must be >= 0");
        }
        this.id = id;
        this.customer = customer;
        this.items = List.copyOf(items);
        this.total = total;
    }
}
