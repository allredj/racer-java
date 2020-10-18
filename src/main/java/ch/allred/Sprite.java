package ch.allred;

import java.awt.Image;
import javax.swing.ImageIcon;

public class Sprite {
  protected float x;
  protected float y;
  protected int width;
  protected int height;
  protected boolean visible;
  protected Image image;

  public Sprite(int x, int y) {
    this.x = x;
    this.y = y;
    visible = true;
  }

  protected void loadImage(String imageName) {
    ImageIcon ii = new ImageIcon(imageName);
    image = ii.getImage();
  }

  protected void getImageDimensions() {
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
}
