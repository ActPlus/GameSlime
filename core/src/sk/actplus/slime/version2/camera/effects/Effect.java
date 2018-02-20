package sk.actplus.slime.version2.camera.effects;

/**
 * Created by Ja on 18.2.2018.
 */

public abstract class Effect {

    public boolean infinite;
    public float duration;
    public boolean active;

    public void activate(float duration) {
        this.duration = duration;
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void clear() {

        //TODO Set to default
    }
    public void update(float delta){
        if(duration<=0) {
            clear();
        } else {
            if (active) {
                if(!infinite)
                    duration -= delta;
                //call update
                action();
            }
        }
    }

    public abstract void action();


}
