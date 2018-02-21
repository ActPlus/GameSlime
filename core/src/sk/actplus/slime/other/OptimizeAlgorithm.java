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

    public BodyArray checkIfCanBeOptimized(World world, BodyArray blocks){
        /** TODO: Nacitaj posledny block a predposledny*/
        Body last, prev;
        /** Horizontalna kontrola suradnic*/
        //if (horizontalCheck(last, prev))

        //Ak true

        //new pos = calcNewpos(last.x, prev.x, last.width,prev.width, last.y)

        deleteBoth();
        //create new one with new posx and same posy and size last.width+prev.width



        /** Vertikalna kontrola suradnic*/
        //if(horizontalCheck(last, prev))

        //ak true

        //new pos = calcNewpos(last.x, prev.x, last.width,prev.width, last.y)

        deleteBoth();
        //create new one with new posy and same posx and size last.height+prev.height

        return blocks;
    }

    private float calcNewPos(float pos1, float pos2, float size1, float size2, float sizeOtherDimension) {
        /*
         Vazeny priemer..
         sumOfPos = size * pos1 + pos2

         newxPos = sumOfPos / (size1+size2);

          */
        //return new pos
        return 0;

    }

    private BodyArray deleteBoth(){
        //zmaze z world aj z bodyarray
        return null;
    }



    private boolean horizontalCheck(Body last, Body previous) {
        //compare(last.pos.x,previous.pos.x);
        return true;
    }

    private boolean verticalCheck(Body last, Body previous) {
        //compare(last.pos.y,previous.pos.y);
        return true;
    }

    private boolean compare(float value1, float value2) {
        return value1 == value2;
    }


    // Last version
    public void optimizeWorld(World world, BodyArray bodies) {
        /**
         * Everything in vertical and horizontal array will be merged into one body
         */



//        if (bodies.size > 1) {
//            int lastIndex = bodies.size - 1;
//            float x, y, width, height, width1;
//            if ((bodies.get(lastIndex).getPosition().y == bodies.get(lastIndex - 1).getPosition().y)) {
//                width1 = ((Sprite) (bodies.get(lastIndex - 1).getUserData())).getWidth();
//                //height1 = ((Sprite) (bodies.get(lastIndex - 1).getUserData())).getHeight();
//
//                width = width1 + ((Sprite) (bodies.get(lastIndex).getUserData())).getWidth();
//                height = 1.1f;
//                x = (bodies.get(lastIndex).getPosition().x + bodies.get(lastIndex - 1).getPosition().x - width1/2f +0.5f) / 2f;
//                y = bodies.get(lastIndex).getPosition().y;
//
//
//                for (int i = 0; i < 2; i++) {
//                    world.destroyBody(bodies.get(lastIndex - i));
//                    bodies.removeIndex(lastIndex - i);
//                }
//
//
//                bodies.add(new Block(world, bodies, x, y, width, height, SPRITE_BLOCK, BLOCK_USER_DATA).body);
//
//                //System.out.println(((Sprite)bodies.get(bodies.size-1).getUserData()).getWidth());
//            }
//        }
    }
}
