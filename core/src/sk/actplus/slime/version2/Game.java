package sk.actplus.slime.version2;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entity.EntityArray;
import sk.actplus.slime.version2.entity.friendly.Player;

/**
 * Created by Ja on 17.2.2018.
 */

public class Game {
    private World world;
    private GameScreen screen;
    protected EntityArray entities;
    private MapGenerator mapGen;
    private boolean paused;
    private String savePath;
    private InputMultiplexer mux;

    public
    Game(GameScreen screen, InputMultiplexer mux) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mux = mux;
        entities = new EntityArray();
        mapGen= new MapGenerator(screen,screen.camera,new Vector2[]{new Vector2(-7,-4),new Vector2(2,-6)},new Vector2(6,0));

        Player player= new Player(screen,mux);

        entities.add(player);
        paused = false;

        for (int i = 0; i < 50; i++) {
           entities.add(mapGen.generate(mapGen.last));
        }
    }



    public void render(float delta) {
        entities.render(delta);
    }

    public void update(float delta) {
        entities.update(delta);
    }

    public void dispose() {
        world.dispose();
        screen.dispose();
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
