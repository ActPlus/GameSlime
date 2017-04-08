package sk.actplus.slime.other;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import sk.actplus.slime.screens.PlayScreen;

import static sk.actplus.slime.constants.Values.CAMERA_SPEED;
import static sk.actplus.slime.constants.Values.DEBUG;
import static sk.actplus.slime.constants.Values.PLAYER_SPEED;
import static sk.actplus.slime.constants.Values.WORLD_STEP;

/**
 * Created by Ja on 7.4.2017.
 */

public class MovableCamera extends OrthographicCamera {
    Body focusBody;
    private Vector2 lastPlayerPos;


    /**
     * Its OrthographicCamera with special method cameraUpdate() which differs, mode its updated in.
     * @param focusBody   - Center Body, camera should be focused on, during DEBUG, and if player is fast in NORMAL MODE
     * @param startingPos - Starting position of camera, after initialized
     */
    public MovableCamera(Body focusBody, Vector2 startingPos) {
        this.focusBody = focusBody;
        lastPlayerPos = focusBody.getPosition();
        position.set(startingPos.x, startingPos.y, 0);
    }



    public void cameraUpdate() {
        if (DEBUG) {
            /**
             * In DEBUG mode, camera is focused on focusBody, therefor it is not faster, nor slower
             */
            position.set(focusBody.getPosition().x, focusBody.getPosition().y, 0);
        } else {
            /**
             * Normal Play Mode, Camera don't wait player/focusBody
             */
            float speed = focusBody.getPosition().x-position.x;

            if ((focusBody.getPosition().x >= position.x)) {
                translate(PLAYER_SPEED*WORLD_STEP*((PlayScreen.paused) ? 0 : 1),0);
            } else {
                translate(CAMERA_SPEED*WORLD_STEP*((PlayScreen.paused) ? 0 : 1),0);
            }
            position.set(position.x,focusBody.getPosition().y,0);
        }

        update();
    }
}
