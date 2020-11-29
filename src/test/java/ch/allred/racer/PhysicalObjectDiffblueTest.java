package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PhysicalObjectDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange and Act
    PhysicalObject actualPhysicalObject = new PhysicalObject(2, 3);

    // Assert
    assertTrue(actualPhysicalObject.isVisible());
    assertEquals(3.0, actualPhysicalObject.y);
    assertEquals(2.0, actualPhysicalObject.x);
  }

  @Test
  public void testConstructor2() {
    // Arrange and Act
    PhysicalObject actualPhysicalObject = new PhysicalObject(2, 3, 1, 1);

    // Assert
    assertTrue(actualPhysicalObject.isVisible());
    assertEquals(1, actualPhysicalObject.height);
    assertEquals(3.0, actualPhysicalObject.y);
    assertEquals(1, actualPhysicalObject.width);
    assertEquals(2.0, actualPhysicalObject.x);
  }
}

