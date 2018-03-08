package sk.actplus.slime.version2.input;

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
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        System.out.println("Pressed");
        player.bodies.setLinearVelocity(0,0);
        player.bodies.applyForceToCenter(0,0.2f,false);
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
