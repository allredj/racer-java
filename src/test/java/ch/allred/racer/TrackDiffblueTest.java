package ch.allred.racer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

public class TrackDiffblueTest {
  @Test
  public void testConstructor() {
    // Arrange, Act and Assert
    assertFalse((new Track(1, 1)).currentCollisionApplied);
  }
}

