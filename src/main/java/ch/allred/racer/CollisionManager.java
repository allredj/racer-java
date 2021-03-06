package ch.allred.racer;

import java.awt.Rectangle;
import java.util.List;

public class CollisionManager {

  private static final double COLLISION_ELASTICITY = 0.5;

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
    final double massRatioObject1 = object1.mass / (object1.mass + object2.mass);
    final double massRatioObject2 = object2.mass / (object1.mass + object2.mass);

    final double xDistance = object2.x - object1.x;
    final double yDistance = object2.y - object1.y;
    final double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    final double xDistanceUnit = distance == 0 ? 0 : xDistance / distance;
    final double yDistanceUnit = distance == 0 ? 0 : yDistance / distance;

    final double meanXSpeed = (object1.xSpeed + object2.xSpeed) / 2;
    final double meanYSpeed = (object1.ySpeed + object2.ySpeed) / 2;
    final double collisionXSpeed = object1.xSpeed - object2.xSpeed;
    final double collisionYSpeed = object1.ySpeed - object2.ySpeed;

    final double xResultant =
        (collisionXSpeed + xDistanceUnit * Math.abs(collisionYSpeed)) * COLLISION_ELASTICITY;
    object1.xSpeed = meanXSpeed - massRatioObject2 * xResultant;
    object2.xSpeed = meanXSpeed + massRatioObject1 * xResultant;

    final double yResultant =
        (collisionYSpeed + yDistanceUnit * Math.abs(collisionXSpeed)) * COLLISION_ELASTICITY;
    object1.ySpeed = meanYSpeed - massRatioObject2 * yResultant;
    object2.ySpeed = meanYSpeed + massRatioObject1 * yResultant;

    deconflict(object1, object2);
  }

  private static void applyCollision(MovingObject object1, Wall object2) {
    Rectangle carBounds = object1.getBounds();
    Rectangle car2Bounds = object2.getBounds();
    Rectangle intersection = carBounds.intersection(car2Bounds);
    if (intersection.width < intersection.height) {
      // horizontal collision
      object1.x = object1.x - Math.signum(object2.x - object1.x) * intersection.width;
      object1.xSpeed = -COLLISION_ELASTICITY * object1.xSpeed;
    } else {
      // vertical collision
      object1.y = object1.y - Math.signum(object2.y - object1.y) * intersection.height;
      object1.ySpeed = -COLLISION_ELASTICITY * object1.ySpeed;
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
