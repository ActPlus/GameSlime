package sk.actplus.slime.version2;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Ja on 19.2.2018.
 */

public class Function {
    protected float slope;
    protected float yAxisIntesection;


    public Function(float slope, float yAxisIntesection) {
        this.slope = slope;
        this.yAxisIntesection = yAxisIntesection;
    }

    public Function(Vector2 A, Vector2 B) {
        this.slope = getSlope(A,B);
        this.yAxisIntesection = getYAxisIntersection(A,slope);
    }

    public static float getYAxisIntersection(Vector2 point, float slope) {
        return point.y-slope*point.x;
    }

    public static float getSlope(Vector2 A, Vector2 B) throws IllegalArgumentException{
        float deltaX = B.x-A.x;

        return (B.y-A.y) / deltaX;
    }



    @Override
    public String toString() {
        return "f(x) = "+ String.format(java.util.Locale.US,"%.2f", slope) + " * x " + String.format(java.util.Locale.US,"%.2f", yAxisIntesection);
    }

    public float getSlope() {
        return slope;
    }
    public float getAngle() {
        return (float)((180*slope)/Math.PI);
    }

    public void setSlope(float slope) {
        this.slope = slope;
    }

    public float getyAxisIntesection() {
        return yAxisIntesection;
    }

    public void setyAxisIntesection(float yAxisIntesection) {
        this.yAxisIntesection = yAxisIntesection;
    }
    /***
     * 0 --> pod
     * 1 --> nad
     * -1 --> na
     */
    public int isAbovePoint(Vector2 point) {
        if(point.y>getSlope()*point.x+getyAxisIntesection()) {
            return 1;
        } else if(point.y<getSlope()*point.x+getyAxisIntesection()) {
            return 0;
        }

        return -1; //na funkcii
    }

    public boolean contains(Vector2  point) {
        if(point.y == slope*point.x + yAxisIntesection) return true;
        return false;
    }

    public boolean canIntersect(Function function) {
        if((slope-function.slope) != 0) {
            return true;
        }

        return false;
    }

    /***
     *
     * @param function
     * //null means no intersection
     * @return
     */
    public Vector2 getIntersection(Function function) {
        if((slope-function.slope) != 0) {
            float x = (function.yAxisIntesection-yAxisIntesection)/(slope-function.slope);
            float y = getSlope()*x+yAxisIntesection;
            return new Vector2(x, y);
        }

        return null;
    }


}
