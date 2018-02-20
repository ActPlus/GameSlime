package sk.actplus.slime.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import static sk.actplus.slime.constants.Files.FONT;
import static sk.actplus.slime.constants.Values.GUI_COLOR;
import static sk.actplus.slime.constants.Values.GUI_FONT_SIZE;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Ja on 5.4.2017.
 */

public class GUI {
    private Stage stage;
    private Table tableLeftTop;
    private Table tableCenterBot;
    public Label fpsLoggerLabel;
    public Label gameOverLabel;
    public Label touchToStartLabel;
    public Label scoreLabel;

    public BitmapFont font;

    public Label.LabelStyle style;

    public void create () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        tableLeftTop = new Table();
        tableLeftTop.setFillParent(true);

        tableCenterBot = new Table();
        tableCenterBot.setFillParent(true);

        stage.addActor(tableLeftTop);
        stage.addActor(tableCenterBot);

        //tableLeftTop.setDebug(true); // This is optional, but enables debug lines for tables.
        //tableCenterBot.setDebug(true);


        //Creates new Font, from FONT_PATH.tff
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FONT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = GUI_FONT_SIZE;
        font = generator.generateFont(parameter);
        generator.dispose();

        //new style
        style = new Label.LabelStyle();
        style.fontColor = GUI_COLOR;
        style.font = font;

        //Initializations of buttons
        fpsLoggerLabel = new Label("FPS: ",style);
        gameOverLabel = new Label("GAME OVER",style);
        touchToStartLabel = new Label("Touch to Start..",style);
        scoreLabel = new Label("Score: ",style);

        //font size
        fpsLoggerLabel.setFontScale(0.33f);
        scoreLabel.setFontScale(0.33f);
        gameOverLabel.setFontScale(1f);
        touchToStartLabel.setFontScale(0.5f);

        gameOverLabel.setVisible(false);
        touchToStartLabel.setVisible(true);
        tableLeftTop.left().top();
        tableLeftTop.add(fpsLoggerLabel).left();
        tableLeftTop.row();
        tableLeftTop.add(scoreLabel).left();

        tableCenterBot.center().bottom();
        tableCenterBot.add(gameOverLabel).bottom();
        tableCenterBot.row();
        tableCenterBot.add(touchToStartLabel).bottom();

        validate();

        // Add widgets to the table here.
    }

    /**
     * Show GameOverGUI message
     */
    public void gameOver() {
        gameOverLabel.setVisible(true);
        touchToStartLabel.setText("Touch to RESTART..");
        touchToStartLabel.setVisible(true);
        validate();
    }
    public void updateScore(int score) {
        scoreLabel.setText("Score: " + score);
        validate();
    }

    public void updateFps(int fps) {
        fpsLoggerLabel.setText("FPS: " + fps);
        validate();
    }


    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render (float delta) {
        stage.act(delta);
        stage.draw();
    }

    public void validate () {
        /**
         * update labels after property is changed
         * */
        gameOverLabel.validate();
        touchToStartLabel.validate();
        scoreLabel.validate();
        fpsLoggerLabel.validate();
    }

    public void dispose() {
        stage.dispose();
    }

    public void pause() {
        touchToStartLabel.setText("Touch to RESUME..");
        touchToStartLabel.setVisible(true);
        validate();
    }
}
