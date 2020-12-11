package ch.allred.racer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private static final int DELAY = 10;
  private final List<Car> cars;
  private final List<MovingObject> movingObjects;
  private final List<Wall> walls;
  private final List<TrackPaint> trackPaints;

  public Track() {
    cars = new ArrayList<>();
    movingObjects = new ArrayList<>();
    walls = new ArrayList<>();
    trackPaints = new ArrayList<>();
    initTrack();
  }

  private void initTrack() {
    addKeyListener(new TAdapter());
    setBackground(Color.GRAY);
    setFocusable(true);
    walls.addAll(TrackData.createWalls());
    trackPaints.addAll(TrackData.createTrackPaints());
    cars.addAll(TrackData.createCars());
    movingObjects.addAll(cars);
    movingObjects.addAll(TrackData.createBoxes());
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    walls.forEach(wall -> wall.draw(g2d, this));
    trackPaints.forEach(paint -> paint.draw(g2d, this));
    movingObjects.forEach(obj -> obj.draw(g2d, this));
    drawStats(g2d);
    Toolkit.getDefaultToolkit().sync();
  }

  private void drawStats(Graphics2D g2d) {
    g2d.drawString(cars.get(0).infoString(), 40, 50);
  }

  private void checkCollisions() {
    // FIXME Too many collision checks (reflexivity)
    for (MovingObject collider : movingObjects) {
      for (MovingObject collidee : movingObjects) {
        if (collider.getBounds().intersects(collidee.getBounds())) {
          if (!collider.equals(collidee)) {
            CollisionManager.applyCollision(collider, collidee);
          }
        }
      }
    }

    for (MovingObject collider : movingObjects) {
      for (Wall wall : walls) {
        if (collider.getBounds().intersects(wall.getBounds())) {
          CollisionManager.applyCollision(collider, wall);
        }
      }
    }
  }

  private class TAdapter extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent e) {
      cars.forEach(car -> car.keyReleased(e));
    }

    @Override
    public void keyPressed(KeyEvent e) {
      cars.forEach(car -> car.keyPressed(e));
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
      movingObjects.forEach(obj -> obj.update((float) timeDiff / 1000));
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
