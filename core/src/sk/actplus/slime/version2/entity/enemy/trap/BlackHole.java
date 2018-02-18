package sk.actplus.slime.version2.entity.enemy.trap;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.GameScreen;

/**
 * Created by Ja on 17.2.2018.
 */

public class BlackHole extends sk.actplus.slime.version2.entity.Entity {
    //mask ill ignore collision
    private short mask;
    private float strength;

    private GravitationalField gravity;

    public BlackHole(GameScreen screen) {
        super(screen);
    }


    public void checkJoints(){

    }

    public void handleObjectInField() {

    }

    @Override
    public void handleCollision(short collisionBIT) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void render(float delta) {

    }
}
