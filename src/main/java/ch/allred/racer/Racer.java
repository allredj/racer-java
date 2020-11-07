package ch.allred.racer;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Racer extends JFrame {

  private final static int TRACK_WIDTH = 800;
  private final static int TRACK_HEIGHT = 600;

  private void initUI() {
    add(new Track(TRACK_WIDTH, TRACK_HEIGHT));
    setTitle("Racer");
    setSize(TRACK_WIDTH, TRACK_HEIGHT);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public Racer() {
    initUI();
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {
      Racer ex = new Racer();
      ex.setVisible(true);
    });
  }

}
