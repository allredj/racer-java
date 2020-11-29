package ch.allred.racer;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class Wall extends PhysicalObject {

  public Wall(final int x, final int y) {
    super(x, y);
  }

  public Wall(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  @Override
  public void draw(final Graphics2D g2d, final ImageObserver imageObserver) {
    g2d.fill(getBounds());
  }
}
