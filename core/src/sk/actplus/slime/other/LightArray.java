package sk.actplus.slime.other;

import com.badlogic.gdx.utils.Array;
import box2dLight.PointLight;

/**
 * Created by Ja on 5.4.2017.
 */

public class LightArray extends Array<PointLight> {
    public PointLight getFirst() {
        return this.first();
    }

    public PointLight getLast() {
        return this.get(this.size-1);
    }

    public PointLight getLight(int index) {
        return this.get(index);
    }
}
