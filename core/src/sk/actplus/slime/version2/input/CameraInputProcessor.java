package sk.actplus.slime.version2.input;

import com.badlogic.gdx.InputProcessor;

import sk.actplus.slime.version2.GameScreen;
import sk.actplus.slime.version2.MovableCamera;

import static sk.actplus.slime.version2.GameScreen.PPM;

/**
 * Created by Ja on 6.3.2018.
 */

public class CameraInputProcessor implements InputProcessor {
    private MovableCamera camera;

    public CameraInputProcessor(MovableCamera camera){
        this.camera = camera;

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
        /*System.out.println(PPM);
        PPM+=amount;
        camera.updatePPM();*/
        return false;
    }
}
