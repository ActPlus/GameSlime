package sk.actplus.slime.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import sk.actplus.slime.inputs.GameInputHandler;

import static sk.actplus.slime.constants.Values.GRAVITY;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Ja on 6.4.2017.
 */

public class ScreenMapEditor implements Screen {
    /**
     * Created by timla on 26.1.2017.
     */


        public SpriteBatch batch;
        public OrthographicCamera camera;



        /**
         * Box2D Declaration
         */

        public ScreenMapEditor(SpriteBatch batch){
            this.batch = batch;

        }

        public void update(float delta) {

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

