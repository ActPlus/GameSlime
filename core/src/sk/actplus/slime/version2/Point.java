package sk.actplus.slime.version2;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ja on 20.2.2018.
 */

public class Point {



    public static Vector2 getLeftPoint(Vector2 [] points) throws NullPointerException {
        Vector2 left = points[0];

        for (int i = 1; i < points.length; i++) {
            if (points[i].x<left.x)
                left = points[i];
        }
        return left;

    }

    public static Vector2 getRightPoint(Vector2 [] points) {
    Vector2 right = points[0];

        for (int i = 1; i < points.length; i++) {
            if (points[i].x>right.x)
                right = points[i];
        }
        return right;

    }

}
