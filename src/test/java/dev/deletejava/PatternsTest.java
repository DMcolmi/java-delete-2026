package dev.deletejava;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.deletejava.patterns.after.Shapes;
import org.junit.jupiter.api.Test;

class PatternsTest {

    @Test
    // Compares area calculations for each supported shape.
    void sameAreas() {
        assertEquals(dev.deletejava.patterns.before.Shapes.area(new dev.deletejava.patterns.before.Shapes.Circle(2)),
                Shapes.area(new Shapes.Circle(2)));
        assertEquals(dev.deletejava.patterns.before.Shapes.area(new dev.deletejava.patterns.before.Shapes.Rectangle(3, 4)),
                Shapes.area(new Shapes.Rectangle(3, 4)));
        assertEquals(dev.deletejava.patterns.before.Shapes.area(new dev.deletejava.patterns.before.Shapes.Triangle(3, 4)),
                Shapes.area(new Shapes.Triangle(3, 4)));
    }
}
