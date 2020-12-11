package ch.allred.racer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackData {

  public final static int BOUNDING_WALL_THICKNESS = 30;
  private final static int CENTRE_WALL_THICKNESS = 300;
  private final static int CENTRE_WALL_LENGTH = 700;
  private final static int CENTRE_WALL_X_POSITION = 200;
  public final static int CENTRE_WALL_Y_POSITION = 200;

  private static final int FIRST_CAR_X = 200;
  private static final int FIRST_CAR_Y = 50;
  private static final int CAR_SPACING = 80;

  private static final int IBOX_X = 300;
  private static final int IBOX_Y = 300;


  public static int getCarX(final int carIndex) {
    return FIRST_CAR_X;
  }

  public static int getCarY(final int carIndex) {
    return FIRST_CAR_Y + CAR_SPACING * carIndex;
  }

  public static List<Wall> createWalls() {
    Wall northWall = new Wall(0, 0, Racer.TRACK_WIDTH, BOUNDING_WALL_THICKNESS);
    Wall southWall = new Wall(0, Racer.TRACK_HEIGHT - BOUNDING_WALL_THICKNESS, Racer.TRACK_WIDTH,
        BOUNDING_WALL_THICKNESS);
    Wall westWall = new Wall(0, 0, BOUNDING_WALL_THICKNESS, Racer.TRACK_HEIGHT);
    Wall eastWall = new Wall(Racer.TRACK_WIDTH - BOUNDING_WALL_THICKNESS, 0,
        BOUNDING_WALL_THICKNESS,
        Racer.TRACK_HEIGHT);
    Wall centreWall = new Wall(CENTRE_WALL_X_POSITION, CENTRE_WALL_Y_POSITION, CENTRE_WALL_LENGTH,
        CENTRE_WALL_THICKNESS);
    List<Wall> walls = new ArrayList<>();
    Collections.addAll(walls, northWall, southWall, westWall, eastWall, centreWall);
    return walls;
  }

  public static List<Car> cars() {
    List<Car> cars = new ArrayList<>();
    cars.add(Car.fromIndex(0));
    cars.add(Car.fromIndex(1));
    return cars;
  }

  public static List<Box> boxes() {
    List<Box> boxes = new ArrayList();
    boxes.add(new Box(IBOX_X, IBOX_Y));
    return boxes;
  }

}
