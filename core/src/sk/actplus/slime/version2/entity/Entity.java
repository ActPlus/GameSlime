package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import sk.actplus.slime.version2.GameScreen;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class Entity {
    protected World world;
    protected GameScreen screen;
    protected boolean setToDestroy;
    protected boolean destroyed;

    protected Body body;

    public Entity(GameScreen screen) {
        this.world = screen.getWorld();
        this.screen = screen;
        setToDestroy = false;
        destroyed = false;

    }

    public abstract void render(float delta, PolygonSpriteBatch polyBatch);
    public abstract void handleCollision(short collisionBIT);
    public abstract void destroy();

    public void update(float delta) {
        shouldDie();
    }



    //checks if Body should be destoroyed
    protected void shouldDie() {
        if (setToDestroy && !destroyed) {
            world.destroyBody(body);
            destroy();
        }

        //TODO: remove from Entity array
    }

    /**
     * Creates Body, initializes it, adds into World
     *
     * @param bodyDef    - Body Definition
     * @param fixtureDef - Fixture Definition
     * @return New Body
     */

    public  static Body createBody(World world, BodyDef bodyDef, FixtureDef fixtureDef) {
        Body body;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }

    public  static BodyDef defineBody(BodyDef.BodyType type, float xi, float yi, boolean fixedRotation) {
        BodyDef def = new BodyDef();
        def.type = type;
        def.position.set(xi, yi);
        def.fixedRotation = fixedRotation;
        return def;
    }

    /**
     * Create and returns Definition of Fixture, Physical properties
     *
     * @param shape       - Shape of Fixture
     * @param density     - Density, can change weight
     * @param restitution - Bouncing Ratio
     * @param friction    - Friction on other bodies
     * @return Returns Definition of Physical properties
     */

    public static  FixtureDef defineFixture(Shape shape, float density, float restitution, float friction) {
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = density;
        fixDef.restitution = restitution;
        fixDef.friction = friction;
        return fixDef;
    }

    public  static FixtureDef defineFixture(Shape shape, float density, float restitution, float friction, short category, short maskbit) {
        FixtureDef fixDef;
        fixDef = defineFixture(shape, density, restitution, friction);
        fixDef.filter.categoryBits = category;
        fixDef.filter.maskBits = (short) ~maskbit;
        return fixDef;
    }

    public  static FixtureDef collideWith(FixtureDef fixDef, short category) {
        //TODO : Set Collision
        return fixDef;
    }

    public  static FixtureDef ignoreCollision(FixtureDef fixDef, short maskbit) {
        //TODO : Set Collision to ignore
        return fixDef;
    }

    public Body getBody() {
        return body;
    }


    /**
     * Creates Distance Joint Def with Properties.
     *
     * @param frequencyHz      - The mass-spring-damper frequency in Hertz.
     * @param dampingRatio     - The damping ratio. 0 = no damping, 1 = critical damping.
     * @param collideConnected - Boolean will firstBody and second Body collide?
     */

    public  static DistanceJointDef defineDistanceJointDef(float frequencyHz, float dampingRatio, boolean collideConnected) {
        DistanceJointDef jointDef = new DistanceJointDef();
        // Connect the outer circles to each other

        jointDef.collideConnected = false;
        jointDef.frequencyHz = frequencyHz;
        jointDef.dampingRatio = dampingRatio;

        return jointDef;
    }

    /**
     * @param jointDef   - Distance Joint Def
     * @param firstBody  - first Box2D Body
     * @param secondBody - second Box2D Body
     * @param anchorA    - A point of Joint
     * @param anchorB    - B point of Joint
     */
    public static void createDistanceJoint(World world, DistanceJointDef jointDef, Body firstBody, Body secondBody, Vector2 anchorA, Vector2 anchorB) {
        jointDef.initialize(firstBody, secondBody, anchorA, anchorB);
        world.createJoint(jointDef);
    }

    /**
     * @param jointDef   - Distance Joint Def
     * @param firstBody  - first Box2D Body
     * @param secondBody - second Box2D Body
     */

    public static void createDistanceJointSpec(World world, DistanceJointDef jointDef, Body firstBody, Body secondBody) {

        createDistanceJoint(world, jointDef, firstBody, secondBody, firstBody.getWorldCenter(), firstBody.getWorldCenter());
    }

    public static void createDistanceJointAtCenter(World world,DistanceJointDef jointDef, Body firstBody, Body secondBody) {
        createDistanceJoint(world, jointDef, firstBody, secondBody, firstBody.getWorldCenter(), secondBody.getWorldCenter());
    }
}
