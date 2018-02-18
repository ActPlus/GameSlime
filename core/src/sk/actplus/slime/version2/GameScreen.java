package sk.actplus.slime.version2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entities.EntityArray;
import sk.actplus.slime.version2.entities.Player;

import static sk.actplus.slime.constants.Values.WORLD_STEP;

/**
 * Created by Ja on 17.2.2018.
 */

public class GameScreen implements Screen{
    private InputMultiplexer mux;
    private World world;
    private Box2DDebugRenderer b2ddr;
    private MovableCamera camera;
    private Game game;

    private final Vector2 GRAVITY = new Vector2(0,-10);
    public static float PPM = 32;
    private final float CLIENT_WIDTH = Gdx.graphics.getWidth();
    private final float CLIENT_HEIGHT = Gdx.graphics.getHeight();


    public GameScreen() {
        world = new World(GRAVITY,false);
        camera = new MovableCamera(0,0,CLIENT_WIDTH,CLIENT_HEIGHT);
        b2ddr = new Box2DDebugRenderer();
        game = new Game(world);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.render();
        b2ddr.render(world,camera.combined);
        update();
    }

    public void update() {
        game.update();
        world.step(WORLD_STEP * ((game.isPaused()) ? 0 : 1), 6, 2);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        game.pause();
    }

    @Override
    public void resume() {
        game.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2ddr.dispose();
        game.dispose();
    }
}
