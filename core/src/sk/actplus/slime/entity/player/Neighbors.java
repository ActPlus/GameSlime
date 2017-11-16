package sk.actplus.slime.entity.player;

import com.badlogic.gdx.physics.box2d.Body;

import sk.actplus.slime.other.BodyArray;

/**
 * Created by Ja on 14.11.2017.
 */

public class Neighbors {
    public Body body;
    public BodyArray neighbors;

    public Neighbors(Body body) {
        this.body = body;
        neighbors = new BodyArray();
    }
}
