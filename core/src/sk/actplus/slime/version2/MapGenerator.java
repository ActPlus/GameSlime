package sk.actplus.slime.version2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import sk.actplus.slime.version2.entity.mapentity.Triangle;

import static sk.actplus.slime.version2.GameScreen.PPM;

/**
 * Created by Ja on 17.2.2018.
 */

class MapGenerator {
    Random rand;
    protected World world;
    protected GameScreen screen;
    protected MovableCamera camera;
    protected Vector2 negativeDeltaX_transition;
    protected Triangle last;

    public static final float MAX_RADIUS = 10f;
    public static final float SCREEN_GEN_BORDER_X = GameScreen.CLIENT_WIDTH*1.5f;

    public MapGenerator(GameScreen screen, Array<Triangle> triangles,Vector2[] startingEdge, Vector2 C) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.camera = screen.camera;
        rand = new Random();
        triangles.add(last = new Triangle(screen,new Vector2[]{startingEdge[0],startingEdge[1],C},screen.camera));
    }


    public Array<Triangle> generateIfNeeded(Array<Triangle> entities){

        while (last.getCenterPoint().x < SCREEN_GEN_BORDER_X) {
            //entities.add(generate(last,entities));
        }

        return entities;
    }

    int returnBy = 0;
    int genAfter = 0;
    public Array<Triangle> generate(Triangle last, Array<Triangle> triangles) {
        Vector2[] newShared = new Vector2[2];
        Triangle tri;
        Vector2 newC;

        int numFails = 0;

        int triedIdx = getRandomIdx();

        Vector2[] lastSharedSide = new Vector2[]{last.getVertices()[0].cpy(),last.getVertices()[1].cpy()};
        newShared[0]= lastSharedSide[triedIdx].cpy();
        newShared[1]= last.getVertices()[2].cpy();
        Vector2[] newTriangleVertices;
        int returnBefore = returnBy;
        do {
            if((numFails )==5) {
                System.out.println("Switching");
                triedIdx = getOtherVertexIdx(triedIdx);
                newShared[0] = lastSharedSide[triedIdx].cpy();
            }

            if(numFails>=10){
                returnBy++;
                System.out.println("returtnig by one in array of: " + triangles.size);



                for (int i = 0; i < returnBy; i++) {
                    if(negativeDeltaX_transition!=null){
                        if(triangles.size==1){
                            returnBy=0;
                            genAfter=0;
                            break;
                        }
                        negativeDeltaX_transition.sub(triangles.get(triangles.size-1).getCenterPoint());
                    }
                    world.destroyBody(triangles.get(triangles.size-1).getBody());
                    triangles.removeIndex(triangles.size-1);

                }


                last = triangles.get(triangles.size-1);


                numFails = 0;
                triedIdx = getRandomIdx();

                lastSharedSide = new Vector2[]{last.getVertices()[0].cpy(),last.getVertices()[1].cpy()};
                newShared[0]= lastSharedSide[triedIdx].cpy();
                newShared[1]= last.getVertices()[2].cpy();


            }
            newC = getRandomPoint(last, newShared);
            numFails++;
        newTriangleVertices = new Vector2[]{newShared[0].cpy(),newShared[1].cpy(),newC.cpy()};
        } while(isColliding(newTriangleVertices,triangles) || !isValidTriangle(newTriangleVertices) || (Triangle.getArea(newTriangleVertices)<20));

        if((returnBy!=0)&&(returnBefore==returnBy)){
            genAfter++;
        }
        if (genAfter > returnBy+5) {
            returnBy=0;
            genAfter=0;
        }

        tri = new Triangle(screen, newTriangleVertices, camera);
        triangles.add(tri);

        camera.goTo(tri.getCenterPoint().x,tri.getCenterPoint().y*1.5f);

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

        return triangles;
    }


    public double getAngleFromSlope(float slope) {
        return Math.toDegrees(Math.atan(slope));
    }


    public float getRandomAngle(Vector2[] vertices, Vector2[] newShared) {
        Vector2 center = Triangle.getCenterPoint(vertices);
        Vector2 sideCenter = Triangle.getCenterPoint(newShared);
        Side side = new Side(newShared);
        Vector2 vectorToCenter = center.cpy().sub(sideCenter);
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
        float angle = getRandomAngle(last.getVertices(),newShared);

        float random_x;
        float random_y;

        radius = rand.nextFloat() * MAX_RADIUS/2f + MAX_RADIUS/2f;

            if((negativeDeltaX_transition!= null) && (getDeltaX()<0)){
                float limitX = Math.abs(negativeDeltaX_transition.x/2f);
                if(limitX<MAX_RADIUS)
                    random_x = rand.nextFloat()*(2*MAX_RADIUS-limitX)-MAX_RADIUS+limitX;
                else
                    random_x=rand.nextFloat()*(MAX_RADIUS);
                    random_y = Math.signum(rand.nextInt(2)*2-1)*rand.nextFloat()*MAX_RADIUS;
            } else {
                random_x = (float)(Math.cos(Math.toRadians(angle)) * radius);
                random_y = (float)(Math.sin(Math.toRadians(angle)) * radius);
            }

        return new Vector2(center.x+random_x,center.y+random_y);

    }




    public boolean isValidTriangle(Vector2 [] vertices) {
            return !(new Function(vertices[0],vertices[1])).contains(vertices[2]);
    }

    public boolean isColliding(Vector2[] vertex, Array<Triangle> triangles) {
        Side[] newSide = new Side[]{
                new Side(vertex[0].cpy(), vertex[2].cpy()),
                new Side(vertex[1].cpy(), vertex[2].cpy())};


        for (int idx = 0; idx < triangles.size; idx++) {

            //if (!triangles.get(idx).isTooFar(Triangle.getCenterPoint(vertex))) {
            if(triangles.get(idx).pointInArea(vertex[2])) {
                return true;
            } else {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        //if ((newSide[i].isIntersecting(entities.get(idx).getSides()[j], true))||(entities.get(idx).contains(vertex[2]))) {
                        if ((newSide[i].isIntersecting(triangles.get(idx).getSides()[j], true))) {
                            return true;
                        }
                    }
                    }
              //  }
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
        this.last = tri;
    }

}
