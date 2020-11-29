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
  private final Rectangle northWall;
  private final Rectangle southWall;
  private final Rectangle westWall;
  private final Rectangle eastWall;
  private final static int boundingWallThickness = 30;
  private final static boolean DRAW_BOUNDING_BOXES = true;

  public Track(final int width, final int height) {
    initTrack();
    northWall = new Rectangle(0, 0, width, boundingWallThickness);
    southWall = new Rectangle(0, height - boundingWallThickness, width, boundingWallThickness);
    westWall = new Rectangle(0, 0, boundingWallThickness, height);
    eastWall = new Rectangle(width - boundingWallThickness, 0, boundingWallThickness, height);
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
    g2d.fill(northWall);
    g2d.fill(southWall);
    g2d.fill(westWall);
    g2d.fill(eastWall);
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
    car.move(timeDiff);
    car2.move(timeDiff);
    box.move(timeDiff);
  }

  private static void applyCollision(MovingObject sprite1, MovingObject sprite2) {
    // ensure cars are disjoint
    Rectangle carBounds = sprite1.getBounds();
    Rectangle car2Bounds = sprite2.getBounds();
    Rectangle intersection = carBounds.intersection(car2Bounds);
    if (intersection.width < intersection.height) {
      sprite1.x = sprite1.x - Math.signum(sprite2.x - sprite1.x) * intersection.width;
      sprite2.x = sprite2.x + Math.signum(sprite2.x - sprite1.x) * intersection.width;
    } else {
      sprite1.y = sprite1.y - Math.signum(sprite2.y - sprite1.y) * intersection.height;
      sprite2.y = sprite2.y + Math.signum(sprite2.y - sprite1.y) * intersection.height;
    }

    // assume equal weight
    double meanXSpeed = (sprite1.xSpeed + sprite2.xSpeed) / 2;
    double collisionXSpeed = sprite1.xSpeed - sprite2.xSpeed;
    double carNewXSpeed = meanXSpeed - 0.3 * collisionXSpeed;
    double car2NewXSpeed = meanXSpeed + 0.3 * collisionXSpeed;
    sprite1.xSpeed = carNewXSpeed;
    sprite2.xSpeed = car2NewXSpeed;

    double meanYSpeed = (sprite1.ySpeed + sprite2.ySpeed) / 2;
    double collisionYSpeed = sprite1.ySpeed - sprite2.ySpeed;
    double carNewYSpeed = meanYSpeed - 0.3 * collisionYSpeed;
    double car2NewYSpeed = meanYSpeed + 0.3 * collisionYSpeed;
    sprite1.ySpeed = carNewYSpeed;
    sprite2.ySpeed = car2NewYSpeed;
  }

  private static final double WALL_ELASTICITY = 0.5;

  private void checkCollisions() {
    ArrayList<MovingObject> collidees = new ArrayList<>(); // TODO should be persistent
    collidees.add(car);
    collidees.add(car2);
    collidees.add(box);

    ArrayList<MovingObject> colliders = new ArrayList<>(); // TODO should be persistent
    colliders.add(car);
    colliders.add(car2);
    colliders.add(box);

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

    Rectangle carBounds = car.getBounds();
    // TODO force depends on mass and speed
    // FIXME Car should be updated between collisions to avoid multiple force application
    if (carBounds.intersects(northWall)) {
      car.ySpeed = WALL_ELASTICITY * Math.abs(car.ySpeed);
      car.y = car.y + carBounds.intersection(northWall).height;
    }
    if (carBounds.intersects(southWall)) {
      car.ySpeed = -WALL_ELASTICITY * Math.abs(car.ySpeed);
      car.y = car.y - carBounds.intersection(southWall).height;
    }
    if (carBounds.intersects(westWall)) {
      car.xSpeed = WALL_ELASTICITY * Math.abs(car.xSpeed);
      car.x = car.x + carBounds.intersection(westWall).width;
    }
    if (carBounds.intersects(eastWall)) {
      car.xSpeed = -WALL_ELASTICITY * Math.abs(car.xSpeed);
      car.x = car.x - carBounds.intersection(eastWall).width;
    }
    Rectangle car2Bounds = car2.getBounds();
    // TODO force depends on mass and speed
    // FIXME Car should be updated between collisions to avoid multiple force application
    if (car2Bounds.intersects(northWall)) {
      car2.ySpeed = WALL_ELASTICITY * Math.abs(car.ySpeed);
      car2.y = car2.y + car2Bounds.intersection(northWall).height;
    }
    if (car2Bounds.intersects(southWall)) {
      car2.ySpeed = -WALL_ELASTICITY * Math.abs(car.ySpeed);
      car2.y = car2.y - car2Bounds.intersection(southWall).height;
    }
    if (car2Bounds.intersects(westWall)) {
      car2.xSpeed = WALL_ELASTICITY * Math.abs(car.xSpeed);
      car2.x = car2.x + car2Bounds.intersection(westWall).width;
    }
    if (car2Bounds.intersects(eastWall)) {
      car2.xSpeed = -WALL_ELASTICITY * Math.abs(car.xSpeed);
      car2.x = car2.x - car2Bounds.intersection(eastWall).width;
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
