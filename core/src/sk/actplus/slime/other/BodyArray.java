package sk.actplus.slime.other;


/**
 * Created by Admin on 20.12.2016.
 */

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;



    public class BodyArray extends Array<Body>{

        public Body getBody(int index) {
            return this.get(index);
        }

        public void applyForceToCenter (float vx, float vy, boolean wake) {
            for (Object obj:this) {
                Body body = (Body) obj;
                body.applyForceToCenter(vx, vy,wake);
            }
        }

        public void setLinearVelocity(float vx, float vy) {
            for (Object obj:this) {
                Body body = (Body) obj;
                body.setLinearVelocity(vx,vy);
            }
        }
    }
