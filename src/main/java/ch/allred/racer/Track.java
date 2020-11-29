package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private final int ICAR_X = 100;
  private final int ICAR_Y = 100;
  private final int ICAR2_X = 200;
  private final int ICAR2_Y = 100;
  private final int IBOX_X = 300;
  private final int IBOX_Y = 300;
  private final int DELAY = 10;
  private Car car;
  private Car car2;
  private Box box;
  private Thread animatorThread;
  private final Wall northWall;
  private final Wall southWall;
  private final Wall westWall;
  private final Wall eastWall;
  private final static int boundingWallThickness = 30;
  private final static boolean DRAW_BOUNDING_BOXES = true;

  public Track(final int width, final int height) {
    initTrack();
    northWall = new Wall(0, 0, width, boundingWallThickness);
    southWall = new Wall(0, height - boundingWallThickness, width, boundingWallThickness);
    westWall = new Wall(0, 0, boundingWallThickness, height);
    eastWall = new Wall(width - boundingWallThickness, 0, boundingWallThickness, height);
  }

  private void initTrack() {
    addKeyListener(new TAdapter());
    setBackground(Color.GRAY);
    setFocusable(true);
    car = new Car(ICAR_X, ICAR_Y, 0);
    car2 = new Car(ICAR2_X, ICAR2_Y, 1);
    box = new Box(IBOX_X, IBOX_Y);
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
    g2d.fill(northWall.getBounds());
    g2d.fill(southWall.getBounds());
    g2d.fill(westWall.getBounds());
    g2d.fill(eastWall.getBounds());
  }

  private void drawBox(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.fill(box.getBounds());
  }

  private void drawCars(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(car.getImage(), car.getAffineTransform(), this);
    g2d.drawImage(car2.getImage(), car2.getAffineTransform(), this);

    if (DRAW_BOUNDING_BOXES) {
      g2d.draw(car.getBounds());
      g2d.draw(car2.getBounds());
    }
  }

  private void drawStats(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawString(car.infoString(), 40, 50);
  }

  private void updateCars(final double timeDiff) {
    car.update(timeDiff);
    car2.update(timeDiff);
    box.update(timeDiff);
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
    ArrayList<MovingObject> collidees = new ArrayList<>(); // TODO should be persistent
    collidees.add(car);
    collidees.add(car2);
    collidees.add(box);

    ArrayList<MovingObject> colliders = new ArrayList<>(); // TODO should be persistent
    colliders.add(car);
    colliders.add(car2);
    colliders.add(box);

    ArrayList<Wall> walls = new ArrayList<>(); // TODO should be persistent
    walls.add(northWall);
    walls.add(southWall);
    walls.add(westWall);
    walls.add(eastWall);

    // FIXME Too many collision checks (reflexivity)
    for (MovingObject collider : colliders) {
      for (MovingObject collidee : collidees) {
        if (collider.getBounds().intersects(collidee.getBounds())) {
          if (!collider.equals(collidee)) {
            applyCollision(collider, collidee);
          }
        }
      }
    }

    for (MovingObject collider : colliders) {
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
      car.keyReleased(e);
      car2.keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
      car.keyPressed(e);
      car2.keyPressed(e);
    }
  }

  @Override
  public void addNotify() {
    super.addNotify();
    animatorThread = new Thread(this);
    animatorThread.start();
  }

  @Override
  public void run() {
    long now, lastTime, timeDiff, sleep;

    lastTime = System.currentTimeMillis();

    while (true) {
      now = System.currentTimeMillis();
      timeDiff = now - lastTime;
      lastTime = now;
      checkCollisions();
      updateCars((float) timeDiff / 1000);
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
