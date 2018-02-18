package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.utils.Array;

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

    public void render(float delta) {
        for (Entity entity:this) {
            entity.render(delta);
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

    //TODO FATAL EXEPTION
    /*public Entity get(int index) {
        return get(index);
    }*/


}
