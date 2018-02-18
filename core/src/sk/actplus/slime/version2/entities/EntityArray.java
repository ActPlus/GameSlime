package sk.actplus.slime.version2.entities;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Ja on 17.2.2018.
 */

public class EntityArray extends Array<Entity> {

    public void update() {
        for (Entity entity:this) {
            entity.update();
        }
    }

    public void render() {
        for (Entity entity:this) {
            entity.render();
        }
    }

    public void suicide(int index) {
        get(index).suicide();
    }

    public void suicide() {
        for (Entity entity:this) {
            entity.render();
        }
    }

    public Entity get(int index) {
        return get(index);
    }


}
