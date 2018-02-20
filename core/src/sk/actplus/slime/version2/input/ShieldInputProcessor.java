package sk.actplus.slime.version2.input;

import com.badlogic.gdx.InputProcessor;

import sk.actplus.slime.version2.entity.accessory.Shield;

/**
 * Created by Ja on 18.2.2018.
 */

public class ShieldInputProcessor implements InputProcessor {

    private Shield shield;

    public ShieldInputProcessor(Shield shield) {
        this.shield = shield;
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
