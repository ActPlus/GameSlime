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

    @Override
    public String toString() {
        return "Function(x) = slope * x + yAxisIntersection";
    }

    public float getSlope() {
        return slope;
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
            float x = function.yAxisIntesection-yAxisIntesection/slope-function.slope;
            float y = getSlope()*x+yAxisIntesection;
            return new Vector2(x, y);
        }

        return null;
    }


}
