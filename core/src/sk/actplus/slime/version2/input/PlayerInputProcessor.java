package sk.actplus.slime.version2.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import sk.actplus.slime.version2.entity.friendly.Player;


/**
 * Created by Ja on 18.2.2018.
 */

public class PlayerInputProcessor implements InputProcessor {

    private Player player;

    public PlayerInputProcessor(Player player) {
        this.player = player;


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            //player.bodies.setLinearVelocity(0,0);
            player.bodies.applyForceToCenter(0.2f,0,false);
        }

            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
              //  player.bodies.setLinearVelocity(0,0);
                player.bodies.applyForceToCenter(-0.2f,0,false);
            }


        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        System.out.println("Pressed");
        player.bodies.setLinearVelocity(0,0);
        player.bodies.applyForceToCenter(0,0.5f,false);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
