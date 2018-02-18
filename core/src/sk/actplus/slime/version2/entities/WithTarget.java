package sk.actplus.slime.version2.entities;

/**
 * Created by Ja on 17.2.2018.
 */

public abstract class WithTarget extends Entity {

    private Entity target;

    public abstract void beforeAction();

    public abstract void action();

    public abstract void afterAction();
}
