package ch.allred.racer;

public abstract class MovingObject extends PhysicalObject {

  protected double mass; // must be non-zero
  public double xSpeed;
  public double ySpeed;
  protected double xForce;
  protected double yForce;

  public MovingObject(final int x, final int y) {
    super(x, y);
  }

  public MovingObject(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  public abstract void update(final double timeDiff);
}
