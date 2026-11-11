package dev.deletejava.optional;

/** A customer; not every customer has given us an email address. */
public record Customer(String id, String name, String email) {}
