package ch.allred;

import java.awt.event.KeyEvent;

public class Car extends Sprite {

  // px/s
  private static final double LOW_SPEED_FILTER = 0.6;

  // rad/s
  private static final double TURN_RATE = 4;

  private static final double AIR_DRAG_COEFFICIENT = 0.01;

  // px/s
  public double getIndicatedSpeed() {
    return indicatedSpeed;
  }

  public String infoString() {
    return String
        .format("Speed:%f, xH:%f, yH:%g, xS:%f, yS:%f\nxF:%f, yF:%f",
            indicatedSpeed,
            xHeading,
            yHeading,
            xSpeed,
            ySpeed,
            xForce,
            yForce);
  }

  // radians
  public double getHeading() {
    return Math.atan2(xHeading, -yHeading);
  }

  private double indicatedSpeed;
  private double xHeading;
  private double yHeading;
  private boolean accelerating;
  private boolean braking;
  private boolean turningRight;
  private boolean turningLeft;
  private double xSpeed;
  private double ySpeed;
  private double xForce;
  private double yForce;
  private double mass; // must be non-zero

  public Car(int x, int y) {
    super(x, y);
    initCar();
  }

  private void initCar() {
    loadImage("src/main/resources/car_red.png");
    getImageDimensions();
    indicatedSpeed = 0;
    xSpeed = 0;
    ySpeed = 0;
    xForce = 0;
    yForce = 0;
    mass = 1;
    xHeading = 1;
    yHeading = 0;
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT) {
      turningLeft = true;
    }
    if (key == KeyEvent.VK_RIGHT) {
      turningRight = true;
    }
    if (key == KeyEvent.VK_UP) {
      accelerating = true;
    }
    if (key == KeyEvent.VK_DOWN) {
      braking = true;
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == KeyEvent.VK_LEFT) {
      turningLeft = false;
    }
    if (key == KeyEvent.VK_RIGHT) {
      turningRight = false;
    }
    if (key == KeyEvent.VK_UP) {
      accelerating = false;
    }
    if (key == KeyEvent.VK_DOWN) {
      braking = false;
    }
  }

  private void updateDynamicsFromInputs(final double timeDiff) {
    if (accelerating) {
      // FIXME: Decouple acceleration from frame rate
      // TODO: Extract magic number
      xSpeed += 2 * xHeading;
      ySpeed += 2 * yHeading;
    }
    if (braking) {
      xSpeed -= 2 * xHeading;
      ySpeed -= 2 * yHeading;
    }
    if (turningLeft) {
      // delta must be acute
      final double delta = - TURN_RATE * timeDiff;
      final double newXHeading = Math.cos(delta) * xHeading - Math.sin(delta) * yHeading;
      final double newYHeading = Math.sin(delta) * xHeading + Math.cos(delta) * yHeading;
      xHeading = newXHeading;
      yHeading = newYHeading;
    }
    if (turningRight) {
      final double delta = TURN_RATE * timeDiff;
      final double newXHeading = Math.cos(delta) * xHeading - Math.sin(delta) * yHeading;
      final double newYHeading = Math.sin(delta) * xHeading + Math.cos(delta) * yHeading;
      xHeading = newXHeading;
      yHeading = newYHeading;
    }
  }

  static double dot(double u1, double u2, double v1, double v2) {
    return u1 * v1 + u2 * v2;
  }

  static double vectorLength(double u1, double u2) {
    return Math.sqrt(u1 * u1 + u2 * u2);
  }

  static double signedAngleBetweenVectors(double u1, double u2, double v1, double v2) {
    return Math.atan2(u2, u1) - Math.atan2(v2, v1);
  }

  static double lateralTyreForce(double xHeading, double yHeading, double xSpeed, double ySpeed) {
    final double speed = vectorLength(xSpeed, ySpeed);
    if (xSpeed - xHeading == 0) {
      return 0;
    }
    return Math.sin(signedAngleBetweenVectors(xHeading, yHeading, xSpeed, ySpeed)) * speed;
  }

  protected void updateForces() {
    final double xAirResistanceForceNewton = -xSpeed * Math.abs(xSpeed) * AIR_DRAG_COEFFICIENT;
    final double yAirResistanceForceNewton = -ySpeed * Math.abs(ySpeed) * AIR_DRAG_COEFFICIENT;
    final double lateralTyreForce = lateralTyreForce(xHeading, yHeading, xSpeed, ySpeed);
    final double xTyreResistanceForceNewton = -yHeading * lateralTyreForce;
    final double yTyreResistanceForceNewton = xHeading * lateralTyreForce;
    xForce = xAirResistanceForceNewton + xTyreResistanceForceNewton;
    yForce = yAirResistanceForceNewton + yTyreResistanceForceNewton;
  }

  //pre-condition: non-zero
  protected void updateSpeed(final double timeDiff) {
    final double dXSpeed = xForce / mass * timeDiff;
    xSpeed += dXSpeed;
    // low-filter
    if (xSpeed > -LOW_SPEED_FILTER && xSpeed < LOW_SPEED_FILTER) {
      xSpeed = 0;
    }
    final double dYSpeed = yForce / mass * timeDiff;
    ySpeed += dYSpeed;
    // low-filter
    if (ySpeed > -LOW_SPEED_FILTER && ySpeed < LOW_SPEED_FILTER) {
      ySpeed = 0;
    }

    indicatedSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
  }

  // pre-condition: non-zero
  public void move(final double timeDiff) {
    if (timeDiff == 0) {
       return;
    }
    updateDynamicsFromInputs(timeDiff);
    updateForces();
    updateSpeed(timeDiff);
    x += xSpeed * timeDiff;
    y += ySpeed * timeDiff;
  }
}
