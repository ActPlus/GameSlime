package sk.actplus.slime.version2.entity.accessory;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import sk.actplus.slime.version2.Angle;
import sk.actplus.slime.version2.GameScreen;
import sk.actplus.slime.version2.entity.ExpectsInput;
import sk.actplus.slime.version2.entity.PolygonGenerator;
import sk.actplus.slime.version2.enums.Types;
import sk.actplus.slime.version2.enums.Types.TimePeriod;
import sk.actplus.slime.version2.input.ShieldInputProcessor;

/**
 * Created by Ja on 17.2.2018.
 */

public class Shield extends sk.actplus.slime.version2.entity.Entity  {
    private Angle position;
    private Angle width;
    private boolean active;
    private boolean infinite;
    //duration is in seconds
    private float duration;

    private ShieldInputProcessor inputProcessor;

    public Shield(GameScreen screen, Body ownerBody) {
        super(screen);
        inputProcessor = new ShieldInputProcessor(this);
        active = false;

        //TODO remove comment after box2d body is implemented
        //body = createShieldBody(ownerBody);

    }
    public void deactivate() {
        active = false;
        hide();
    }

    public void activate(){
        infinite = true;
        active = true;
        show();
    }

    public void activate(int duration, TimePeriod unit){
        int multiply = 0;
        switch (unit){
            case sec:
                multiply = 1;
                break;
            case min:
                multiply = 60;
                break;
            case hour:
                multiply = 3600;
                break;
            case day:
                multiply = 3600*24;
                break;
            case week:
                multiply = 3600*24*7;
                break;
            case month:
                multiply = 3600*24*31;
                break;
            case year:
                multiply = 3600*24*365;
                break;
            case decade:
                multiply = 3600*24*365*10;
                break;
        }

        this.duration = handleValueOverFlow(duration*multiply);
        active = true;
        show();

    }

    private float handleValueOverFlow(long value) {
        if (value >= Float.MAX_VALUE){
            return Float.MAX_VALUE;
        }
        return value;

    }


    @Override
    public void render(float delta, PolygonSpriteBatch polyBatch) {

    }

    @Override
    public void handleCollision(short collisionBIT) {
        //TODO deflects bullets, kamiqazees
    }

    @Override
    public void destroy() {
        screen.removeInputProcessor(inputProcessor);
    }


    @Override
    public void update(float delta) {
        super.update(delta);
        if(active) {
            //TODO optimize conditions
            if (((!infinite) && duration > 0)||infinite) {
                duration -= delta;

                //duration ellapsed - > deactivate
                if((!infinite) && duration <= 0) {
                    deactivate();
                }

                //TODO do something faboulous
            }
        }
    }

    private void hide() {
        //TODO hide shield
    }

    private void show() {
        //TODO show shield
    }

    private Body createShieldBody(Body owner) {
        //TODO create body
        return null;
    }

}
