package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Ja on 17.2.2018.
 */

public interface ExpectsInput {

    boolean touchDown(InputEvent event, float x, float y, int pointer, int button);
    void touchUp(InputEvent event, float x, float y, int pointer, int button);
    void touchDragged(InputEvent event, float x, float y, int pointer);
    boolean mouseMoved(InputEvent event, float x, float y);
    boolean scrolled(InputEvent event, float x, float y, int amount);
    boolean keyDown(InputEvent event, int keycode);
    boolean keyUp(InputEvent event, int keycode);
    boolean keyTyped(InputEvent event, char character);


}
