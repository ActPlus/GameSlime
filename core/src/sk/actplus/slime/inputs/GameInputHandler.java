package sk.actplus.slime.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import sk.actplus.slime.screens.PlayScreen;

import static sk.actplus.slime.constants.Values.*;

/**
 * Created by timla on 26.1.2017.
 */

public class GameInputHandler {
    PlayScreen screen;
    public GameInputHandler(PlayScreen screen) {
        this.screen = screen;
    }

    public Entity getPlayer(){
        return screen.player;
    }

    public void handle(float delta) {

        float horizontalForce = 1f;

        if (DEBUG) {
            // testovanie pociatocna sila horizontalna
            horizontalForce = 0;
        }
            // testovanie gameover
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            screen.gameOver();
        }

        // testovanie posun v pravo/vlavo
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            horizontalForce = 2;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            horizontalForce = -2;


        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.justTouched()) {

            screen.player.body.applyForceToCenter(0, 500, false);



        screen.player.body.setLinearVelocity(horizontalForce*5, screen.player.body.getLinearVelocity().y);

    }
}
