package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

public class TrackPaint extends Sprite {

  public TrackPaint(final int x, final int y, final int width, final int height) {
    super(x, y, width, height);
  }

  @Override
  public void draw(final Graphics2D g2d, ImageObserver imageObserver) {
    g2d.setColor(Color.WHITE);
    g2d.fill(getBounds());
    g2d.setColor(Color.DARK_GRAY);
  }
}
