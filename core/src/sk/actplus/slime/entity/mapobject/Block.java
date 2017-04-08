package sk.actplus.slime.entity.mapobject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.other.LogicalOperations;

import static sk.actplus.slime.constants.Files.SPRITE_BLOCK;
import static sk.actplus.slime.constants.Files.SPRITE_BLOCK_TRAP;
import static sk.actplus.slime.constants.Values.BLOCK_COOKIE_USER_DATA;
import static sk.actplus.slime.constants.Values.BLOCK_TRAP_USER_DATA;
import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Kobra on 4.4.2017.
 */


public class Block {
    public Body body;
    static Random rand = new Random();
    BodyArray blocks;

    public Block(World world, BodyArray blocks, int x, int y, Sprite sprite, String userData) {
        try {
            this.blocks = blocks;
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.StaticBody;
            def.position.set(x, y);
            blocks.add(body = world.createBody(def));

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(0.5f, 0.5f);

            sprite.setBounds((x - 0.5f) * PPM + WIDTH_CLIENT / 2f, (y - 0.5f) * PPM + HEIGHT_CLIENT / 2f, PPM, PPM);

            FixtureDef fixDef = new FixtureDef();
            fixDef.shape = shape;
            fixDef.restitution = 0.4f;
            fixDef.friction = 0f;
            body.createFixture(fixDef);
            body.setUserData(sprite);

            for (Fixture fixture : body.getFixtureList()) {
                fixture.setUserData(userData);
            }

            shape.dispose();

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();

        }
    }

    public static Block newRandomBlock(World world, BodyArray blocks, int x, int y) {
        Sprite sprite = SPRITE_BLOCK;
        String userData = BLOCK_USER_DATA;


        int randomInt = rand.nextInt(300);

        //3% chance to generate trap block
        if (LogicalOperations.isBetween(randomInt, 0, 3)) {
            sprite = SPRITE_BLOCK_TRAP;
            userData = BLOCK_TRAP_USER_DATA;
        }

        return new Block(world, blocks, x, y, sprite, userData);
    }
}
