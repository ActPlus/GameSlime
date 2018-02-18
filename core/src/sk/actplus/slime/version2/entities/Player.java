package sk.actplus.slime.version2.entities;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.InputHandler;

/**
 * Created by Ja on 17.2.2018.
 */

public class Player extends Entity {

    private int score;
    private InputHandler inputHandler;

    public Player(World world) {
        super(world);
    }
}
