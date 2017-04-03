package sk.actplus.slime.constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by timla on 26.1.2017.
 */

public class Values {
    public static boolean DEBUG = true;

    public static float WIDTH_CLIENT = Gdx.graphics.getWidth();
    public static float HEIGHT_CLIENT = Gdx.graphics.getHeight();
    public static float PPM = 32;

    public static BitmapFont GUI_FONT = new BitmapFont();
    public static Color GUI_COLOR = Color.WHITE;

    public static Vector2 GRAVITY = new Vector2(0,-5);
}
