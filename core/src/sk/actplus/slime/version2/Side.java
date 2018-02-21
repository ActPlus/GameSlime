package sk.actplus.slime.version2;

import com.badlogic.gdx.math.Vector2;

import java.awt.Rectangle;

/**
 * Created by Ja on 19.2.2018.
 */

public class Side extends Function {
    protected Vector2 A;

    public Vector2 getA() {
        return A;
    }

    public Vector2 getB() {
        return B;
    }

    protected Vector2 B;
    private float x;



    private boolean function;

    public Side(float slope, float yAxisIntersection) {
        super(slope, yAxisIntersection);
        function=true;

    }

    public Side(Vector2 A, Vector2 B) {
        super(getSlope(A,B),getYAxisIntersection(A,getSlope(A,B)));
        this.A=A.cpy();
        this.B=B.cpy();
        function = checkIfFunction(A,B);
        if(!function) x = A.x;
    }

    public Side(Vector2 [] vertex) {
        this(vertex[0],vertex[1]);
    }

    public boolean isFunction() {
        return function;
    }

    private boolean checkIfFunction(Vector2 A, Vector2 B){
        return (B.x == A.x)?false:true;
    }

    private static float getYAxisIntersection(Vector2 point, float slope) {
        return point.y-slope*point.x;
    }

    public static float getSlope(Vector2 A, Vector2 B) throws IllegalArgumentException{
        float deltaX = B.x-A.x;

        return (B.y-A.y) / deltaX;
    }

    @Override
    public String toString() {
        if(isFunction())
            return super.toString();
                    return "x = " + x;
    }

    public float getX() {
        return x;
    }

    private boolean isOnInterval(Vector2 point, boolean openInterval){
        float minX = Math.min(A.x,B.x);
        float maxX = Math.max(A.x,B.x);

        if (openInterval) {
            if ((point.x > minX) && (point.x < maxX)) {
                float minY = Math.min(A.y,B.y);
                float maxY =Math.max(A.y,B.y);
                if ((point.y > minY) && (point.y < maxY))
                    return true;
            }

        } else {
            if ((point.x >= minX) && (point.x <= maxX)) {
                float minY = Math.min(A.y,B.y);
                float maxY =Math.max(A.y,B.y);
                if ((point.y >= minY) && (point.y <= maxY))
                    return true;
            }
        }

        return false;
    }

    private Vector2 getDownLimit(Vector2[] vertex) {
        float [] xVals = new float[2];
        float [] yVals = new float[2];
        xVals[0] = vertex[0].x;
        xVals[1] = vertex[1].x;
        yVals[0] = vertex[0].y;
        yVals[1] = vertex[1].y;

        return  new Vector2(MyMath.getSmallestNumber(xVals),MyMath.getSmallestNumber(yVals));
    }

    private Vector2 getUpLimit(Vector2[] vertex) {
        float [] xVals = new float[2];
        float [] yVals = new float[2];
        xVals[0] = vertex[0].x;
        xVals[1] = vertex[1].x;
        yVals[0] = vertex[0].y;
        yVals[1] = vertex[1].y;

        return  new Vector2(MyMath.getBiggestNumber(xVals),MyMath.getBiggestNumber(yVals));
    }


    public boolean contains(Vector2 point, boolean openInterval) {
        if (isOnInterval(point,openInterval)){
            return super.contains(point);
        }

        return false;
    }
    public boolean isIntersecting(Side side, boolean openInterval) {
        System.out.println("Is " + toString() +  " intersectiong " + side.toString() +" ?");

        System.out.println("first function A" + getA() ) ;
        System.out.println("first function B" + getB() ) ;
        System.out.println("---------------------------------");
        System.out.println("second function A" + side.getA() ) ;
        System.out.println("second function B" + side.getB() ) ;
        System.out.println("##################################");




        Vector2 intersection = getIntersectionPoint(side);
        if(intersection!=null){
            System.out.println("intersection point at: " + intersection);
            if(contains(intersection,openInterval)&&side.contains(intersection,openInterval)) {
                System.out.println("Intersection is on Interval");
                for (int i = 0; i < 10; i++) {
                    System.err.println("#############---------#############");
                }
                return true;
            }
        }
        System.out.println("no intersection");
        return false;
    }

    public Vector2 getIntersectionPoint(Side side) {

        float x;
        float y;
        Vector2 intersection = null;

        if((this.function)&&(side.isFunction())){
            return super.getIntersection(side);
        } else if(!this.function&&side.isFunction()) {
            x=this.x;
            y=side.slope*x+side.yAxisIntesection;
            intersection = new Vector2(x,y);
            System.out.println("shit");
        } else if(this.function&&!side.isFunction()) {
            x=side.getX();
            y=slope*x+yAxisIntesection;
            intersection = new Vector2(x,y);
            System.out.println("shit2");
        } else if(!this.function&&!side.isFunction()) {
            if(getX() == side.getX())
                //todo good intersection to all
            intersection = new Vector2(getX(),0);
            System.out.println("shit3");
        }


        return intersection;
    }
}
