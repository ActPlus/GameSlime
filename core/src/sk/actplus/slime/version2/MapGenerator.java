package sk.actplus.slime.version2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import sk.actplus.slime.version2.entity.mapentity.Triangle;
import sun.security.provider.certpath.Vertex;

/**
 * Created by Ja on 17.2.2018.
 */

class MapGenerator {
    Random rand;
    protected World world;
    protected GameScreen screen;
    protected OrthographicCamera camera;
    protected Vector2 negativeDeltaX_transition;
    protected Triangle last;

    public static final float MAX_RADIUS = 5f;
    public static final float SCREEN_GEN_BORDER_X = GameScreen.CLIENT_WIDTH*1.5f;

    public MapGenerator(GameScreen screen, Array<Triangle> triangles,Vector2[] startingEdge, Vector2 C) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.camera = screen.camera;
        rand = new Random();
        triangles.add(last = new Triangle(screen,new Vector2[]{startingEdge[0],startingEdge[1],C},screen.camera));
    }


    public Array<Triangle> generateIfNeeded(Array<Triangle> entities){

        while (last.getC().x < SCREEN_GEN_BORDER_X) {
            entities.add(generate(last,entities));
        }
        return entities;
    }


    public Triangle generate(Triangle last, Array<Triangle> entities) {
        Triangle tri;
        Vector2[] newShared = new Vector2[2];
        Vector2 newC;
        int numFails = 0;


        int triedIdx = getRandomIdx();

        newShared[0]= last.getSharedSide()[triedIdx].cpy();
        newShared[1]= last.getC().cpy();
        Vector2[] vertices;
        do {
            if(numFails>=20) System.out.println("fails : " +numFails);
            if((numFails % 50)==49) {
                System.out.println("Switching");
                triedIdx = getOtherVertexIdx(triedIdx);
                newShared[0] = last.getSharedSide()[triedIdx].cpy();}
            newC = getRandomPoint(last, newShared);
            numFails++;
            vertices = new Vector2[]{newShared[0].cpy(),newShared[1].cpy(),newC.cpy()};
        } while(isColliding(vertices,entities)); // || !isValidTriangle(vertices)


        tri = new Triangle(screen, new Vector2[]{newShared[0].cpy(), newShared[1].cpy(), newC.cpy()}, camera);

        camera.position.set(tri.getCenterPoint().x,tri.getCenterPoint().y,0);
        camera.update();

        Vector2 lastTransition = tri.getCenterPoint().cpy().sub(last.getCenterPoint().cpy());

        if((negativeDeltaX_transition!=null) &&(negativeDeltaX_transition.x>0)) {
            negativeDeltaX_transition = null;

        }

        if((negativeDeltaX_transition== null)&&(lastTransition.x<0)){
            negativeDeltaX_transition = lastTransition.cpy();

        }

        if((lastTransition.x<0)||(negativeDeltaX_transition != null)){
            negativeDeltaX_transition.add(lastTransition.cpy());
        }

        setLast(tri);

        return tri;
    }

    public Vector2 predictNext(int sideIdx, Array<Triangle> entities){
        Vector2[] newShared = new Vector2[2];
        Vector2 newC;

        newShared[0]= last.getSharedSide()[sideIdx];
        newShared[1]= last.getC();
        Vector2[] vertices;
        newC = getRandomPoint(last, newShared);
        vertices = new Vector2[]{newShared[0].cpy(),newShared[1].cpy(),newC.cpy()};
        // || !isValidTriangle(vertices)
        if(!isColliding(vertices,entities)){
            return newC;
        }

        return null;
    }
/*
    public Vector2[] predict(int howMany, int useHowMany, Array<Triangle> entities){
        Array<Vector2> newCVertices = new Array<Vector2>();
        int [] idxUsed = new int[howMany];
        int idx = 0;

        while (idx<=10) {
            if(newCVertices.get(idx)!=null){
                idxUsed[idx]=getOtherVertexIdx(idxUsed[idx]);

                Vector2 attempt = new Vector2();
                int fails = 0;
                while((attempt = predictNext(idxUsed[idx],entities))==null){
                    fails++;
                    if (fails >=20) {
                        idx--;
                    } else if(fails>=10) {
                        idxUsed[idx]=getOtherVertexIdx(idxUsed[idx]);
                    }
                }

                newCVertices.get(idx).set();
            } else {
                idxUsed[idx] = getRandomIdx();
                newCVertices.add(predictNext(idxUsed[idx],entities));
            }
        }

    }
*/


    public double getAngleFromSlope(float slope) {
        return Math.toDegrees(Math.atan(slope));
    }


    public float getRandomAngle(Vector2 [] vertices, Vector2[] newShared) {
        Vector2 center = Triangle.getCenterPoint(vertices).cpy();
        Vector2 sideCenter = Triangle.getCenterPoint(newShared).cpy();
        Side side = new Side(newShared);
        Vector2 vectorToCenter = center.cpy().sub(sideCenter.cpy());
        Random rand = new Random();

        //half circle
        float angle = rand.nextFloat()*180;

        try {
            angle += (float)getAngleFromSlope(side.getSlope());
            if(side.isAbovePoint(center) == 1) {
                angle+=180;
            }
        } catch (Exception zeroDivException) {
            if(vectorToCenter.x>0){
                angle +=90;
            } else if(vectorToCenter.x<0) {
                angle -=90;
            } else {
                return getRandomAngle(vertices,newShared);
            }
        }

        return angle;
    }


    public Vector2 getRandomPoint(Triangle last, Vector2[] newShared){


        float radius;
        Vector2 center = Triangle.getCenterPoint(newShared);
        float angle = getRandomAngle(last.getPoints(),newShared);

        float random_x;
        float random_y;

        radius = rand.nextFloat() * MAX_RADIUS/2f + MAX_RADIUS/2f;
        //radius = 1;
//
            if(negativeDeltaX_transition!= null && getDeltaX()<0){
                float limitX = Math.abs(negativeDeltaX_transition.x/4f);
                float limitYX = Math.abs(negativeDeltaX_transition.y/20f);
                //System.out.println("x% blocked "+limitX/MAX_RADIUS*100);
                if(limitX<MAX_RADIUS)
                    random_x = rand.nextFloat()*(2*MAX_RADIUS-limitX)-MAX_RADIUS+limitX;
                else
                    random_x=rand.nextFloat()*(MAX_RADIUS);
                    random_y = Math.signum(rand.nextInt(2)*2-1)*rand.nextFloat()*MAX_RADIUS;
            } else {
//
                random_x = (float)(Math.cos(Math.toRadians(angle)) * radius);
                random_y = (float)(Math.sin(Math.toRadians(angle)) * radius);

            }

        Vector2 point = new Vector2(random_x+center.x,random_y+center.y);
        return point;

    }




    public boolean isValidTriangle(Vector2 [] vertices) {
        try {
            return !(new Function(vertices[0],vertices[1])).contains(vertices[2]);}
        catch(Exception e) {
            return !((vertices[0].x == vertices[1].x) && (vertices[1].x == vertices[2].x));
        }
    }

    public boolean isColliding(Vector2[] vertex, Array<Triangle> entities) {
        Side[] newSide = new Side[]{
                new Side(vertex[0].cpy(), vertex[2].cpy()),
                new Side(vertex[1].cpy(), vertex[2].cpy())};


        for (int idx = 0; idx < entities.size; idx++) {

            //if (!Triangle.isTooFar(Triangle.getCenterPoint(vertex), triangle.getCenterPoint())) {
            if(entities.get(idx).contains(vertex[2])) {
                return true;
            } else {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        //if ((newSide[i].isIntersecting(entities.get(idx).getSides()[j], true))||(entities.get(idx).contains(vertex[2]))) {
                        if ((newSide[i].isIntersecting(entities.get(idx).getSides()[j], true))) {
                            return true;
                        }
                    }
                    //}
                }
            }
        }

        return false;
    }

    public float getDeltaX() {
        return negativeDeltaX_transition.x;
    }

    public int getDirectionY() {
        return (int)Math.signum(negativeDeltaX_transition.y);
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
