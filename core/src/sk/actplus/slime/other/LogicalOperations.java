package sk.actplus.slime.other;

import com.badlogic.gdx.math.Vector2;

import java.awt.Point;

/**
 * Created by Ja on 8.4.2017.
 */

public class LogicalOperations {
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public static float getAngle(Vector2 target, Vector2 b) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - b.y, target.x - b.x));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}
