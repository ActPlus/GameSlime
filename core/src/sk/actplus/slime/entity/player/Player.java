package sk.actplus.slime.entity.player;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


import sk.actplus.slime.entity.Entity;


/**
 * Created by Admin on 21.12.2016.
 */

public class Player extends Entity {
    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.DynamicBody;
    public static final boolean FIXED_ROTATION = true;
    public static final float SIDE_WIDTH = 2f;
    public static final float SIDE_HEIGHT = 3f;
    public static final float DENSITY = 0.35f;
    public static final float RESTITUTION = 0.1f;
    public static final float FRICTION = 0f;

    public Body body;

    public Player(World world) {
        super(world);
    }

    public Player(World world, float xi, float yi) {
        super(world);
        /**
         * Creates Main ExpectsInput Body, controlled by Input
         */
        BodyDef bodyDefMain = defineBody(BODY_TYPE, xi,yi,FIXED_ROTATION);
        PolygonShape shapeMain = new PolygonShape();
        shapeMain.setAsBox(SIDE_WIDTH/2,SIDE_HEIGHT/2);
        FixtureDef fixtureDefMain = defineFixture(shapeMain,DENSITY,RESTITUTION,FRICTION);

        body = createBody(bodyDefMain,fixtureDefMain);
    }

}
