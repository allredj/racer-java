package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private final int ICAR_X = 100;
  private final int ICAR_Y = 100;
  private final int ICAR2_X = 200;
  private final int ICAR2_Y = 100;
  private final int IBOX_X = 600;
  private final int IBOX_Y = 600;
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

  boolean currentCollisionApplied = false;

  private void updateCars(final double timeDiff) {
    car.move(timeDiff);
    car2.move(timeDiff);
  }

  private void applyCollision() {
    if (currentCollisionApplied) {
      return;
    }
    // assume equal weight
    double meanXSpeed = (car.xSpeed + car2.xSpeed) / 2;
    double collisionXSpeed = car.xSpeed - car2.xSpeed;
    double carNewXSpeed = meanXSpeed - 0.3 * collisionXSpeed;
    double car2NewXSpeed = meanXSpeed + 0.3 * collisionXSpeed;
    car.xSpeed = carNewXSpeed;
    car2.xSpeed = car2NewXSpeed;

    double meanYSpeed = (car.ySpeed + car2.ySpeed) / 2;
    double collisionYSpeed = car.ySpeed - car2.ySpeed;
    double carNewYSpeed = meanYSpeed - 0.3 * collisionYSpeed;
    double car2NewYSpeed = meanYSpeed + 0.3 * collisionYSpeed;
    car.ySpeed = carNewYSpeed;
    car2.ySpeed = car2NewYSpeed;

    currentCollisionApplied = true;
  }

  private static final double WALL_ELASTICITY = 0.5;

  private void checkCollisions() {
    Rectangle carBounds = car.getBounds();
    // TODO force depends on mass and speed
    // FIXME Car should be updated between collisions to avoid multiple force application
    if (carBounds.intersects(northWall)) {
      car.ySpeed = WALL_ELASTICITY * Math.abs(car.ySpeed);
    }
    if (carBounds.intersects(southWall)) {
      car.ySpeed = -WALL_ELASTICITY * Math.abs(car.ySpeed);
    }
    if (carBounds.intersects(westWall)) {
      car.xSpeed = WALL_ELASTICITY * Math.abs(car.xSpeed);
    }
    if (carBounds.intersects(eastWall)) {
      car.xSpeed = -WALL_ELASTICITY * Math.abs(car.xSpeed);
    }
    if (carBounds.intersects(box.getBounds())) {
//      xForceForCar = 10000;
    }
    Rectangle car2Bounds = car2.getBounds();
    // TODO force depends on mass and speed
    // FIXME Car should be updated between collisions to avoid multiple force application
    if (car2Bounds.intersects(northWall)) {
      car2.ySpeed = WALL_ELASTICITY * Math.abs(car.ySpeed);
    }
    if (car2Bounds.intersects(southWall)) {
      car2.ySpeed = -WALL_ELASTICITY * Math.abs(car.ySpeed);
    }
    if (car2Bounds.intersects(westWall)) {
      car2.xSpeed = WALL_ELASTICITY * Math.abs(car.xSpeed);
    }
    if (car2Bounds.intersects(eastWall)) {
      car2.xSpeed = -WALL_ELASTICITY * Math.abs(car.xSpeed);
    }

    if (carBounds.intersects(car2Bounds)) {
      applyCollision();
    } else {
      currentCollisionApplied = false;
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
