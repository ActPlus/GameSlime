package sk.actplus.slime.version2;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class InputHandler implements InputProcessor {

    public InputHandler(InputMultiplexer multiplexor) {
        multiplexor.addProcessor(this);
    }

}
