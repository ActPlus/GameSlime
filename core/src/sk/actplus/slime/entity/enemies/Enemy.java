package sk.actplus.slime.entity.enemies;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import sk.actplus.slime.entity.interfaces.Updateable;
import sk.actplus.slime.entity.mapobject.MovingBlock;
import sk.actplus.slime.entity.player.Jelly;
import sk.actplus.slime.entity.player.Player;
import sk.actplus.slime.other.BodyArray;
import sk.actplus.slime.other.LogicalOperations;

import static sk.actplus.slime.constants.Values.FOLLOWER_SPEED;
import static sk.actplus.slime.constants.Values.SHOOT_SPEED;

/**
 * Created by Ja on 8.4.2017.
 */



public class Enemy extends Player implements Updateable {
    Random rand = new Random();
    Jelly player;

    public static final BodyDef.BodyType BODY_TYPE = BodyDef.BodyType.KinematicBody;
    public static final boolean FIXED_ROTATION = true;
    public static final float SIDE_WIDTH = 1f;
    public static final float SIDE_HEIGHT = 1f;
    public static final float DENSITY = 0.35f;
    public static final float RESTITUTION = 0.1f;
    public static final float FRICTION = 0f;

    public static final float SHOOT_WIDTH = 0.1f;
    public static final float SHOOT_HEIGHT = 0.5f;



    public Enemy(World world, int x, int y, Jelly player) {
        super(world);

        /**
         * Creates Main Player Body, controlled by Input
         */
        BodyDef bodyDefMain = defineBody(BODY_TYPE, x,y,FIXED_ROTATION);
        PolygonShape shapeMain = new PolygonShape();
        shapeMain.setAsBox(SIDE_WIDTH/2f,SIDE_HEIGHT/2f);
        FixtureDef fixtureDefMain = defineFixture(shapeMain,DENSITY,RESTITUTION,FRICTION);
        body = createBody(bodyDefMain,fixtureDefMain);

        this.player = player;
}

    @Override
    public void update() {
        float angle;

        if (rand.nextInt(3)==0) {
            angle = (float) Math.toRadians(LogicalOperations.getAngle(player.body.getPosition(), body.getPosition()));

            body.setLinearVelocity(FOLLOWER_SPEED * (float) Math.cos(angle), FOLLOWER_SPEED * (float) Math.sin(angle));
        }

        //1/20Chance to shoot
        if (rand.nextInt(25)==0) {

            angle = (float)Math.toRadians(LogicalOperations.getAngle(player.body.getPosition(),body.getPosition()));

            Vector2 direction = new Vector2(SHOOT_SPEED*(float)Math.cos(angle), SHOOT_SPEED*(float)Math.sin(angle));

            MovingBlock shoot = new MovingBlock(world,body.getPosition().x, body.getPosition().y, SHOOT_WIDTH, SHOOT_HEIGHT,  direction,"shoot");
            shoot.body.setTransform(shoot.body.getWorldCenter(),(float)Math.toRadians(Math.toDegrees(angle)+90));
            body.setTransform(shoot.body.getWorldCenter(),(float)Math.toRadians(Math.toDegrees(angle)+90));
        }

    }
}
