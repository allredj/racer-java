package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class MovingObjectDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange and Act
    MovingObject actualMovingObject = new MovingObject(2, 3);

    // Assert
    assertTrue(actualMovingObject.isVisible());
    assertEquals(3.0, actualMovingObject.y);
    assertEquals(2.0, actualMovingObject.x);
  }

  @Test
  public void testConstructor2() {
    // Arrange and Act
    MovingObject actualMovingObject = new MovingObject(2, 3, 1, 1);

    // Assert
    assertTrue(actualMovingObject.isVisible());
    assertEquals(1, actualMovingObject.height);
    assertEquals(3.0, actualMovingObject.y);
    assertEquals(1, actualMovingObject.width);
    assertEquals(2.0, actualMovingObject.x);
  }
}

