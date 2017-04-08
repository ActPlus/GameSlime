package sk.actplus.slime.entity.mapobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.entity.Entity;

import static sk.actplus.slime.constants.Values.PLAYER_SPEED;

/**
 * Created by Ja on 8.4.2017.
 */

public class MovingBlock extends Entity {
    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.KinematicBody;
    public static final boolean FIXED_ROTATION = true;
    public static final float DENSITY = 0.35f;
    public static final float RESTITUTION = 0f;
    public static final float FRICTION = 0f;

    public float sideWidth = 0.2f;
    public float sideHeight = 1f;

    public Body body;

    public MovingBlock(World world, float xi, float yi, float width, float height, Vector2 direction) {
        super(world);
        /**
         * Creates Main Player Body, controlled by Input
         */
        sideWidth = width;
        sideHeight = height;

        BodyDef bodyDefMain = defineBody(BODY_TYPE, xi-0.1f, yi, FIXED_ROTATION);
        PolygonShape shapeMain = new PolygonShape();
        shapeMain.setAsBox(sideWidth / 2, sideHeight / 2);
        FixtureDef fixtureDefMain = defineFixture(shapeMain, DENSITY, RESTITUTION, FRICTION);

        body = createBody(bodyDefMain, fixtureDefMain);
        body.setLinearVelocity(direction);

    }

}
