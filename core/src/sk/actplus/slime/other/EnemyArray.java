package sk.actplus.slime.other;

import com.badlogic.gdx.utils.Array;

import sk.actplus.slime.entity.enemies.Enemy;

/**
 * Created by Ja on 8.4.2017.
 */

public class EnemyArray extends Array<Enemy> {
    public void update() {
        for (Enemy enemy:this) {
            enemy.update();
        }
    }

}
