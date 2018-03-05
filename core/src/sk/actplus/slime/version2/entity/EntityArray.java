package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sk.actplus.slime.version2.entity.mapentity.Triangle;

/**
 * Created by Ja on 17.2.2018.
 */

public class EntityArray extends Array<Entity> {

    public void update(float delta) {
        for (int index = 0; index < this.size-1; index++) {
            Entity entity = get(index);
            if(entity.setToDestroy && !entity.destroyed) {
                //destroy(index);
            } else {
                entity.update(delta);
            }
        }
    }

    public void render(float delta, PolygonSpriteBatch polyBatch) {
        for (Entity entity:this) {
            entity.render(delta,polyBatch);
        }
    }

    public void destroy(int index) {
        //TODO FATAL EXEPTION
        //get(index).destroy();
    }

    public void destroy() {
        for (Entity entity:this) {
            //TODO remove from list
        }
    }

    public Vector2[] getArrayOfAllSides() {
        Vector2[] array = new Vector2[this.size*3];
        for (int i = 0; i < size; i++) {
            Vector2[] triangle = ((Triangle)(get(i))).getVertices();
            for (int j = 0; j < 3; j++) {
                array[i+j] = triangle[j];
            }
        }
        return array;
    }

    //TODO FATAL EXEPTION
    /*public Entity get(int index) {
        return get(index);
    }*/


}
