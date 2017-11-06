package sk.actplus.slime.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

import sk.actplus.slime.other.BodyArray;

import static sk.actplus.slime.constants.Values.PPM;

/**
 * Created by Admin on 22.12.2016.
 */

public class Entity extends BodyArray{
    private float xi,yi;
    public World world;

    public Entity(World world){
        this.world = world;
    }


    /**
     * Add new Body to Entity
     * @param body - new Body
     */

    public void addBody(Body body) {
        this.add(body);
    }

    public void adNewBody(BodyDef bodyDef, FixtureDef fixtureDef) {
        Body body = createBody(bodyDef,fixtureDef);
        this.add(body);
    }

    /**
     * Creates Body, initializes it, adds into World
     * @param bodyDef - Body Definition
     * @param fixtureDef - Fixture Definition
     * @return New Body
     */

    public Body createBody(BodyDef bodyDef, FixtureDef fixtureDef) {
        Body body;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        return body;
    }

    /**
     * Create and returns Definition Of Body
     * @param type - Body Type Static/Dynamic/Kinematic
     * @param xi - X coordinate in PPM
     * @param yi - Y coordinate in PPM
     * @param rotation - Is rotation of body fixed?
     * @return Definition of body
     */

    public BodyDef defineBody(BodyDef.BodyType type, float xi, float yi, boolean rotation) {
        BodyDef def = new BodyDef();
        def.type = type;
        def.position.set(xi,yi);
        def.fixedRotation = rotation;
        return def;
    }

    /**
     * Create and returns Definition of Fixture, Physical properties
     * @param shape - Shape of Fixture
     * @param density - Density, can change weight
     * @param restitution - Bouncing Ratio
     * @param friction - Friction on other bodies
     * @return Returns Definition of Physical properties
     */

    public FixtureDef defineFixture(Shape shape, float density, float restitution, float friction) {
        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = density;
        fixDef.restitution = restitution;
        fixDef.friction = friction;
        return fixDef;
    }

    /**
     * Create and returns Definition of Fixture, Physical properties
     * @param shape - Shape of Fixture
     * @param density - Density, can change weight
     * @param restitution - Bouncing Ratio
     * @param friction - Friction on other bodies
     * @param category - Category of which bodies will collide and which not
     * @return Definition of Physical Properties and Filter of Collision
     */

    public FixtureDef defineFixture(Shape shape, float density, float restitution, float friction, short category) {
        FixtureDef fixDef;
        fixDef = defineFixture(shape,density,restitution,friction);
        fixDef.filter.categoryBits = category;
        fixDef.filter.maskBits = (short)~category;
        return fixDef;
    }


    /**
     * Creates Distance Joint Def with Properties.
     * @param frequencyHz - The mass-spring-damper frequency in Hertz.
     * @param dampingRatio - The damping ratio. 0 = no damping, 1 = critical damping.
     * @param collideConnected - Boolean will firstBody and second Body collide?
     */

    public DistanceJointDef defineDistanceJointDef(float frequencyHz, float dampingRatio, boolean collideConnected) {
        DistanceJointDef jointDef = new DistanceJointDef();
        // Connect the outer circles to each other

        jointDef.collideConnected = false;
        jointDef.frequencyHz = frequencyHz;
        jointDef.dampingRatio = dampingRatio;

        return jointDef;
    }

    /**
     *
     * @param jointDef - Distance Joint Def
     * @param firstBody - first Box2D Body
     * @param secondBody - second Box2D Body
     * @param anchorA - A point of Joint
     * @param anchorB - B point of Joint
     */
    public void createDistanceJoint(DistanceJointDef jointDef,Body firstBody, Body secondBody,Vector2 anchorA, Vector2 anchorB) {
        jointDef.initialize(firstBody, secondBody, anchorA,anchorB);
        world.createJoint(jointDef);
    }

    /**
     * @param jointDef - Distance Joint Def
     * @param firstBody - first Box2D Body
     * @param secondBody - second Box2D Body
     */

    public void createDistanceJointAtCenter(DistanceJointDef jointDef,Body firstBody, Body secondBody) {
        createDistanceJoint(jointDef,firstBody,secondBody,firstBody.getWorldCenter(),secondBody.getWorldCenter());
    }
}
