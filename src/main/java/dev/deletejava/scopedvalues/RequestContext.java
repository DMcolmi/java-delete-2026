package dev.deletejava.scopedvalues;

/** Per-request data that deep layers need without passing it through every signature. */
public record RequestContext(String tenantId, String userId) {}
