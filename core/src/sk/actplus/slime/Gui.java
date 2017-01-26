package sk.actplus.slime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

import static sk.actplus.slime.constants.Values.GUI_COLOR;
import static sk.actplus.slime.constants.Values.GUI_FONT;

/**
 * Created by timla on 26.1.2017.
 */

public class Gui extends Stage {
    private Table table;
    private Viewport viewport;
    Label fpsLabel;
    Label scoreLabel;

    public Gui (Viewport viewport, Batch batch){
        super(viewport, batch);

        Gdx.input.setInputProcessor(this);

        table = new Table();
        table.setFillParent(true);
        addActor(table);

        table.top();
        fpsLabel = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), new Label.LabelStyle(GUI_FONT,GUI_COLOR));
    }
}
