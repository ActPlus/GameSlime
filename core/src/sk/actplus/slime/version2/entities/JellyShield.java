package sk.actplus.slime.version2.entities;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.Shield;

/**
 * Created by Ja on 17.2.2018.
 */

public class JellyShield extends Player {
    Shield shield;

    public JellyShield(World world) {
        super(world);
    }

    @Override
    public void render() {
        super.render();
        shield.render();
    }

    @Override
    public void update() {
        super.update();
        shield.update();
    }
}
