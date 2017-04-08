package sk.actplus.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
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

import static sk.actplus.slime.constants.Values.GRAVITY;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;
import static sk.actplus.slime.constants.Values.WORLD_STEP;
import static sk.actplus.slime.constants.Values.finalPPM;

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

    Texture textureBlock  = new Texture("textures/brick.png");
    Texture textureTrap = new Texture("textures/trap.png");

    Random rand = new Random();


    public PlayScreen(SpriteBatch batch) {
        newGame(batch);

        movingBlocks.add(new MovingBlock(world,WIDTH_CLIENT/PPM, HEIGHT_CLIENT/2/PPM-1, 2f, 10f, new Vector2(rand.nextInt(20)-40,0)).body);
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
                sprite.setBounds((body.getPosition().x-0.5f - camera.position.x)*PPM+WIDTH_CLIENT/2f, (body.getPosition().y-0.5f - camera.position.y)*PPM+HEIGHT_CLIENT/2f,PPM,PPM);
                sprite.draw(batch);
            }
            /*

            float y=(body.getPosition().y-0.5f - camera.position.y)*PPM+HEIGHT_CLIENT/2f;
            int nums = 1;
            while (y-nums*PPM > (camera.position.y*PPM-HEIGHT_CLIENT)) {
                sprite = new Sprite(textureBlock);
                sprite.setBounds((body.getPosition().x-0.5f - camera.position.x)*PPM+WIDTH_CLIENT/2f, y-nums*PPM,PPM,PPM);
                sprite.draw(batch);
                nums++;
            }
            */
        }

        batch.end();

        b2ddr.render(world,camera.combined);
        gui.render(delta);
    }

    public void update(float delta) {

        world.step(WORLD_STEP * ((paused) ? 0 : 1), 6, 2);


        Filter filter = new Filter();
        filter.categoryBits = 0x1000;
        filter.maskBits = (short)~0x1000;
        if (destroyBodies.size > 0) {
            for (int i = 0; i < destroyBodies.size; i++) {
                destroyBodies.get(i).getFixtureList().get(0).setFilterData(filter);

            }
        }
        checkGameOver();
        camera.cameraUpdate();
        mapGenerator.update();
        input.handle(delta);
        enemies.update();

    }

    public void gameOver() {
        gui.gameOver();
        gameover = true;
        paused = true;
    }

    public void newGame(SpriteBatch batch) {
        score=0;
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
        world.setContactListener(new CollisionListener(this,gui));
        input = new GameInputHandler(this);
        b2ddr = new Box2DDebugRenderer();
        //b2ddr.setDrawJoints(false);

        rayHandler = new RayHandler(world);
        player = new Jelly(world, 0, 15);
        camera = new MovableCamera(player.body, new Vector2(0, 2));
        camera.viewportWidth = WIDTH_CLIENT / PPM;
        camera.viewportHeight = HEIGHT_CLIENT / PPM;
        mapGenerator = new MapGenerator(world, player, camera, new Vector2(-WIDTH_CLIENT / 2, 0), blocks, lights, movingBlocks, enemies, rayHandler);
        fps = new FpsCounter(gui);
        jumped= false;
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
        textureTrap.dispose();
        textureBlock.dispose();
    }

}

