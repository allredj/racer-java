package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import org.junit.jupiter.api.Test;

public class SpriteDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange and Act
    Sprite actualSprite = new Sprite(2, 3);

    // Assert
    assertTrue(actualSprite.isVisible());
    assertEquals(2.0, actualSprite.x);
    assertEquals(3.0, actualSprite.y);
  }

  @Test
  public void testLoadImage() {
    // Arrange
    Sprite sprite = new Sprite(2, 3);

    // Act
    sprite.loadImage("Image Name");

    // Assert
    Image expectedImage = sprite.image;
    assertSame(expectedImage, sprite.getImage());
  }

  @Test
  public void testGetImageDimensions() {
    // Arrange
    Sprite sprite = new Sprite(2, 3);
    sprite.loadImage("Image Name");

    // Act
    sprite.getImageDimensions();

    // Assert
    assertEquals(-1, sprite.height);
    assertEquals(-1, sprite.width);
  }

  @Test
  public void testGetX() {
    // Arrange, Act and Assert
    assertEquals(2, (new Sprite(2, 3)).getX());
  }

  @Test
  public void testGetY() {
    // Arrange, Act and Assert
    assertEquals(3, (new Sprite(2, 3)).getY());
  }

  @Test
  public void testSetVisible() {
    // Arrange
    Sprite sprite = new Sprite(2, 3);

    // Act
    sprite.setVisible(true);

    // Assert
    assertTrue(sprite.isVisible());
  }
}

