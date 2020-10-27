package ch.allred;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Track extends JPanel implements Runnable {

  private final int ICAR_X = 40;
  private final int ICAR_Y = 60;
  private final int DELAY = 10;
  private Car car;
  private Thread animatorThread;

  public Track() {
    initTrack();
  }

  private void initTrack() {
    addKeyListener(new TAdapter());
    setBackground(Color.GRAY);
    setFocusable(true);
    car = new Car(ICAR_X, ICAR_Y);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawCar(g);
    drawStats(g);
    Toolkit.getDefaultToolkit().sync();
  }

  private void drawCar(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    AffineTransform affine = new AffineTransform();
    affine.translate(car.getX(), car.getY());
    // center image
    affine.rotate(car.getHeading(), (double) car.width / 2, (double) car.height / 2);
    g2d.drawImage(car.getImage(), affine, this);
  }

  private void drawStats(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.drawString(
        String.format("Speed:%f, Heading:%f", car.getSpeed(), car.getHeading()),
        10, 20);
  }

  private void updateCar(final double timeDiff) {
    car.move(timeDiff);
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
      updateCar(timeDiff);
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
