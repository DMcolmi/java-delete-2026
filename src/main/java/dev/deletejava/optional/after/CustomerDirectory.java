package dev.deletejava.optional.after;

import dev.deletejava.optional.Customer;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/// "Not found" is in the return type, and no caller needs `isPresent()` + `get()`.
/// `orElseThrow` with a supplier (Java 8), `Optional.stream()` (Java 9), `Stream.toList()` (Java 16).
public class CustomerDirectory {

    private final Map<String, Customer> customers;

    public CustomerDirectory(Map<String, Customer> customers) {
        this.customers = Map.copyOf(customers);
    }

    // Finds a customer by ID, returning an empty optional if absent.
    public Optional<Customer> find(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    // Returns a customer's email or a default address if they are absent.
    public String emailOrDefault(String id) {
        return find(id).map(Customer::email).orElse("no-reply@example.com");
    }

    // Returns a customer's name or throws if the customer is absent.
    public String nameOrThrow(String id) {
        return find(id).map(Customer::name).orElseThrow(() -> new NoSuchElementException("customer " + id));
    }

    // Looks up the emails for all listed customer IDs.
    public List<String> emails(List<String> ids) {
        return ids.stream()
                .flatMap(id -> find(id).map(Customer::email).stream())
                .toList();
    }
}
