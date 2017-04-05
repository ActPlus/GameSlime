package sk.actplus.slime.entity.player;

/**
 * Created by Ja on 4.4.2017.
 */


        import com.badlogic.gdx.math.Vector2;
        import com.badlogic.gdx.physics.box2d.Body;
        import com.badlogic.gdx.physics.box2d.BodyDef;
        import com.badlogic.gdx.physics.box2d.CircleShape;
        import com.badlogic.gdx.physics.box2d.FixtureDef;
        import com.badlogic.gdx.physics.box2d.PolygonShape;
        import com.badlogic.gdx.physics.box2d.Shape;
        import com.badlogic.gdx.physics.box2d.World;
        import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

        import sk.actplus.slime.other.BodyArray;

        import static sk.actplus.slime.constants.Values.PPM;

/**
 * Created by Admin on 20.12.2016.
 */

public class Jelly extends Player {
    public static final short CATEGORY = 0x2000;
    public static final int NUM_SEGMENTS = 5;
    public static final float INNER_SIDE = 0.75f;
    public static final float OUTER_RADIUS = 0.25f;
    public static final float ORBITAL_SIDE = 2f;
    public static final float INNER_DENSITY = 0.05f;
    public static final float OUTER_DENSITY = 0.2f;
    public static final float INNER_FRICTION = 0f;
    public static final float OUTER_FRICTION = 0f;
    public static final float OUTER_SPRINGINESS = 1f;
    public static final float INNER_SPRINGINESS = 2f;
    public static final float DIAGONAL_SPRINGINESS =0.5f;
    public static final float OUTER_RESTITUTION = 0f;
    public static final float INNER_RESTITUTION = 0f;
    public static final float DAMPING = 0.5f;
    public static final float INNRER_DAMPING = 0f;

    private World world;




    public Jelly (World world, float xi, float yi) {
        super(world, xi, yi);
        this.world = world;

        float upLine[][] = new float[NUM_SEGMENTS][2];
        float downLine[][] = new float[NUM_SEGMENTS][2];
        float leftLine[][] = new float[NUM_SEGMENTS][2];
        float rightLine[][] = new float[NUM_SEGMENTS][2];

        BodyArray upBodies = new BodyArray();
        BodyArray downBodies = new BodyArray();
        BodyArray leftBodies = new BodyArray();
        BodyArray rightBodies = new BodyArray();



        //define shape of Outer Shape and fixture
        Shape shape = new CircleShape();
        shape.setRadius(OUTER_RADIUS);
        FixtureDef fixtureDef = defineFixture(shape,OUTER_DENSITY,OUTER_RESTITUTION,OUTER_FRICTION,CATEGORY);
            Body centerBody = world.createBody(defineBody(BodyDef.BodyType.StaticBody,xi,yi,true));

        /**Add segments*/
        // For each segment...

        float x;
        float y;

        float space = (ORBITAL_SIDE/((float) NUM_SEGMENTS));
        //System.out.println((ORBITAL_SIDE/((float) NUM_SEGMENTS)));

        for (int i = 0; i < NUM_SEGMENTS; i++) {
            x =  ORBITAL_SIDE/2f - i*space - space/2f;
            y =  - ORBITAL_SIDE/2f + space/2f;
            downLine[i][0] = x;
            downLine[i][1] = y;

            x =  - ORBITAL_SIDE/2f + i*space + space/2f;
            y =   ORBITAL_SIDE/2f - space/2f;
            upLine[i][0] = x;
            upLine[i][1] = y;

            x =  - ORBITAL_SIDE/2f + space/2f;
            y =  - ORBITAL_SIDE/2f + i*space + space/2f;
            leftLine[i][0] = x;
            leftLine[i][1] = y;

            x =  ORBITAL_SIDE/2f - space/2f;
            y =  ORBITAL_SIDE/2f - i*space -space/2f;
            rightLine[i][0] = x;
            rightLine[i][1] = y;
        }

        initializeBodyList(downBodies,centerBody,downLine,fixtureDef);
        initializeBodyList(upBodies,centerBody,upLine,fixtureDef);
        initializeBodyList(leftBodies,centerBody,leftLine,fixtureDef);
        initializeBodyList(rightBodies,centerBody,rightLine,fixtureDef);


        // Connect the joints

        connectJointsOfLine(upBodies,centerBody);
        connectJointsOfLine(downBodies,centerBody);
        connectJointsOfLine(leftBodies,centerBody);
        connectJointsOfLine(rightBodies,centerBody);


            DistanceJointDef jointDef = defineDistanceJointDef(OUTER_SPRINGINESS,DAMPING,false);

        //connect corners
        createDistanceJointAtCenter(jointDef,upBodies.first(),leftBodies.get(leftBodies.size-1));
            createDistanceJointAtCenter(jointDef,rightBodies.first(),upBodies.get(upBodies.size-1));
            createDistanceJointAtCenter(jointDef,downBodies.first(),rightBodies.get(rightBodies.size-1));
            createDistanceJointAtCenter(jointDef,leftBodies.first(),downBodies.get(downBodies.size-1));

        for (int i = 1; i < upBodies.size-1; i++) {
                createDistanceJointAtCenter(jointDef,upBodies.get(i),leftBodies.get(leftBodies.size-i));
                createDistanceJointAtCenter(jointDef,rightBodies.get(i),upBodies.get(upBodies.size-i));
                createDistanceJointAtCenter(jointDef,downBodies.get(i),rightBodies.get(rightBodies.size-i));
                createDistanceJointAtCenter(jointDef,leftBodies.get(i),downBodies.get(downBodies.size-i));
        }

            DistanceJointDef diagonalJointDef = defineDistanceJointDef(DIAGONAL_SPRINGINESS,DAMPING,false);

        for (int i = 0; i < upBodies.size-1; i++) {
            for (int j = 1; j < upBodies.size-2; j++) {
                    createDistanceJointAtCenter(diagonalJointDef,upBodies.get(i),leftBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,upBodies.get(i),rightBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,upBodies.get(i),downBodies.get(leftBodies.size-j));

                    createDistanceJointAtCenter(diagonalJointDef,leftBodies.get(i),upBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,leftBodies.get(i),rightBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,leftBodies.get(i),downBodies.get(leftBodies.size-j));

                    createDistanceJointAtCenter(diagonalJointDef,rightBodies.get(i),leftBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,rightBodies.get(i),upBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,rightBodies.get(i),downBodies.get(leftBodies.size-j));

                    createDistanceJointAtCenter(diagonalJointDef,downBodies.get(i),leftBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,downBodies.get(i),rightBodies.get(leftBodies.size-j));
                    createDistanceJointAtCenter(diagonalJointDef,downBodies.get(i),upBodies.get(leftBodies.size-j));
            }

        }

    }


    public void initializeBodyList(BodyArray bodyArray, Body centerBody, float[][] points, FixtureDef fixDef) {
        for (int i = 0; i < NUM_SEGMENTS-1; i++) {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;

            // Remember to divide by PTM_RATIO to convert to Box2d coordinates
            Vector2 circlePosition = new Vector2(points[i][0],points[i][1]);
            Vector2 v2 = centerBody.getPosition().cpy().add(circlePosition);
            bodyDef.position.set(v2);

            // Create the body and fixture
            Body body = world.createBody(bodyDef);
            body.createFixture(fixDef);

            // Add the body to the array to connect joints to it
            // later. b2Body is a C++ object, so must wrap it
            // in NSValue when inserting into it NSMutableArray
            bodyArray.add(body);
        }
    }

    public void connectJointsOfLine(BodyArray bodies, Body centerBody) {
        Body first;
        Body second = null;
            DistanceJointDef jointDef = defineDistanceJointDef(OUTER_SPRINGINESS,INNRER_DAMPING,false);

            for (int i = 0; i < bodies.size-1; i++) {
            first = bodies.get(i);
            second = bodies.get(i + 1);
            createDistanceJointAtCenter(jointDef,first,second);
                    createDistanceJointAtCenter(jointDef,first,centerBody);
        }
            createDistanceJointAtCenter(jointDef,second,centerBody);
    }

}
