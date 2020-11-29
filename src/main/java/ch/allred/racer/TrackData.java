package ch.allred.racer;

public class TrackData {

  private static final int FIRST_CAR_X = 200;
  private static final int FIRST_CAR_Y = 50;
  private static final int CAR_SPACING = 80;

  public static int getCarX(final int carIndex) {
    return FIRST_CAR_X;
  }

  public static int getCarY(final int carIndex) {
    return FIRST_CAR_Y + CAR_SPACING * carIndex;
  }

}
