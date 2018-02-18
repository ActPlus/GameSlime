package sk.actplus.slime.version2.entities;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Ja on 17.2.2018.
 */

public class BlackHole extends Entity{
    //mask ill ignore collision
    private short mask;
    private float strength;

    private GravitationalField gravity;

    public BlackHole(World world) {
        super(world);
    }

    public void checkJoints(){

    }

    public void handleObjectInField() {

    }
}
