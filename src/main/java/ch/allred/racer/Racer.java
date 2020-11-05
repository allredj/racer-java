package ch.allred.racer;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Racer extends JFrame {

  private void initUI() {
    add(new Track());
    setTitle("Racer");
    setSize(800, 600);
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
