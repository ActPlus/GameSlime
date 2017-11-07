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


    public static Vector2 GRAVITY = new Vector2(0,-40);


    public static float PPM = 64;
    public static float WORLD_STEP = 1 / 60f;

    //SPEED IS IN m/s in m/update it is -20*WORLD_STEP=0.333
    public static float PLAYER_SPEED = 10f;
    public static float PLAYER_JUMP = -7 * GRAVITY.y;
    public static float SHOOT_SPEED = PLAYER_SPEED*2.2f;
    public static float FOLLOWER_SPEED = PLAYER_SPEED*0.5f;
    public static float MOVING_BLOCK_SPEED = PLAYER_SPEED * 0.15f;
    public static float CAMERA_SPEED = PLAYER_SPEED*0.95f;

    public static Color GUI_COLOR = Color.WHITE;
    public static int GUI_FONT_SIZE = (int)(150*HEIGHT_CLIENT/WIDTH_CLIENT);

    public static int HEIGHTGEN_HIGH = 6;
    public static int HEIGHTGEN_MED = HEIGHTGEN_HIGH/3*2;
    public static int HEIGHTGEN_LOW = HEIGHTGEN_HIGH/2;

    /**
     * USER DATA
     */

    public static String BLOCK_USER_DATA = "block";
    public static String BLOCK_TRAP_USER_DATA = "trap";
    public static String BLOCK_COOKIE_USER_DATA = "cookie";

}
