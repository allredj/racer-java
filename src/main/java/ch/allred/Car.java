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

  // radians
  public double getHeading() {
    return heading;
  }

  private double indicatedSpeed;
  private double heading;
  private boolean accelerating;
  private boolean braking;
  private boolean turningRight;
  private boolean turningLeft;
  private double xSpeed;
  private double ySpeed;
  private double xForce;
  private double yForce;
  private double mass;

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
    heading = Math.PI / 2;
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
      xSpeed += 2 * Math.sin(heading);
      ySpeed -= 2 * Math.cos(heading);
    }
    if (braking) {
      xSpeed -= 2 * Math.sin(heading);
      ySpeed += 2 * Math.cos(heading);
    }
    if (turningLeft) {
      heading = heading - TURN_RATE * timeDiff;
      if (heading <= 0) {
        heading += 2 * Math.PI;
      }
    }
    if (turningRight) {
      heading = heading + TURN_RATE * timeDiff;
      if (heading >= 2 * Math.PI) {
        heading -= 2 * Math.PI;
      }
    }
  }

  private void updateForces() {
    final double xAirResistanceForceNewton = -xSpeed * Math.abs(xSpeed) * AIR_DRAG_COEFFICIENT;
    final double yAirResistanceForceNewton = -ySpeed * Math.abs(ySpeed) * AIR_DRAG_COEFFICIENT;
    xForce = xAirResistanceForceNewton;
    yForce = yAirResistanceForceNewton;
  }

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

  public void move(final double timeDiff) {
    updateDynamicsFromInputs(timeDiff);
    updateForces();
    updateSpeed(timeDiff);
    x += xSpeed * timeDiff;
    y += ySpeed * timeDiff;
  }
}
