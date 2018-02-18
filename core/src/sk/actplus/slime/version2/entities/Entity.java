package sk.actplus.slime.version2.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class Entity {
    private Vector2 position;

    public Entity(World world) {

    }

    public void handleCollision() {

    }

    public void render() {

    }

    public void update() {

    }

    public void suicide() {
        System.out.println("Entity: TODO: kill body");
    }
}
