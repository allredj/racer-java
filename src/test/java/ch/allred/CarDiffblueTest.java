package ch.allred;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.Image;
import org.junit.jupiter.api.Test;

public class CarDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange and Act
    Car actualCar = new Car(2, 3);

    // Assert
    assertEquals(0.0, actualCar.getIndicatedSpeed());
    assertEquals(50, actualCar.height);
    assertEquals(50, actualCar.width);
    assertEquals(2.0, actualCar.x);
    assertEquals(3.0, actualCar.y);
    Image expectedImage = actualCar.image;
    assertSame(expectedImage, actualCar.getImage());
    assertEquals(1.5707963267948966, actualCar.getHeading());
    assertTrue(actualCar.isVisible());
  }

  @Test
  public void testUpdateSpeed() {
    // Arrange
    Car car = new Car(2, 3);

    // Act
    car.updateSpeed(10.0);

    // Assert
    assertEquals(0.0, car.getIndicatedSpeed());
  }

  @Test
  public void testMove() {
    // Arrange
    Car car = new Car(2, 3);

    // Act
    car.move(10.0);

    // Assert
    assertEquals(0.0, car.getIndicatedSpeed());
    assertEquals(2.0, car.x);
    assertEquals(3.0, car.y);
  }

  @Test
  public void testMove2() {
    // Arrange
    Car car = new Car(2, 3);

    // Act
    car.move(Double.NaN);

    // Assert
    assertEquals(Double.NaN, car.getIndicatedSpeed());
    assertEquals(Double.NaN, car.x);
    assertEquals(Double.NaN, car.y);
  }
}

