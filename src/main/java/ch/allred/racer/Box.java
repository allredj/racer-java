package ch.allred.racer;

public class Box extends MovingObject {

  public static final int WIDTH = 20;
  public static final int HEIGHT = 20;
  private static final double AIR_DRAG_COEFFICIENT = 0.005;

  private double mass; // must be non-zero
  private double xForce;
  private double yForce;

  public Box(final int x, final int y) {
    super(x, y, WIDTH, HEIGHT);
    mass = 1;
  }

  protected void updateForces() {
    final double xAirResistanceForceNewton = -xSpeed * Math.abs(xSpeed) * AIR_DRAG_COEFFICIENT;
    final double yAirResistanceForceNewton = -ySpeed * Math.abs(ySpeed) * AIR_DRAG_COEFFICIENT;
    xForce = xAirResistanceForceNewton;
    yForce = yAirResistanceForceNewton;
  }

  protected void updateSpeed(final double timeDiff) {
    final double dXSpeed = xForce / mass * timeDiff;
    xSpeed += dXSpeed;
    final double dYSpeed = yForce / mass * timeDiff;
    ySpeed += dYSpeed;
  }

  public void move(final double timeDiff) {
    if (timeDiff == 0) {
      return;
    }

    updateSpeed(timeDiff);
    x += xSpeed * timeDiff;
    y += ySpeed * timeDiff;
  }

}
