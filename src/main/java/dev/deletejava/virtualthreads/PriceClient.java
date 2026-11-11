package dev.deletejava.virtualthreads;

import java.math.BigDecimal;

/** A blocking remote call, e.g. a REST or JDBC lookup. */
@FunctionalInterface
public interface PriceClient {
    // Fetches the price for a product ID.
    BigDecimal price(String productId);
}
