package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CarTest {

  @Test
  public void testSignedAngleBetweenVectors() {
    assertEquals(0.0, Car.signedAngleBetweenVectors(10.0, 10.0, 10.0, 10.0));
    assertEquals(Math.PI/2, Car.signedAngleBetweenVectors(0.0, 10.0, 10.0, 0.0));
    assertEquals(Math.PI, Car.signedAngleBetweenVectors(0.0, 10.0, 0.0, -10.0));
    assertEquals(-Math.PI/2, Car.signedAngleBetweenVectors(0.0, 10.0, -10.0, 0.0));
  }

  @Test
  public void testAngleBetweenVectors() {
    Assertions.assertEquals(0.0, Car.signedAngleBetweenVectors(10.0, 10.0, 10.0, 10.0));
  }

  @Test
  public void testLateralTyreForce() {
    assertEquals(0.0, Car.lateralTyreForce(0, 1, 0, 1));
    assertEquals(300.0, Car.lateralTyreForce(0, 1, 1, 0));
    assertEquals(-300.0, Car.lateralTyreForce(0, 1, -1, 0));
    assertEquals(0.0, Car.lateralTyreForce(10.0, 10.0, 10.0, 10.0));
  }

}

