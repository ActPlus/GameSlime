package sk.actplus.slime.version2;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entity.EntityArray;
import sk.actplus.slime.version2.entity.PolygonRenderer;
import sk.actplus.slime.version2.entity.friendly.Player;
import sk.actplus.slime.version2.entity.mapentity.Triangle;

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
    GameArray array;
    PolygonSpriteBatch polyBatch;
    Player player;
    Camera camera;
    private Vector2 deltaTranslation;

    public
    Game(GameScreen screen, InputMultiplexer mux, OrthographicCamera camera) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mux = mux;
        this.camera = camera;
        entities = new EntityArray();
        array = new GameArray();
        mapGen= new MapGenerator(screen,array.triangles,new Vector2[]{new Vector2(-2,2),new Vector2(3,3)},new Vector2(2,-3));
        polyBatch = new PolygonSpriteBatch();

        player= new Player(screen,mux);

        deltaTranslation = new Vector2(0,0);

        entities.add(player);
        paused = false;
    }




    public void render(float delta) {
        entities.render(delta);
        polyBatch.begin();

        //render of all triangles in array
        for (int i = 0; i < array.triangles.size ;i++){
            array.triangles.get(i).render(delta,polyBatch);
        }


        player.getPolygonRenderer().getPolygonSprite().draw(polyBatch);


        polyBatch.end();

    }

    private int index = 0;
    private float dt = 0;
    public void update(float delta) {
        player.getPolygonRenderer().update(player.getOutlineArray());
        dt+=delta;
        entities.update(delta);
        if(dt>=0.5) {
            index++;
            dt =0;
            array.triangles.add(mapGen.generate(mapGen.last,array.triangles));
        }

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

    public GameArray getGameArray(){return array;}
}
