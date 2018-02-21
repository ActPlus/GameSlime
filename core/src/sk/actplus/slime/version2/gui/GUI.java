package sk.actplus.slime.version2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Ja on 18.2.2018.
 */

public abstract class GUI {
    //TODO: dynamic size
    protected static final int GUI_DEFAULT_FONT_SIZE = 16;
    protected static final Color GUI_DEFAULT_COLOR = Color.WHITE;

    protected Stage stage;
    //TODO: Constructor


    public abstract void create ();
    public abstract void validate ();


    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render (float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
