package sk.actplus.slime.version2.entity.enemy;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.GameScreen;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class Enemy extends sk.actplus.slime.version2.entity.Entity {


    public Enemy(GameScreen screen) {
        super(screen);
    }

    @Override
    public void handleCollision(short collisionBIT) {

    }

    @Override
    public void render(float delta) {

    }

    public abstract void action();
}
