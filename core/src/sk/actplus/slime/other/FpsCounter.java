package sk.actplus.slime.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

import sk.actplus.slime.screens.GUI;

/**
 * Created by Ja on 8.4.2017.
 */

public class FpsCounter {

    GUI gui;
    long startTime;


    public FpsCounter(GUI gui) {
        this.gui = gui;
        startTime = TimeUtils.nanoTime();

    }


    public void count() {
        if (TimeUtils.nanoTime() - startTime > 1000000000) /* 1,000,000,000ns == one second */ {
            gui.updateFps(Gdx.graphics.getFramesPerSecond());
            startTime = TimeUtils.nanoTime();
        }
    }
}
