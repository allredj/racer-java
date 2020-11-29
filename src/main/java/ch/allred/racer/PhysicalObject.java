package ch.allred.racer;

public abstract class PhysicalObject extends Sprite {

  public PhysicalObject(final int x, final int y) {
    super(x, y);
  }

  public PhysicalObject(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }
}
