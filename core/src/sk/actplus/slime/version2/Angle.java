package sk.actplus.slime.version2;

/**
 * Created by Ja on 17.2.2018.
 */

public class Angle {


    private float deg;

    public Angle(float deg) {
        this.deg = deg;
    }

    public float getDeg(){
        return deg;
    }

    public float getRad(){
        //TODO
        return 0;
    }
    public void setDeg(float deg) {
        this.deg = deg;
    }

}
