package sk.actplus.slime.version2;

import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entities.Entity;
import sk.actplus.slime.version2.entities.EntityArray;

/**
 * Created by Ja on 17.2.2018.
 */

public class Game {
    private World world;
    private EntityArray entities;
    private MapGenerator mapGen;

    private boolean paused;

    public Game(World world) {
        this.world = world;
        entities = new EntityArray();

        paused = false;
    }



    public void render() {

    }

    public void update() {

    }

    public void dispose() {

    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        save();
        paused = true;
    }

    public void resume() {

        load();
        paused = false;
    }

    public void save() {
         //TODO: Save to file method
    }

    public void load() {
        //TODO: load from file method
    }
}
