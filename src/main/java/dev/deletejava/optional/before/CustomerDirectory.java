package dev.deletejava.optional.before;

import dev.deletejava.optional.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/** null as "not found", and Optional used as a more verbose null check. */
public class CustomerDirectory {

    private final Map<String, Customer> customers;

    public CustomerDirectory(Map<String, Customer> customers) {
        this.customers = Map.copyOf(customers);
    }

    /** Finds a customer by ID, returning null if the customer is absent. */
    public Customer find(String id) {
        return customers.get(id);
    }

    // Returns a customer's email or a default address if they are absent.
    public String emailOrDefault(String id) {
        Customer customer = find(id);
        if (customer != null && customer.email() != null) {
            return customer.email();
        }
        return "no-reply@example.com";
    }

    // Returns a customer's name or throws if the customer is absent.
    public String nameOrThrow(String id) {
        Optional<Customer> customer = Optional.ofNullable(find(id));
        if (customer.isPresent()) {
            return customer.get().name();
        }
        throw new NoSuchElementException("customer " + id);
    }

    // Looks up the emails for all listed customer IDs.
    public List<String> emails(List<String> ids) {
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            Customer customer = find(id);
            if (customer != null && customer.email() != null) {
                result.add(customer.email());
            }
        }
        return result;
    }
}
