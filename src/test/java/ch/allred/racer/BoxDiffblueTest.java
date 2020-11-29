package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BoxDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange and Act
    Box actualBox = new Box(2, 3);

    // Assert
    assertTrue(actualBox.isVisible());
    assertEquals(20, actualBox.height);
    assertEquals(3.0, actualBox.y);
    assertEquals(20, actualBox.width);
    assertEquals(2.0, actualBox.x);
  }

  @Test
  public void testUpdateSpeed() {
    // Arrange
    Box box = new Box(2, 3);

    // Act
    box.updateSpeed(10.0);

    // Assert
    assertEquals(0.0, box.ySpeed);
    assertEquals(0.0, box.xSpeed);
  }

  @Test
  public void testMove() {
    // Arrange
    Box box = new Box(2, 3);

    // Act
    box.update(10.0);

    // Assert
    assertEquals(0.0, box.ySpeed);
    assertEquals(3.0, box.y);
    assertEquals(2.0, box.x);
    assertEquals(0.0, box.xSpeed);
  }
}

