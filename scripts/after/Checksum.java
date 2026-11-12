/// Compact source file + instance main (final in Java 25): no class declaration, no static,
/// and the whole java.base module is imported implicitly. IO.println is in java.lang.
/// Run it with: java scripts/after/Checksum.java <file>...
// Calculates and prints the checksum for each input file.
void main(String[] args) throws Exception {
    if (args.length == 0) {
        IO.println("usage: java Checksum.java <file>...");
        return;
    }
    for (var arg : args) {
        var hash = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(Path.of(arg)));
        IO.println(HexFormat.of().formatHex(hash) + "  " + arg);
    }
}
