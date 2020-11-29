package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private final int ICAR_X = 100;
  private final int ICAR_Y = 100;
  private final int ICAR2_X = 200;
  private final int ICAR2_Y = 100;
  private final int IBOX_X = 300;
  private final int IBOX_Y = 300;
  private final static int CENTRE_WALL_X_POSITION = 200;
  private final static int CENTRE_WALL_Y_POSITION = 200;
  private final int DELAY = 10;
  private Box box;
  private List<Car> cars;
  private List<MovingObject> movingObjects;
  private List<Wall> walls;
  private final static int boundingWallThickness = 30;
  private final static int CENTRE_WALL_THICKNESS = 300;
  private final static int CENTRE_WALL_LENGTH = 700;
  private final static boolean DRAW_BOUNDING_BOXES = true;

  public Track(final int width, final int height) {
    // TODO: separate init from construction
    initTrack();
    Wall northWall = new Wall(0, 0, width, boundingWallThickness);
    Wall southWall = new Wall(0, height - boundingWallThickness, width, boundingWallThickness);
    Wall westWall = new Wall(0, 0, boundingWallThickness, height);
    Wall eastWall = new Wall(width - boundingWallThickness, 0, boundingWallThickness, height);
    Wall centreWall = new Wall(CENTRE_WALL_X_POSITION, CENTRE_WALL_Y_POSITION, CENTRE_WALL_LENGTH,
        CENTRE_WALL_THICKNESS);
    walls = new ArrayList<>();
    walls.add(northWall);
    walls.add(southWall);
    walls.add(westWall);
    walls.add(eastWall);
    walls.add(centreWall);
  }

  private void initTrack() {
    addKeyListener(new TAdapter());
    setBackground(Color.GRAY);
    setFocusable(true);
    cars = new ArrayList<>();
    Car car1 = new Car(ICAR_X, ICAR_Y, 0);
    Car car2 = new Car(ICAR2_X, ICAR2_Y, 1);
    cars.add(car1);
    cars.add(car2);
    movingObjects = new ArrayList<>();
    movingObjects.add(car1);
    movingObjects.add(car2);
    box = new Box(IBOX_X, IBOX_Y);
    movingObjects.add(box);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawWalls(g);
    drawCars(g);
    drawBox(g);
    drawStats(g);
    Toolkit.getDefaultToolkit().sync();
  }

  private void drawWalls(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    walls.stream().forEach(wall -> g2d.fill(wall.getBounds()));
  }

  private void drawBox(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.fill(box.getBounds());
  }

  private void drawCars(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    cars.stream().forEach(car -> g2d.drawImage(car.getImage(), car.getAffineTransform(), this));

    if (DRAW_BOUNDING_BOXES) {
      cars.stream().forEach(car -> g2d.draw(car.getBounds()));
    }
  }

  private void drawStats(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawString(cars.get(0).infoString(), 40, 50);
  }

  private static void applyCollision(MovingObject object1, MovingObject object2) {
    // ensure cars are disjoint
    Rectangle carBounds = object1.getBounds();
    Rectangle car2Bounds = object2.getBounds();
    Rectangle intersection = carBounds.intersection(car2Bounds);
    if (intersection.width < intersection.height) {
      object1.x = object1.x - Math.signum(object2.x - object1.x) * intersection.width;
    } else {
      object1.y = object1.y - Math.signum(object2.y - object1.y) * intersection.height;
    }

    // FIXME consider angle of collision, cf wall collision
    // assume equal weight
    double meanXSpeed = (object1.xSpeed + object2.xSpeed) / 2;
    double collisionXSpeed = object1.xSpeed - object2.xSpeed;
    double carNewXSpeed = meanXSpeed - 0.3 * collisionXSpeed;
    double car2NewXSpeed = meanXSpeed + 0.3 * collisionXSpeed;
    object1.xSpeed = carNewXSpeed;
    object2.xSpeed = car2NewXSpeed;

    double meanYSpeed = (object1.ySpeed + object2.ySpeed) / 2;
    double collisionYSpeed = object1.ySpeed - object2.ySpeed;
    double carNewYSpeed = meanYSpeed - 0.3 * collisionYSpeed;
    double car2NewYSpeed = meanYSpeed + 0.3 * collisionYSpeed;
    object1.ySpeed = carNewYSpeed;
    object2.ySpeed = car2NewYSpeed;
  }

  private static final double WALL_ELASTICITY = 0.5;

  private static void applyCollision(MovingObject object1, Wall object2) {
    // ensure cars are disjoint
    Rectangle carBounds = object1.getBounds();
    Rectangle car2Bounds = object2.getBounds();
    Rectangle intersection = carBounds.intersection(car2Bounds);
    if (intersection.width < intersection.height) {
      // horizontal collision
      object1.x = object1.x - Math.signum(object2.x - object1.x) * intersection.width;
      object1.xSpeed = -WALL_ELASTICITY * object1.xSpeed;
    } else {
      // vertical collision
      object1.y = object1.y - Math.signum(object2.y - object1.y) * intersection.height;
      object1.ySpeed = -WALL_ELASTICITY * object1.ySpeed;
    }

  }

  private void checkCollisions() {
    // FIXME Too many collision checks (reflexivity)
    for (MovingObject collider : movingObjects) {
      for (MovingObject collidee : movingObjects) {
        if (collider.getBounds().intersects(collidee.getBounds())) {
          if (!collider.equals(collidee)) {
            applyCollision(collider, collidee);
          }
        }
      }
    }

    for (MovingObject collider : movingObjects) {
      for (Wall wall : walls) {
        if (collider.getBounds().intersects(wall.getBounds())) {
          applyCollision(collider, wall);
        }
      }
    }
  }

  private class TAdapter extends KeyAdapter {
    @Override
    public void keyReleased(KeyEvent e) {
      cars.stream().forEach(car -> car.keyReleased(e));
    }
    @Override
    public void keyPressed(KeyEvent e) {
      cars.stream().forEach(car -> car.keyPressed(e));
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    final Thread animatorThread = new Thread(this);
    animatorThread.start();
  }

  @Override
  public void run() {
    long now, lastTime, sleep;

    lastTime = System.currentTimeMillis();

    while (true) {
      now = System.currentTimeMillis();
      final long timeDiff = now - lastTime;
      lastTime = now;
      checkCollisions();
      movingObjects.stream().forEach(obj -> obj.update((float) timeDiff / 1000));
      repaint();
      sleep = DELAY - timeDiff;
      if (sleep < 0) {
        sleep = 2;
      }
      try {
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        String msg = String.format("Thread interrupted: %s", e.getMessage());
        JOptionPane.showMessageDialog(this, msg, "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
