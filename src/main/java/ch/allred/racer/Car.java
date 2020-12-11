package ch.allred.racer;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

public class Car extends MovingObject {

  // rad/s
  private static final double TURN_RATE = 4;

  private static final double AIR_DRAG_COEFFICIENT = 0.01;

  private static final double TYRE_TRACTION_COEFFICIENT = 800;
  private final static boolean DRAW_BOUNDING_BOXES = true;

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

  private int forwardKeyCode = KeyEvent.VK_UP;
  private int backwardKeyCode = KeyEvent.VK_DOWN;
  private int leftKeyCode = KeyEvent.VK_LEFT;
  private int rightKeyCode = KeyEvent.VK_RIGHT;

  public Car(int x, int y) {
    super(x, y);
    initCar(0);
  }

  @Override
  public void draw(final Graphics2D g2d, ImageObserver imageObserver) {
    g2d.drawImage(getImage(), getAffineTransform(), imageObserver);

    if (DRAW_BOUNDING_BOXES) {
      g2d.draw(getBounds());
    }
  }

  public Car(int x, int y, int carIndex) {
    super(x, y);
    initCar(carIndex);
    if (carIndex == 1) {
      forwardKeyCode = KeyEvent.VK_W;
      backwardKeyCode = KeyEvent.VK_S;
      leftKeyCode = KeyEvent.VK_A;
      rightKeyCode = KeyEvent.VK_D;
    }
  }

  public static Car fromIndex(int carIndex) {
    return new Car(TrackData.getCarX(carIndex), TrackData.getCarY(carIndex), carIndex);
  }

  private void initCar(int carIndex) {
    if (carIndex == 1) {
      loadImage("src/main/resources/car_blue.png");
    } else {
      loadImage("src/main/resources/car_red.png");
    }
    indicatedSpeed = 0;
    xSpeed = 0;
    ySpeed = 0;
    xForce = 0;
    yForce = 0;
    mass = 4;
    xHeading = 1;
    yHeading = 0;
  }

  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == leftKeyCode) {
      turningLeft = true;
    }
    if (key == rightKeyCode) {
      turningRight = true;
    }
    if (key == forwardKeyCode) {
      accelerating = true;
    }
    if (key == backwardKeyCode) {
      braking = true;
    }
  }

  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
    if (key == leftKeyCode) {
      turningLeft = false;
    }
    if (key == rightKeyCode) {
      turningRight = false;
    }
    if (key == forwardKeyCode) {
      accelerating = false;
    }
    if (key == backwardKeyCode) {
      braking = false;
    }
  }

  private void updateDynamicsFromInputs(final double timeDiff) {
    if (accelerating) {
      // FIXME: Decouple acceleration from frame rate
      // TODO: Extract magic number
      xSpeed += 3 * xHeading;
      ySpeed += 3 * yHeading;
    }
    if (braking) {
      xSpeed -= 2 * xHeading;
      ySpeed -= 2 * yHeading;
    }
    if (turningLeft) {
      // delta must be acute
      final double delta = -TURN_RATE * timeDiff;
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

  static double signedAngleBetweenVectors(double u1, double u2, double v1, double v2) {
    return Math.atan2(u2, u1) - Math.atan2(v2, v1);
  }

  static double lateralTyreForce(double xHeading, double yHeading, double xSpeed, double ySpeed) {
    if (xSpeed - xHeading == 0) {
      return 0;
    }
    return TYRE_TRACTION_COEFFICIENT * Math
        .sin(signedAngleBetweenVectors(xHeading, yHeading, xSpeed, ySpeed));
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
    final double dYSpeed = yForce / mass * timeDiff;
    ySpeed += dYSpeed;
    indicatedSpeed = Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
  }

  // pre-condition: non-zero
  @Override
  public void update(final double timeDiff) {
    if (timeDiff == 0) {
      return;
    }

    updateDynamicsFromInputs(timeDiff);
    updateForces();
    updateSpeed(timeDiff);
    x += xSpeed * timeDiff;
    y += ySpeed * timeDiff;
  }

  public AffineTransform getAffineTransform() {
    AffineTransform affine = new AffineTransform();
    affine.translate(x, y);
    // center image
    affine.rotate(getHeading(), (double) width / 2, (double) height / 2);
    return affine;
  }

}
