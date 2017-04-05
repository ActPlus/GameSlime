package sk.actplus.slime.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import sk.actplus.slime.screens.PlayScreen;
import sk.actplus.slime.screens.PlayScreenTemp;

import static sk.actplus.slime.constants.Values.*;

/**
 * Created by timla on 26.1.2017.
 */

public class GameInputHandler {
    PlayScreenTemp screen;

    public GameInputHandler(PlayScreenTemp screen) {
        this.screen = screen;
    }

    /*public Entity getPlayer(){
        return screen.player;
    }*/

    public void handle(float delta) {

        float horizontalForce = 2f;

        /*if (DEBUG) {
            //System.out.println(Gdx.input.isKeyPressed(Input.Keys.UP));
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {

                screen.camera.position.set(screen.camera.position.x,screen.camera.position.y+0.5f,0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                screen.camera.position.set(screen.camera.position.x,screen.camera.position.y-0.5f,0);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                screen.camera.position.set(screen.camera.position.x+0.5f,screen.camera.position.y,0);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                screen.camera.position.set(screen.camera.position.x-0.5f,screen.camera.position.y,0);
            }
        }*/

        // testovanie gameover
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            //   screen.gameOver();
        }

        // testovanie posun v pravo/vlavo
        /*if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            horizontalForce = 2;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            horizontalForce = -2;*/


        if (Gdx.input.justTouched()) {
               screen.player.body.applyForceToCenter(0, 1000, false);
        }

        screen.player.body.setLinearVelocity(horizontalForce*10, screen.player.body.getLinearVelocity().y);
        screen.camera.position.set(screen.player.body.getPosition().x,screen.player.body.getPosition().y,0);
        screen.camera.update();
    }
}
