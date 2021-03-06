package ch.allred.racer;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Racer extends JFrame {

  public final static int TRACK_WIDTH = 1100;
  public final static int TRACK_HEIGHT = 700;
  private final static int BOTTOM_MARGIN = 28;

  private void initUI() {
    add(new Track());
    setTitle("Racer");
    setSize(TRACK_WIDTH, TRACK_HEIGHT + BOTTOM_MARGIN);
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
