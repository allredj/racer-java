package ch.allred.racer;

import java.awt.Rectangle;
import java.util.List;

public class CollisionManager {

  private static final double WALL_ELASTICITY = 0.5;

  /**
   * Move objects apart to make sure they remain disjoint by moving the first object away.
   */
  private static void deconflict(MovingObject object1, MovingObject object2) {
    Rectangle intersection = object1.getBounds().intersection(object2.getBounds());
    if (intersection.width < intersection.height) {
      object1.x = object1.x - Math.signum(object2.x - object1.x) * intersection.width;
    } else {
      object1.y = object1.y - Math.signum(object2.y - object1.y) * intersection.height;
    }
  }

  private static void applyCollision(MovingObject object1, MovingObject object2) {
    deconflict(object1, object2);

    // FIXME consider angle of collision, cf wall collision
    // assume equal weight
    double meanXSpeed = (object1.xSpeed + object2.xSpeed) / 2;
    double collisionXSpeed = object1.xSpeed - object2.xSpeed;
    double object1NewXSpeed = meanXSpeed - 0.5 * collisionXSpeed;
    double object2NewXSpeed = meanXSpeed + 0.5 * collisionXSpeed;
    object1.xSpeed = object1NewXSpeed;
    object2.xSpeed = object2NewXSpeed;

    double meanYSpeed = (object1.ySpeed + object2.ySpeed) / 2;
    double collisionYSpeed = object1.ySpeed - object2.ySpeed;
    double object1NewYSpeed = meanYSpeed - 0.5 * collisionYSpeed;
    double object2NewYSpeed = meanYSpeed + 0.5 * collisionYSpeed;
    object1.ySpeed = object1NewYSpeed;
    object2.ySpeed = object2NewYSpeed;
  }

  private static void applyCollision(MovingObject object1, Wall object2) {
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

  public static void checkAndApplyCollisions(List<MovingObject> movingObjects, List<Wall> walls) {
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

}
