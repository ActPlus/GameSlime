package sk.actplus.slime.other;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.entity.mapobject.Block;

import static sk.actplus.slime.constants.Files.SPRITE_BLOCK;
import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;
import static sk.actplus.slime.constants.Values.PPM;

/**
 * Created by Ja on 11.4.2017.
 */

public class OptimizeAlgorithm {

    public void optimizeWorld(World world, BodyArray bodies) {
        /**
         * Everything in vertical and horizontal array will be merged into one body
         */

        if (bodies.size > 1) {
            int lastIndex = bodies.size - 1;
            float x, y, width, height, width1;
            if ((bodies.get(lastIndex).getPosition().y == bodies.get(lastIndex - 1).getPosition().y)) {
                width1 = ((Sprite) (bodies.get(lastIndex - 1).getUserData())).getWidth();
                //height1 = ((Sprite) (bodies.get(lastIndex - 1).getUserData())).getHeight();

                width = width1 + ((Sprite) (bodies.get(lastIndex).getUserData())).getWidth();
                height = 1.1f;
                x = (bodies.get(lastIndex).getPosition().x + bodies.get(lastIndex - 1).getPosition().x - width1/2f +0.5f) / 2f;
                y = bodies.get(lastIndex).getPosition().y;


                for (int i = 0; i < 2; i++) {
                    world.destroyBody(bodies.get(lastIndex - i));
                    bodies.removeIndex(lastIndex - i);
                }


                bodies.add(new Block(world, bodies, x, y, width, height, SPRITE_BLOCK, BLOCK_USER_DATA).body);

                //System.out.println(((Sprite)bodies.get(bodies.size-1).getUserData()).getWidth());
            }
        }
    }
}
