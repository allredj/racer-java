package ch.allred;

import java.awt.event.KeyEvent;

public class Car extends Sprite {

  // px/s
  private static final double MAX_FORWARD_SPEED = 300;
  private static final double MAX_BACKWARD_SPEED = -100;
  private static final double LOW_SPEED_FILTER = 0.6;

  // rad/s
  private static final double TURN_RATE = 4;

  // ratio to current speed
  private static final double COAST_DECELERATION_RATE = 0.01;

  // px/s
  public double getSpeed() {
    return speed;
  }

  // radians
  public double getDirection() {
    return direction;
  }

  private double speed;
  private double direction;
  private boolean accelerating;
  private boolean braking;
  private boolean turningRight;
  private boolean turningLeft;

  public Car(int x, int y) {
    super(x, y);
    initCar();
  }

  private void initCar() {
    loadImage("src/main/resources/car_red.png");
    getImageDimensions();
    speed = 0;
    direction = Math.PI / 2;
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
      speed = Math.min(speed + 2, MAX_FORWARD_SPEED);
    }
    if (braking) {
      speed = Math.max(speed - 2, MAX_BACKWARD_SPEED);
    }
    if (turningLeft) {
      direction = direction - TURN_RATE / 1000 * timeDiff;
      if (direction <= 0) {
        direction += 2 * Math.PI;
      }
    }
    if (turningRight) {
      direction = direction + TURN_RATE / 1000 * timeDiff;
      if (direction >= 2 * Math.PI) {
        direction -= 2 * Math.PI;
      }
    }
  }

  private void coastDecelerate(final double timeDiff) {
    speed *= (1 - COAST_DECELERATION_RATE);
    // low-filter
    if (speed > -LOW_SPEED_FILTER && speed < LOW_SPEED_FILTER) {
      speed = 0;
    }
  }

  public void move(final double timeDiff) {
    updateDynamicsFromInputs(timeDiff);
    coastDecelerate(timeDiff);
    final double dx = speed * Math.sin(direction) * timeDiff / 1000;
    final double dy = -speed * Math.cos(direction) * timeDiff / 1000;
    x += dx;
    y += dy;
  }
}
