package sk.actplus.slime.version2;

/**
 * Created by Ja on 17.2.2018.
 */

public class Angle {
    public static double toDeg(double rad) {
        return (180*rad)/Math.PI;
    }

    public static double toDeg(float rad) {
        return toDeg((double)rad);
    }

    public static double toRad(double angle) {
        return (angle*Math.PI)/180;
    }

    public static double toRad(float angle) {
        return toDeg((double)angle);
    }

}
