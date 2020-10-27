package ch.allred;

import java.awt.event.KeyEvent;

public class Car extends Sprite {

  // px/s
  private static final double LOW_SPEED_FILTER = 0.6;

  // rad/s
  private static final double TURN_RATE = 4;

  // ratio to current speed
  private static final double COAST_DECELERATION_RATE = 1;

  // px/s
  public double getSpeed() {
    return speed;
  }

  // radians
  public double getHeading() {
    return heading;
  }

  private double speed;
  private double heading;
  private boolean accelerating;
  private boolean braking;
  private boolean turningRight;
  private boolean turningLeft;
  private double xSpeed;
  private double ySpeed;

  public Car(int x, int y) {
    super(x, y);
    initCar();
  }

  private void initCar() {
    loadImage("src/main/resources/car_red.png");
    getImageDimensions();
    speed = 0;
    xSpeed = 0;
    ySpeed = 0;
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
      heading = heading - TURN_RATE / 1000 * timeDiff;
      if (heading <= 0) {
        heading += 2 * Math.PI;
      }
    }
    if (turningRight) {
      heading = heading + TURN_RATE / 1000 * timeDiff;
      if (heading >= 2 * Math.PI) {
        heading -= 2 * Math.PI;
      }
    }
  }

  private void coastDecelerate(final double timeDiff) {
    xSpeed *= (1 - COAST_DECELERATION_RATE * timeDiff / 1000);
    // low-filter
    if (xSpeed > -LOW_SPEED_FILTER && xSpeed < LOW_SPEED_FILTER) {
      xSpeed = 0;
    }
    ySpeed *= (1 - COAST_DECELERATION_RATE * timeDiff / 1000);
    // low-filter
    if (ySpeed > -LOW_SPEED_FILTER && ySpeed < LOW_SPEED_FILTER) {
      ySpeed = 0;
    }
  }

  public void move(final double timeDiff) {
    updateDynamicsFromInputs(timeDiff);
    coastDecelerate(timeDiff);
    final double dx = xSpeed * timeDiff / 1000;
    final double dy = ySpeed * timeDiff / 1000;
    x += dx;
    y += dy;
  }
}
