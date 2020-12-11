package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.awt.Image;
import org.junit.jupiter.api.Test;

public class CarDiffblueTest {
  @Test
  public void testInfoString() {
    // Arrange, Act and Assert
    assertEquals("Speed:0.000000, xH:1.000000, yH:0.00000, xS:0.000000, yS:0.000000\nxF:0.000000, yF:0.000000",
        (new Car(2, 3)).infoString());
  }

  @Test
  public void testGetHeading() {
    // Arrange, Act and Assert
    assertEquals(1.5707963267948966, (new Car(2, 3)).getHeading());
  }

  @Test
  public void testConstructor() {
    // Arrange and Act
    Car actualCar = new Car(2, 3);

    // Assert
    assertEquals(0.0, actualCar.getIndicatedSpeed());
    assertEquals(50, actualCar.height);
    assertEquals(3.0, actualCar.y);
    assertEquals(0.0, actualCar.ySpeed);
    assertEquals(50, actualCar.width);
    assertEquals(2.0, actualCar.x);
    assertEquals(0.0, actualCar.xSpeed);
    assertEquals(1.5707963267948966, actualCar.getHeading());
    Image expectedImage = actualCar.image;
    assertSame(expectedImage, actualCar.getImage());
    assertTrue(actualCar.isVisible());
  }

  @Test
  public void testConstructor2() {
    // Arrange and Act
    Car actualCar = new Car(2, 3, 1);

    // Assert
    assertEquals(0.0, actualCar.getIndicatedSpeed());
    assertEquals(50, actualCar.height);
    assertEquals(3.0, actualCar.y);
    assertEquals(0.0, actualCar.ySpeed);
    assertEquals(50, actualCar.width);
    assertEquals(2.0, actualCar.x);
    assertEquals(0.0, actualCar.xSpeed);
    assertEquals(1.5707963267948966, actualCar.getHeading());
    Image expectedImage = actualCar.image;
    assertSame(expectedImage, actualCar.getImage());
    assertTrue(actualCar.isVisible());
  }

  @Test
  public void testConstructor3() {
    // Arrange and Act
    Car actualCar = new Car(2, 3, 0);

    // Assert
    assertEquals(0.0, actualCar.getIndicatedSpeed());
    assertEquals(50, actualCar.height);
    assertEquals(3.0, actualCar.y);
    assertEquals(0.0, actualCar.ySpeed);
    assertEquals(50, actualCar.width);
    assertEquals(2.0, actualCar.x);
    assertEquals(0.0, actualCar.xSpeed);
    assertEquals(1.5707963267948966, actualCar.getHeading());
    Image expectedImage = actualCar.image;
    assertSame(expectedImage, actualCar.getImage());
    assertTrue(actualCar.isVisible());
  }

  @Test
  public void testSignedAngleBetweenVectors() {
    // Arrange, Act and Assert
    assertEquals(0.0, Car.signedAngleBetweenVectors(10.0, 10.0, 10.0, 10.0));
  }

  @Test
  public void testLateralTyreForce() {
    // Arrange, Act and Assert
    assertEquals(0.0, Car.lateralTyreForce(10.0, 10.0, 10.0, 10.0));
    assertEquals(565.6854249492379, Car.lateralTyreForce(0.0, 10.0, 10.0, 10.0));
  }

  @Test
  public void testUpdateSpeed() {
    // Arrange
    Car car = new Car(2, 3);

    // Act
    car.updateSpeed(10.0);

    // Assert
    assertEquals(0.0, car.getIndicatedSpeed());
    assertEquals(0.0, car.ySpeed);
    assertEquals(0.0, car.xSpeed);
  }

  @Test
  public void testUpdate() {
    // Arrange
    Car car = new Car(2, 3);

    // Act
    car.update(10.0);

    // Assert
    assertEquals(0.0, car.getIndicatedSpeed());
    assertEquals(3.0, car.y);
    assertEquals(0.0, car.ySpeed);
    assertEquals(2.0, car.x);
    assertEquals(0.0, car.xSpeed);
  }
}

