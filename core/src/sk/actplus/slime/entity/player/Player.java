package sk.actplus.slime.entity.player;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;


import sk.actplus.slime.entity.Entity;

import static sk.actplus.slime.constants.Values.PPM;

/**
 * Created by Admin on 21.12.2016.
 */

public class Player extends Entity {
    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.DynamicBody;
    public static final float PLAYER_WIDTH =  PPM/3f*2f;
    public static final float PLAYER_HEIGHT =  PPM/3f*4f;
    public static final float DENSITY = 2f;
    public static final float RESTITUTION = 0.1f;
    public static final float FRICTION = 0f;


    public Player(World world, float xi, float yi) {
        super(world, BODY_TYPE, xi, yi);
    }
}
