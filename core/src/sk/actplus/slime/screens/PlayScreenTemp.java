package sk.actplus.slime.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import box2dLight.RayHandler;
import sk.actplus.slime.entity.Entity;
import sk.actplus.slime.entity.mapobject.Block;
import sk.actplus.slime.entity.player.Jelly;
import sk.actplus.slime.entity.player.Player;
import sk.actplus.slime.inputs.GameInputHandler;

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

    GameInputHandler input;

    int currentX;
    int currentY;

    public PlayScreenTemp(SpriteBatch batch) {

        this.batch = batch;

        camera = new OrthographicCamera();
        camera.position.set(0,2,0);
        camera.update();

        world = new World(GRAVITY,false);

        input = new GameInputHandler(this);

        b2ddr = new Box2DDebugRenderer();

        //b2ddr.setDrawJoints(false);
        for (int i = -50; i < 50; i++) {
            Block firstBlock = new Block(world,i,(int)(Math.sin(i*32)*4));
        }

        ChainShape ground = new ChainShape();
        ground.createChain(new Vector2[]{new Vector2(-50,-10),new Vector2(50,-10)});

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = ground;
        groundFixtureDef.friction = 0.99f;
        groundFixtureDef.restitution = 0f;
        
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0,-10);

        world.createBody(groundBodyDef).createFixture(groundFixtureDef);


       player = new Jelly(world,0,2);

    }

    @Override
    public void render(float delta) {


        update(Gdx.graphics.getDeltaTime());

        /**RENDER*/
        b2ddr.render(world,camera.combined);
    }

    public void update(float delta) {

        input.handle(delta);
        //enemies.update();
        world.step(1/60f, 6, 2);
        //cameraUpdate();
        camera.update();
        checkGeneration();
        checkGameOver();

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

    private void checkGeneration() {
        /**Mazanie objektov ked su daleko**//*
        Block block = (Block) blocks.get(0);
        if (block.body.getPosition().x<=(camera.position.x-Gdx.graphics.getWidth()/6)/PPM - BLOCK_SIZE*1){
            SCORE++;

            blocks.remove(block);
            world.destroyBody(block.body);
        }

        Random rand = new Random();
        // pridavanie blockov do sveta
        if ((camera.position.x + Gdx.graphics.getWidth()/6)/PPM + BLOCK_SIZE*1 >= (((Block) blocks.get(blocks.size() - 1)).body.getPosition().x)) {
            blocks.add(new Block(world, currentX, currentY));

            if (rand.nextInt(10) == 0) {
                if (rand.nextInt(2)==0 ) {
                    enemies.add(new Lumpi(world,currentX,currentY+1f));

                } else {
                    new Spike(world, currentX, currentY + 0.5f + Spike.Height / 2f);
                }
            }
            currentX++;
            currentY += RandomGen.generate(false,randomGenControl);
        }*/
    }



    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 12;
        camera.viewportHeight = height / 12;
        camera.update();
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
    }

}

