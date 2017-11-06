package sk.actplus.slime.entity.player;

/**
 * Created by Ja on 4.4.2017.
 */


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import sk.actplus.slime.other.BodyArray;

/**
 * Created by Admin on 20.12.2016.
 */

public class Jelly extends Player {
    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.DynamicBody;
    public static final int NUM_SEGMENTS = 10;

    public static final short CATEGORY = 0x1000;
    public static final boolean FIXED_ROTATION = true;

    public static final float INNER_RADIUS = 1f;
    public static final float OUTER_RADIUS = 0.1f;
    public static final float ORBITAL_SIDE = 2.5f;

    public static final float INNER_DENSITY = 0.4f;
    public static final float OUTER_DENSITY = 0.05f;

    public static final float OUTER_RESTITUTION = 0f;
    public static final float INNER_RESTITUTION = 0f;

    public static final float INNER_FRICTION = 0f;
    public static final float OUTER_FRICTION = 0f;

    public static final float INNER_FREQUENCY_HZ = 14f;
    public static final float OUTER_FREQUENCY_HZ = 16f;
    public static final float DIAGONAL_FREQUENCY_HZ = 4f;

    public static final float INNER_DAMPING = 1f;
    public static final float OUTER_DAMPING = 1f;
    public static final float DIAGONAL_DAMPING = 1f;

    private Body bodyMain;

    private float upLine[][] = new float[NUM_SEGMENTS][2];
    private float downLine[][] = new float[NUM_SEGMENTS][2];
    private float leftLine[][] = new float[NUM_SEGMENTS][2];
    private float rightLine[][] = new float[NUM_SEGMENTS][2];

    private BodyArray upBodies = new BodyArray();
    private BodyArray downBodies = new BodyArray();
    private BodyArray leftBodies = new BodyArray();
    private BodyArray rightBodies = new BodyArray();

    public Body body;


    public Jelly(World world, float xi, float yi) {
        super(world);
        this.world = world;

        /**
         * Main Body's Definitions, Body Definition and Fixture Definition
         * Creates Main Player Body, controlled by Input
         * */

        BodyDef bodyDefMain = defineBody(BODY_TYPE, xi, yi, FIXED_ROTATION);
        CircleShape shapeMain = new CircleShape();
        shapeMain.setRadius(INNER_RADIUS / 2);
        FixtureDef fixtureDefMain = defineFixture(shapeMain, INNER_DENSITY, INNER_RESTITUTION, INNER_FRICTION,CATEGORY);
        fixtureDefMain.filter.categoryBits = CATEGORY;
        fixtureDefMain.filter.maskBits = (short)~CATEGORY;

        /**
         * Orbital's Body Definitions Definitions, Body Definition and Fixture Definition
         */

        BodyDef bodyDefOrbital = defineBody(BODY_TYPE, xi, yi, FIXED_ROTATION);
        CircleShape shapeOrbital = new CircleShape();
        shapeOrbital.setRadius(OUTER_RADIUS / 2);
        FixtureDef fixtureDefOrbital = defineFixture(shapeOrbital, OUTER_DENSITY, OUTER_RESTITUTION, OUTER_FRICTION, CATEGORY);

        bodyMain = createBody(bodyDefMain, fixtureDefMain);
        body = bodyMain;




        /**
         * Calculate Coordinates [i] [0] - x, [i] [1] - y. Save to 4 Arrays, by their location in row.
         */

        float x;
        float y;
        float space = (ORBITAL_SIDE / ((float) NUM_SEGMENTS));

        for (int i = 1; i < NUM_SEGMENTS; i++) {
            x = ORBITAL_SIDE / 2f - i * space - space / 2f;
            y = -ORBITAL_SIDE / 2f + space / 2f;
            downLine[i][0] = x;
            downLine[i][1] = y;

            x = -ORBITAL_SIDE / 2f + i * space + space / 2f;
            y = ORBITAL_SIDE / 2f - space / 2f;
            upLine[i][0] = x;
            upLine[i][1] = y;

            x = -ORBITAL_SIDE / 2f + space / 2f;
            y = -ORBITAL_SIDE / 2f + i * space + space / 2f;
            leftLine[i][0] = x;
            leftLine[i][1] = y;

            x = ORBITAL_SIDE / 2f - space / 2f;
            y = ORBITAL_SIDE / 2f - i * space - space / 2f;
            rightLine[i][0] = x;
            rightLine[i][1] = y;
        }

        initializeBodyList(downBodies, bodyMain, downLine, bodyDefOrbital, fixtureDefOrbital);
        initializeBodyList(upBodies, bodyMain, upLine, bodyDefOrbital, fixtureDefOrbital);
        initializeBodyList(leftBodies, bodyMain, leftLine, bodyDefOrbital, fixtureDefOrbital);
        initializeBodyList(rightBodies, bodyMain, rightLine, bodyDefOrbital, fixtureDefOrbital);

        // Connect the joints

        DistanceJointDef jointDefOUTER = defineDistanceJointDef(OUTER_FREQUENCY_HZ, OUTER_DAMPING, FIXED_ROTATION);
        DistanceJointDef jointDefCENTER = defineDistanceJointDef(INNER_FREQUENCY_HZ, INNER_DAMPING, FIXED_ROTATION);

        connectJointsOfLine(upBodies, bodyMain, jointDefOUTER, jointDefCENTER);
        connectJointsOfLine(downBodies, bodyMain, jointDefOUTER, jointDefCENTER);
        connectJointsOfLine(leftBodies, bodyMain, jointDefOUTER, jointDefCENTER);
        connectJointsOfLine(rightBodies, bodyMain, jointDefOUTER, jointDefCENTER);

        createDistanceJointAtCenter(jointDefOUTER, upBodies.first(), leftBodies.get(leftBodies.size - 1));
        createDistanceJointAtCenter(jointDefOUTER, rightBodies.first(), upBodies.get(upBodies.size - 1));
        createDistanceJointAtCenter(jointDefOUTER, downBodies.first(), rightBodies.get(rightBodies.size - 1));
        createDistanceJointAtCenter(jointDefOUTER, leftBodies.first(), downBodies.get(downBodies.size - 1));


        DistanceJointDef diagonalDistanceJointDef = defineDistanceJointDef(DIAGONAL_FREQUENCY_HZ, DIAGONAL_DAMPING, false);

        for (int i = 1; i < NUM_SEGMENTS - 2; i++) {
            createDistanceJointAtCenter(diagonalDistanceJointDef, upBodies.get(upBodies.size - i - 1), rightBodies.get(i));
            createDistanceJointAtCenter(diagonalDistanceJointDef, upBodies.get(i), leftBodies.get(leftBodies.size - i - 1));
            createDistanceJointAtCenter(diagonalDistanceJointDef, downBodies.get(i), rightBodies.get(rightBodies.size - i - 1));
            createDistanceJointAtCenter(diagonalDistanceJointDef, leftBodies.get(leftBodies.size - i - 1), downBodies.get(i));

        }
    }


    public void initializeBodyList(BodyArray bodyArray, Body bodyMain, float[][] points, BodyDef bodyDef, FixtureDef fixDef) {
        for (int i = 1; i < NUM_SEGMENTS - 1; i++) {
            // Remember to divide by PTM_RATIO to convert to Box2d coordinates
            Vector2 circlePosition = new Vector2(points[i][0], points[i][1]);
            Vector2 v2 = bodyMain.getPosition().cpy().add(circlePosition);
            bodyDef.position.set(v2);

            // Create the body and fixture
            Body body = createBody(bodyDef, fixDef);

            // Add the body to the array to connect joints to it
            // later. b2Body is a C++ object, so must wrap it
            // in NSValue when inserting into it NSMutableArray
            bodyArray.add(body);
        }

        for (Body body: bodyArray) {
            body.getFixtureList().first().setUserData("player");
        }
    }

    public void connectJointsOfLine(BodyArray bodies, Body bodyMain, DistanceJointDef jointDefOUTER, DistanceJointDef jointDefCENTER) {
        Body first;
        Body second = null;

        for (int i = 0; i < bodies.size - 1; i++) {
            first = bodies.get(i);
            second = bodies.get(i + 1);
            createDistanceJointAtCenter(jointDefOUTER, first, second);
            createDistanceJointAtCenter(jointDefCENTER, first, bodyMain);
        }
        createDistanceJointAtCenter(jointDefCENTER, second, bodyMain);
    }

}
