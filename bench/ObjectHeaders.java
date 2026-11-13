/// Heap used by 10 million small objects, with and without compact object headers (JEP 519).
///
///     java -XX:-UseCompactObjectHeaders bench/ObjectHeaders.java
///     java -XX:+UseCompactObjectHeaders bench/ObjectHeaders.java
import module java.management;
import module jdk.management;

record Point(int x, int y) {}

// Measures heap usage for sample objects with compact object headers.
void main() {
    var points = new ArrayList<Point>(10_000_000);
    for (int i = 0; i < 10_000_000; i++) {
        points.add(new Point(i, -i));
    }
    System.gc();
    long usedMb = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / (1024 * 1024);
    var compact = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class).getVMOption("UseCompactObjectHeaders");
    IO.println(String.format(Locale.ROOT, "Java %s, UseCompactObjectHeaders=%s (%s): %d MB of heap for %,d points",
            Runtime.version().feature(), compact.getValue(), compact.getOrigin(), usedMb, points.size()));
}
