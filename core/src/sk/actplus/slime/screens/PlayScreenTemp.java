package sk.actplus.slime.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import sk.actplus.slime.entity.Entity;
import sk.actplus.slime.entity.mapobject.Block;
import sk.actplus.slime.entity.player.Jelly;
import sk.actplus.slime.entity.player.Player;
import sk.actplus.slime.inputs.GameInputHandler;
import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.other.LightArray;

import static sk.actplus.slime.constants.Values.GRAVITY;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.DEBUG;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by timol on 5.12.2016.
 */

public class PlayScreenTemp implements Screen {
    public static int randomGenControl;
    public static int SCORE = 0;


    public static int ellapsedTime = 0;


    public SpriteBatch batch;
    public OrthographicCamera camera;

    /**
     * Box2D Declaration
     */
    public World world;
    public Box2DDebugRenderer b2ddr;
    public Jelly player;
    public RayHandler rayHandler;

    GameInputHandler input;
    BodyArray blocks = new BodyArray();
    LightArray lights = new LightArray();

    int currentX;
    int currentY = 0;
    int lastY;

    long startTime = TimeUtils.nanoTime();

    GUI gui = new GUI();

    public PlayScreenTemp(SpriteBatch batch) {

        this.batch = batch;

        camera = new OrthographicCamera();
        camera.position.set(0, 2, 0);
        camera.update();

        world = new World(GRAVITY, false);
        input = new GameInputHandler(this);
        b2ddr = new Box2DDebugRenderer();

        //b2ddr.setDrawJoints(false);

        generateInitialBodies();


        rayHandler = new RayHandler(world);

        player = new Jelly(world, 0, 15);
        gui.create();

    }

    Random rand = new Random();

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */{
            gui.fpsLoggerLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            startTime = TimeUtils.nanoTime();
        }

        /**RENDER*/

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();
        b2ddr.render(world, camera.combined);
        gui.render();


    }

    public void update(float delta) {
        checkIfGenerationNeeded();
        deleteJunkObjects();

        input.handle(delta);
        //enemies.update();
        world.step(1 / 60f, 6, 2);
        //cameraUpdate();
        camera.update();
        //checkGameOver();

    }


    private void cameraUpdate() {/*
        //testovanie
        if (DEBUG) {
            camera.position.set(player.body.getPosition().x * PPM, player.body.getPosition().y * PPM, 0);
        } else {
            // normalny beh aplikacie
            ellapsedTime++;
            if (player.body.getPosition().x * PPM>= ellapsedTime * CAMERA_SPEED) {

                camera.position.set(player.body.getPosition().x * PPM, player.body.getPosition().y * PPM, 0);
            } else {

                camera.position.set(ellapsedTime * CAMERA_SPEED, player.body.getPosition().y * PPM, 0);
            }
        }

        camera.update();*/
    }

    public void gameOver() {/*
        blocks.clear();
        world = new World(GRAVITY, false);
        player = new JellyHash(world, 0, 3);
        input = new GameInputHandler(this);
        currentY = 0;
        for (currentX = 0; currentX < BLOCKS_NUMBER_AT_START; currentX++) {
            currentY += RandomGen.generate(false,randomGenControl);
            blocks.add(new Block(world, currentX, currentY));
        }

        contactListener = new MyContactListener(player, ((Block) blocks.get(0)).body);
        world.setContactListener(contactListener);
        SCORE = 0;*/
    }

    private void checkGameOver() {/*
        Block block = (Block) blocks.get(0);
        if ((player.body.getPosition().x<=block.body.getPosition().x) && (player.body.getPosition().y<=block.body.getPosition().y - BLOCK_SIZE*2)){
            gameOver();
        }*/
    }

    private void checkIfGenerationNeeded() {
        /**
         * Generate new Block if needed
         */
        if (blocks.get(blocks.size-1).getPosition().x < (camera.position.x + WIDTH_CLIENT / PPM / 2.5f)) {
            lastY = currentY;
            int randomNumber = rand.nextInt(100)+1;
            /**
             * If more then 95, MEANS there is 5% chance to go HIGH
             */
            if (randomNumber>=50) {
                if (randomNumber >= 95) {
                    currentY = currentY + rand.nextInt(31) - 15;
                    /**
                     * If random number is between or equals 80 and 95, MEANS 15% chance to go MEDIUM
                     */
                } else if (randomNumber >= 80) {
                    currentY = currentY + rand.nextInt(11) - 5;
                    /**
                     * Between and equals 50 and 85 is LOW, results in 35%
                     */

                } else {
                    currentY = currentY + rand.nextInt(7) - 3;
                }

                if (lastY - currentY < 0) {
                    for (int j = 0; j > lastY - currentY; j--) {
                        blocks.add(new Block(world, currentX, currentY + j).body);
                    }
                } else if (lastY - currentY > 0) {
                    for (int j = 0; j < lastY - currentY; j++) {
                        blocks.add(new Block(world, currentX, currentY + j).body);
                    }
                } else {
                    blocks.add(new Block(world, currentX, currentY).body);
                }
                /**
                 * 0-50, MEANDS 50% PLAIN
                 */
            } else {
                currentY = currentY + rand.nextInt(3) - 1;
                blocks.add(new Block(world, currentX, currentY).body);
            }

            /**
             * 3%chance to generate new light
             */

            /*if (rand.nextInt(100)+1> 97) {
                lights.add(new PointLight(rayHandler, 5000, new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 0.95f), rand.nextInt(20) + 10, currentX+15, currentY+15+rand.nextInt(10)));
            }*/

            currentX++;
        }
    }

    private void generateInitialBodies() {
        currentX = (int) (camera.position.x - WIDTH_CLIENT / PPM / 2f);
        while ((currentX < camera.position.x + WIDTH_CLIENT / PPM / 2)) {
            currentY = currentY + rand.nextInt(3) - 1;
            blocks.add(new Block(world, currentX, currentY).body);
            currentX++;

        }
    }

    private void deleteJunkObjects () {
        /**
         * Delete bodies if out of camera
         */
        int num_of_deleted_bodies = 0;
        int num_of_deleted_lights = 0;
        if ((blocks.size != 0)) {
            while ((blocks.first().getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM / 2.5f))) {
                num_of_deleted_bodies++;
                world.destroyBody(blocks.first());
                blocks.removeIndex(0);
            }
        }

        /**
         * Delete Junk Lights if FAR out of camera
         */

        /*if (lights.size != 0) {
            if ((lights.first().getPosition().x < (camera.position.x - WIDTH_CLIENT / PPM))) {
                num_of_deleted_lights++;
                lights.first().remove();
                lights.removeIndex(0);
            }
        }*/

        if ((num_of_deleted_bodies != 0)||(num_of_deleted_lights != 0)) {
            //System.out.println("Deleted " + num_of_deleted_bodies + " JUNK BODIES and " + num_of_deleted_lights + " JUNK LIGHTS");
            num_of_deleted_bodies = 0;
            num_of_deleted_lights = 0;
        }
    }


    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / PPM;
        camera.viewportHeight = height / PPM;
        camera.update();
        gui.resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

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
    }

}

