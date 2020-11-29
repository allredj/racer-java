package ch.allred.racer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public abstract class Sprite {
  protected double x;
  protected double y;
  protected int width;
  protected int height;
  protected boolean visible;
  protected Image image;

  public Sprite(int x, int y) {
    this.x = x;
    this.y = y;
    visible = true;
  }

  public Sprite(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    visible = true;
  }

  protected void loadImage(String imageName) {
    ImageIcon ii = new ImageIcon(imageName);
    image = ii.getImage();
    width = image.getWidth(null);
    height = image.getHeight(null);
  }

  public Image getImage() {
    return image;
  }

  public int getX() {
    return (int) x;
  }

  public int getY() {
    return (int) y;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public Rectangle getBounds() {
    return new Rectangle((int) x, (int) y, width, height);
  }

  public abstract void draw (Graphics2D g2d, ImageObserver imageObserver);
}
