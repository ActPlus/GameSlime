package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.GameScreen;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class WithTarget extends Entity {

    private Entity target;

    public WithTarget(GameScreen screen) {
        super(screen);
    }


    public abstract void beforeAction();

    public abstract void action();

    public abstract void afterAction();
}
