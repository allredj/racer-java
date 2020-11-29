package ch.allred.racer;

public class MovingObject extends PhysicalObject {

  public double xSpeed;
  public double ySpeed;

  public MovingObject(final int x, final int y) {
    super(x, y);
  }

  public MovingObject(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }
}
