package sk.actplus.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import box2dLight.RayHandler;
import sk.actplus.slime.entity.mapobject.MovingBlock;
import sk.actplus.slime.entity.player.Jelly;
import sk.actplus.slime.inputs.GameInputHandler;
import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.other.CollisionListener;
import sk.actplus.slime.other.EnemyArray;
import sk.actplus.slime.other.FpsCounter;
import sk.actplus.slime.other.LightArray;
import sk.actplus.slime.other.MapGenerator;
import sk.actplus.slime.other.MovableCamera;
import sk.actplus.slime.other.TriGen;

import static sk.actplus.slime.constants.Values.GRAVITY;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;
import static sk.actplus.slime.constants.Values.WORLD_STEP;

/**
 * Created by timol on 5.12.2016.
 */

public class PlayScreen implements Screen {

    /**
     * Variable Declaration
     */
    public static boolean paused;
    public static boolean gameover;
    public boolean jumped;
    public int score;

    /**
     * Box2D Declaration
     */
    public SpriteBatch batch;
    Sprite sprite;
    public MovableCamera camera;


    public World world;
    public Box2DDebugRenderer b2ddr;
    public RayHandler rayHandler;
    MapGenerator mapGenerator;
    FpsCounter fps;

    GameInputHandler input;
    public BodyArray blocks;
    BodyArray movingBlocks;
    public BodyArray destroyBodies;
    EnemyArray enemies;
    LightArray lights;
    public Jelly player;

    public GUI gui = new GUI();

    Random rand = new Random();

    double ellapsedTime;

    public byte zoomState;


    public PlayScreen(SpriteBatch batch) {
        newGame(batch);

        new MovingBlock(world,WIDTH_CLIENT/PPM, HEIGHT_CLIENT/2/PPM-1, new Vector2(rand.nextInt(20)-40,0),"lefter");
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        fps.count();


        /**RENDER*/
        Gdx.gl.glClearColor(0,0,0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        batch.begin();
        for (Body body: blocks) {


            if (body.getUserData() != null) {
                sprite = (Sprite) body.getUserData();
                sprite.setBounds(((body.getPosition().x-0.5f - camera.position.x)*PPM/camera.zoom+WIDTH_CLIENT/2f), ((body.getPosition().y-0.5f- camera.position.y)*PPM/camera.zoom+HEIGHT_CLIENT/2f),PPM/camera.zoom,PPM/camera.zoom);
                sprite.draw(batch);
            }
        }

        batch.end();

        b2ddr.render(world,camera.combined);
        gui.render(delta);
    }

    public void update(float delta) {
        //world.setGravity(GRAVITY);
        world.step(WORLD_STEP * ((paused) ? 0 : 1), 6, 2);
        //world.setGravity(GRAVITY);
        checkGameOver();

        camera.cameraUpdate();
        //mapGenerator.update(world,blocks,camera,enemies,player,lights,rayHandler);


        input.handle(delta);
        enemies.update();


        ellapsedTime++;

       // world.setGravity(GRAVITY);
        /*if (zoomState==1) {
            camera.zoomOut();
        } else {

            if (camera.zoom >= 1) {
                camera.zoomIn();
                //camera.zoom = 1;
            }

        }*/
    }

    public void gameOver() {
        gui.gameOver();
        gameover = true;
        paused = true;
    }

    public void newGame(SpriteBatch batch) {
        score=0;
        ellapsedTime=0;
        paused = true;
        gameover = false;
        this.batch = batch;
        blocks = new BodyArray();
        destroyBodies = new BodyArray();
        enemies = new EnemyArray();
        lights = new LightArray();
        movingBlocks = new BodyArray();

        gui.create();
        gui.updateFps(0);
        gui.updateScore(0);

        world = new World(GRAVITY, false);
        world.setContactListener(new CollisionListener(this,gui,blocks));
        input = new GameInputHandler(this);
        b2ddr = new Box2DDebugRenderer();
        b2ddr.setDrawJoints(false);

        rayHandler = new RayHandler(world);
        player = new Jelly(world, 0, 15);
        camera = new MovableCamera(player.body, new Vector2(0, 2),WIDTH_CLIENT/PPM, HEIGHT_CLIENT/PPM);
        mapGenerator = new MapGenerator(world, camera, new Vector2(-WIDTH_CLIENT / 2, 0), blocks, lights, rayHandler);
        fps = new FpsCounter(gui);
        jumped= false;
        zoomState = 0;


        TriGen.generateMore(world,250);

    }

    private void checkGameOver() {
        if ((player.body.getPosition().x<= (camera.position.x - WIDTH_CLIENT/2f/PPM))){
            gameOver();
        }
    }

    @Override
    public void resize(int width, int height) {
        WIDTH_CLIENT = width;
        HEIGHT_CLIENT = height;

        camera.viewportWidth = width / PPM;
        camera.viewportHeight = height / PPM;
        camera.update();
        gui.resize(width, height);
    }

    @Override
    public void pause() {
        paused = true;
        paused = true;
        gui.pause();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        paused = true;
        gui.pause();
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        world.dispose();
        b2ddr.dispose();
        batch.dispose();
        gui.dispose();
        rayHandler.dispose();
    }

}

