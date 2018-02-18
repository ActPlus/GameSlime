package sk.actplus.slime.version2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import sk.actplus.slime.screens.PlayScreen;

import static sk.actplus.slime.constants.Values.CAMERA_SPEED;
import static sk.actplus.slime.constants.Values.DEBUG;
import static sk.actplus.slime.constants.Values.PLAYER_SPEED;
import static sk.actplus.slime.constants.Values.WORLD_STEP;
import static sk.actplus.slime.version2.GameScreen.PPM;

/**
 * Created by Ja on 7.4.2017.
 */

    public class MovableCamera extends OrthographicCamera {

        Vector2 transition;
        float width;
        float height;

        public MovableCamera(int x, int y, float width, float height){
            position.set(x,y,0);
            transition = new Vector2(x,y);
            viewportWidth = width/PPM;
            viewportHeight = height/PPM;
            this.width = width;
            this.height = height;
            update();
        }

        public void goTo(int x,int y) {
            transition = new Vector2(x,y);
            position.set(x,y,0);
            update();
        }

        public void move(float dx,float dy) {
            transition = new Vector2(transition.x+dx,transition.y+dy);
            position.set(position.x+(dx)/PPM,position.y+(dy)/PPM,0);
            update();
        }

        public void updatePPM() {
            viewportWidth = width/PPM;
            viewportHeight = height/PPM;
            update();
        }

        public void resize(int width, int height) {
            viewportWidth = width/PPM;
            viewportHeight = height/PPM;
            update();
        }
    }

