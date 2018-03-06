package sk.actplus.slime.version2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.version2.entity.mapentity.Triangle;
import sk.actplus.slime.version2.gui.DebugInGameGUI;
import sk.actplus.slime.version2.gui.GUI;

import static sk.actplus.slime.constants.Values.WORLD_STEP;

/**
 * Created by Ja on 17.2.2018.
 */

public class GameScreen implements Screen{
    private final Vector2 GRAVITY = new Vector2(0,-10);
    public static float PPM = 32;
    public static final float CLIENT_WIDTH = Gdx.graphics.getWidth();
    public static final float CLIENT_HEIGHT = Gdx.graphics.getHeight();

    protected InputMultiplexer muxInput;
    private World world;
    private Box2DDebugRenderer b2ddr;
    protected MovableCamera camera;
    private Game game;
    private GUI gui;
    private PolygonSpriteBatch polygonBatch;




    public GameScreen() {
        world = new World(GRAVITY,false);
        camera = new MovableCamera(0,0,CLIENT_WIDTH/PPM,CLIENT_HEIGHT/PPM);
        b2ddr = new Box2DDebugRenderer();
        muxInput = new InputMultiplexer();
        Gdx.input.setInputProcessor(muxInput);
        game = new Game(this,muxInput,camera);
        polygonBatch = new PolygonSpriteBatch();
        //TODO GUI
        //gui = new DebugInGameGUI();


    }

    @Override
    public void show() {
        game.load();
    }

    @Override
    public void render(float delta) {
        game.render(delta);
        b2ddr.render(world,camera.combined);
        update(delta);
    }

    public void update(float delta) {
        world.step(WORLD_STEP * ((game.isPaused()) ? 0 : 1), 6, 2);
        //SET UPS Updates per second to be fixed on all devices win...
        game.update(delta);
    }

    public void addInputProcessor(InputProcessor processor) {
        muxInput.addProcessor(processor);
    }

    public void removeInputProcessor(InputProcessor processor) {
        muxInput.removeProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        camera.resize(width,height);
        //todo scaling of all objects
        //gui.resize(width,height);
    }

    @Override
    public void pause() {
        game.pause();
        game.save();
    }

    @Override
    public void resume() {
        game.resume();
        game.load();
    }

    @Override
    public void hide() {
        game.save();
    }

    @Override
    public void dispose() {
        world.dispose();
        b2ddr.dispose();
        game.dispose();

    }

    public World getWorld() {
        return world;
    }

}
