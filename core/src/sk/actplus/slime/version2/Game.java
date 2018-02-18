package sk.actplus.slime.version2;

import com.badlogic.gdx.math.Vector2;
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

    public Game(World world, MovableCamera camera) {
        this.world = world;
        entities = new EntityArray();
        mapGen= new MapGenerator(world,camera,new Vector2[]{new Vector2(0,0),new Vector2(2,1)},new Vector2(3,-2));

        paused = false;
    }



    public void render() {
        entities.render();
    }

    public void update() {
        entities = mapGen.generateIfNeeded(entities);
        entities.update();
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
