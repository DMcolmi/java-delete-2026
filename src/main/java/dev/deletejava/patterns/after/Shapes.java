package dev.deletejava.patterns.after;

/// Sealed interface + records + pattern matching switch.
/// The switch is exhaustive: add a new permitted type and every switch without it stops compiling,
/// which is the guarantee the visitor gave you.
public final class Shapes {

    public sealed interface Shape permits Circle, Rectangle, Triangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Triangle(double base, double height) implements Shape {}

    // Computes a shape's area by matching its type.
    public static double area(Shape shape) {
        return switch (shape) {
            case Circle(var r)          -> Math.PI * r * r;
            case Rectangle(var w, var h) -> w * h;
            case Triangle(var b, var h)  -> 0.5 * b * h;
        };
    }

    private Shapes() {}
}
