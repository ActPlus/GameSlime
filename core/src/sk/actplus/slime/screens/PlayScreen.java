package sk.actplus.slime.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import sk.actplus.slime.inputs.GameInputHandler;

import static sk.actplus.slime.constants.Values.*;


/**
 * Created by timla on 26.1.2017.
 */

public class PlayScreen implements Screen {

    public SpriteBatch batch;
    public OrthographicCamera camera;



    /**
     * Box2D Declaration
     */
    public World world;
    public Box2DDebugRenderer b2ddr;

    public PlayScreen(SpriteBatch batch){
        this.batch = batch;

        //constractors camera world player ememies (everything else needed in playscreen)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH_CLIENT/2, HEIGHT_CLIENT/2);

        world = new World(GRAVITY,false);


    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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
    public void dispose() {

    }
}
