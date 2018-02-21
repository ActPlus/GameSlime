package sk.actplus.slime.version2;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entity.EntityArray;
import sk.actplus.slime.version2.entity.PolygonGenerator;
import sk.actplus.slime.version2.entity.friendly.Player;

/**
 * Created by Ja on 17.2.2018.
 */

public class Game {
    protected EntityArray entities;
    GameArray array;
    PolygonGenerator polygonGenerator;
    private World world;
    private GameScreen screen;
    private MapGenerator mapGen;
    private boolean paused;
    private String savePath;
    private InputMultiplexer mux;
    private int index = 0;
    private float dx = 0;



    public
    Game(GameScreen screen, InputMultiplexer mux) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.mux = mux;
        entities = new EntityArray();
        array = new GameArray();
        mapGen= new MapGenerator(screen,array.triangles,new Vector2[]{new Vector2(-7,-4),new Vector2(2,-6)},new Vector2(6,0));

        Player player= new Player(screen,mux);

        entities.add(player);
        paused = false;


        System.out.println(Player.getArrayOfVertices());
        polygonGenerator = new PolygonGenerator(Player.getArrayOfVertices(),48,Color.BLUE);

        //array.polygonGenerators.add(new PolygonGenerator(array.triangles.get(index).getArrayOfVertices(),3, Color.CHARTREUSE));
    }

    public void render(float delta) {
        entities.render(delta);
        polygonGenerator.render();
        for (int i = 0; i <= index; i++){
            //array.polygonGenerators.get(i).render();
        }


    }

    public void update(float delta) {
        dx+=delta;
        entities.update(delta);
        if(dx>=2) {
            index++;
            dx =0;
            System.out.println("Generating");
            //array.triangles.add(mapGen.generate(mapGen.last,array.triangles));
            //array.polygonGenerators.add(new PolygonGenerator(array.triangles.get(index).getArrayOfVertices(),3, Color.CHARTREUSE));
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
}
