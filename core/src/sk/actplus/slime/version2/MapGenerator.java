package sk.actplus.slime.version2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import sk.actplus.slime.version2.entities.Triangle;

/**
 * Created by Ja on 17.2.2018.
 */

class MapGenerator {
    Random rand;
    protected World world;
    protected OrthographicCamera camera;
    protected Vector2 transition;
    protected int numOfFails;
    protected Triangle last;

    public static final float MAX_RADIUS = 2.5f;

    public MapGenerator(World world, OrthographicCamera camera,Vector2[] startingEdge, Vector2 C) {
        this.world = world;
        this.camera = camera;
        rand = new Random();
        numOfFails = 0;




        last = new Triangle(world,new Vector2[]{startingEdge[0],startingEdge[1],C},camera);
        generate(last);
    }


    public Triangle generate(Triangle last) {
        Triangle tri;
        Vector2[] newShared = new Vector2[2];
        Vector2 newC;

        newShared[0]= last.getSharedSide()[getRandomIdx()];
        newShared[1]= last.getC();

        do {

            newC = getRandomPoint(0, last, newShared);
            tri = new Triangle(world, new Vector2[]{newShared[0], newShared[1], newC}, camera);
        } while(!isColliding(tri));


        Vector2 tempTransition = new Vector2(tri.getC().x-last.getC().x,tri.getC().y-last.getC().y);

        if(tempTransition.x<0){
            transition = new Vector2(tempTransition.x+transition.x,tempTransition.y);
        } else {
            transition = tempTransition;
        }

        setLast(tri);

        return tri;
    }

    public boolean isPointAbove(float slope, Vector2 point){
        if(point.y>slope*point.x) {
            return true;
        }
        return false;
    }

    public float getSlope(Vector2[] vec) {
        return (vec[0].y-vec[1].y)/(vec[0].x-vec[1].x);
    }

    public float getAngleFromSlope(float slope) {
        return (float)Math.tan(slope);
    }

    public Vector2 getCenterPoint(Vector2[] vects) {
        int n;
        float dx=0;
        float dy=0;

        for (n = 0; n < vects.length; n++) {
            dx+=vects[n].x;
            dy+=vects[n].y;
        }
        return new Vector2(dx/n,dy/n);
    }

    public Vector2 getRandomPoint(float limitX, Triangle last, Vector2[] newShared){


        float radius;
        float slope = getSlope(newShared);
        Vector2 center = getCenterPoint(newShared);
        int offset = 180;
        Angle angle;

        do {
            radius = rand.nextFloat() * MAX_RADIUS;
            angle = new Angle(rand.nextFloat() * 180 + getAngleFromSlope(slope));

            if (isPointAbove(getSlope(last.getSharedSide()), last.getC())) {
                offset = -offset;
            }

            angle.setDeg(angle.getDeg() + offset);
        } while((Math.cos(angle.getDeg()*radius)+center.x)>limitX);

        Vector2 point = new Vector2((float)(Math.cos(angle.getDeg()*radius)+center.x),(float)(Math.sin(angle.getDeg()*radius))+center.y);

        //TODO: IMPLEMENT no circular motion


        if(getDeltaX()<0){
            //point.y =
        }

        return point;

    }

    public boolean  isReturning() {
        if(getDeltaX()<0) {
            return true;
        }
        return false;
    }

    public boolean isColliding(Triangle newTriangle) {
        return true;
    }

    public float getDeltaX() {
        return transition.x;
    }

    public int getDirectionY() {
        return (int)Math.signum(transition.y);
    }

    public int getRandomIdx() {
        return rand.nextInt(2);
    }

    public int getOtherVertexIdx(int tried) {
        return (tried==1)?0:1;
    }

    public void setLast(Triangle tri) {
        last =tri;
    }

}
