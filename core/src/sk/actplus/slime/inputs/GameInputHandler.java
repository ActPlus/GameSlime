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

    public void handle(float delta) {

        /**
         * If GAME OVER, waiting for input to Restart
         */
        if (screen.gameover) {
            if (Gdx.input.justTouched()) {
                screen.newGame(screen.batch);
            }
        } else if (PlayScreen.paused) {
            /**
             * If Paused waiting for input for unpausing
             */
            if (Gdx.input.justTouched()) {
                screen.gui.touchToStartLabel.setVisible(false);
                screen.gui.validate();
                screen.paused = false;
            }

        } else {

            //Basic speed in NORMAL MODE
            float horizontalForce = PLAYER_SPEED;

            // Instant Game Over
            if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                screen.gameOver();
            }

            if (DEBUG) {
                /**
                 * If in DEBUG mode, you can move with Player
                 */
                horizontalForce = 0;

                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    horizontalForce = PLAYER_SPEED;
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    horizontalForce = -PLAYER_SPEED;
            }


            /**
             * If touched then jump
             */
            if (Gdx.input.justTouched() && !screen.jumped) {
                screen.zoomState = 1;
                screen.jumped = true;
                //PPM = PPM/3f;
                //screen.camera.update();
                screen.player.body.applyForceToCenter(0, PLAYER_JUMP, false);


            }

            /**
             * Apply Horizontal Velocity
             * */
            screen.player.body.setLinearVelocity(horizontalForce, screen.player.body.getLinearVelocity().y);
        }
    }
}
