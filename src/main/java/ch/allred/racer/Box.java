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
    // Lower bound to both model static friction and to avoid division by 0.
    final double speed = Math.max(1, Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed));
    final double xTrackFrictionForce = - FRICTION_COEFFICIENT * xSpeed / speed;
    final double yTrackFrictionForce = - FRICTION_COEFFICIENT * ySpeed / speed;
    xForce = xTrackFrictionForce;
    yForce = yTrackFrictionForce;
  }

  // apply forces
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
