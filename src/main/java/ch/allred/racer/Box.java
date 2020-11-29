package ch.allred.racer;

public class Box extends MovingObject {

  public static final int WIDTH = 20;
  public static final int HEIGHT = 20;
  private static final double FRICTION_COEFFICIENT = 100;

  private double mass; // must be non-zero
  private double xForce;
  private double yForce;

  public Box(final int x, final int y) {
    super(x, y, WIDTH, HEIGHT);
    mass = 1;
  }

  protected void updateForces() {
    // FIXME: Object heading must be stable
    final double xAirResistanceForceNewton = - Math.signum(xSpeed) * FRICTION_COEFFICIENT;
    final double yAirResistanceForceNewton = - Math.signum(ySpeed) * FRICTION_COEFFICIENT;
    xForce = xAirResistanceForceNewton;
    yForce = yAirResistanceForceNewton;
  }

  protected void updateSpeed(final double timeDiff) {
    final double dXSpeed = xForce / mass * timeDiff;
    xSpeed += dXSpeed;
    final double dYSpeed = yForce / mass * timeDiff;
    ySpeed += dYSpeed;
  }

  public void update(final double timeDiff) {
    if (timeDiff == 0) {
      return;
    }
    updateForces();
    updateSpeed(timeDiff);
    x += xSpeed * timeDiff;
    y += ySpeed * timeDiff;
  }

}
