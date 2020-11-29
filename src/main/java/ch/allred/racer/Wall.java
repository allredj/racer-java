package ch.allred.racer;

public class Wall extends PhysicalObject {

  public Wall(final int x, final int y) {
    super(x, y);
  }

  public Wall(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }
}
