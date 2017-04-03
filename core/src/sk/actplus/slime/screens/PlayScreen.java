package sk.actplus.slime.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
    private GameInputHandler gameInputHandler;

    public PlayScreen(SpriteBatch batch){
        this.batch = batch;
        b2ddr = new Box2DDebugRenderer();
        gameInputHandler = new GameInputHandler(this);

        //constractors camera world player ememies (everything else needed in playscreen)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH_CLIENT/2, HEIGHT_CLIENT/2);

        world = new World(GRAVITY,false);

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(0/PPM,0/PPM);
        camera.position.set(0,0,0);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body firstBody = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1*PPM,1*PPM);
        fixtureDef.shape = shape;
        firstBody.createFixture(fixtureDef);



    }


    @Override
    public void show() {

    }

    public void update(float delta) {
        gameInputHandler.handle(delta);
        camera.update();
        world.step(1/60f,6,2);
    }

    @Override
    public void render(float delta) {
        update(delta);


        b2ddr.render(world,camera.combined);
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
