package dev.deletejava.patterns.before;

/** The visitor pattern: two interfaces and an accept() per type, just to switch over a closed hierarchy. */
public final class Shapes {

    public interface ShapeVisitor<R> {
        // Computes a visitor result for a circle.
        R visitCircle(Circle c);
        // Computes a visitor result for a rectangle.
        R visitRectangle(Rectangle r);
        // Computes a visitor result for a triangle.
        R visitTriangle(Triangle t);
    }

    public interface Shape {
        <R> R accept(ShapeVisitor<R> visitor);
    }

    public static final class Circle implements Shape {
        final double radius;
        public Circle(double radius) { this.radius = radius; }
        // Dispatches this circle to the matching visitor method.
        @Override public <R> R accept(ShapeVisitor<R> v) { return v.visitCircle(this); }
    }

    public static final class Rectangle implements Shape {
        final double width;
        final double height;
        public Rectangle(double width, double height) { this.width = width; this.height = height; }
        // Dispatches this rectangle to the matching visitor method.
        @Override public <R> R accept(ShapeVisitor<R> v) { return v.visitRectangle(this); }
    }

    public static final class Triangle implements Shape {
        final double base;
        final double height;
        public Triangle(double base, double height) { this.base = base; this.height = height; }
        // Dispatches this triangle to the matching visitor method.
        @Override public <R> R accept(ShapeVisitor<R> v) { return v.visitTriangle(this); }
    }

    public static final class AreaVisitor implements ShapeVisitor<Double> {
        @Override public Double visitCircle(Circle c) { return Math.PI * c.radius * c.radius; }
        @Override public Double visitRectangle(Rectangle r) { return r.width * r.height; }
        @Override public Double visitTriangle(Triangle t) { return 0.5 * t.base * t.height; }
    }

    // Computes a shape's area through the visitor pattern.
    public static double area(Shape shape) {
        return shape.accept(new AreaVisitor());
    }

    private Shapes() {}
}
