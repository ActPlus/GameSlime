package sk.actplus.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Ja on 5.4.2017.
 */

public class GUI {
    private Stage stage;
    private Table table;
    public Label fpsLoggerLabel;

    public void create () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.setDebug(true); // This is optional, but enables debug lines for tables.



//On Init
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("heav.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font12;
        style.fontColor = Color.WHITE;
        fpsLoggerLabel = new Label("FPS",style);


        table.align(Align.top);
        table.add(fpsLoggerLabel).center();

        // Add widgets to the table here.
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }
}
