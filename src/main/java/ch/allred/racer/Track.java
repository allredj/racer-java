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
  private final int IBOX_X = 200;
  private final int IBOX_Y = 250;
  private final int DELAY = 10;
  private Car car;
  private Box box;
  private Thread animatorThread;
  private final Rectangle northWall;
  private final Rectangle southWall;
  private final Rectangle westWall;
  private final Rectangle eastWall;
  private final static int boundingWallThickness = 30;

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
    car = new Car(ICAR_X, ICAR_Y);
    box = new Box(IBOX_X, IBOX_Y);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawWalls(g);
    drawCar(g);
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

  private void drawCar(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(car.getImage(), car.getAffineTransform(), this);
  }

  private void drawStats(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawString(car.infoString(), 10, 20);
  }

  private double xForceForCar = 0;
  private double yForceForCar = 0;

  private void updateCar(final double timeDiff) {
    car.move(timeDiff, xForceForCar, yForceForCar);
    // TODO update with reaction force
    xForceForCar = 0;
    yForceForCar = 0;
  }

  private void checkCollisions() {
    Rectangle carBounds = car.getBounds();
    // TODO force depends on mass and speed
    // FIXME Car should be updated between collisions to avoid multiple force application
    if (carBounds.intersects(northWall)) {
      yForceForCar = 10000;
    }
    if (carBounds.intersects(southWall)) {
      yForceForCar = -10000;
    }
    if (carBounds.intersects(westWall)) {
      xForceForCar = 10000;
    }
    if (carBounds.intersects(eastWall)) {
      xForceForCar = -10000;
    }
  }

  private class TAdapter extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent e) {
      car.keyReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
      car.keyPressed(e);
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
      updateCar((float) timeDiff / 1000);
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
