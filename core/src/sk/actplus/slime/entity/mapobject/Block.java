package sk.actplus.slime.entity.mapobject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static sk.actplus.slime.constants.Values.PPM;

/**
 * Created by Kobra on 4.4.2017.
 */


public class Block {
    public Body body;
    public static final short CATEGORY = 0x0100;

    public Block (World world, int x, int y){


        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        float nextX = (x);
        float nextY = (y);
        def.position.set(nextX,nextY);

        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f,0.5f);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 0.4f;
        fixDef.friction=0f;
        //fixDef.filter.categoryBits = CATEGORY;
        //fixDef.filter.maskBits = (short)~CATEGORY;
        body.createFixture(fixDef);

        shape.dispose();

    }

    public int getXi() {
        return (int)(body.getPosition().x);
    }

    public int getYi() {
        return (int)(body.getPosition().y);
    }
}
