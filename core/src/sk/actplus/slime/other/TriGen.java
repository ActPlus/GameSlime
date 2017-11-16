package sk.actplus.slime.other;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;
import static sk.actplus.slime.constants.Values.HEIGHT_CLIENT;
import static sk.actplus.slime.constants.Values.PPM;
import static sk.actplus.slime.constants.Values.WIDTH_CLIENT;

/**
 * Created by Ja on 7.11.2017.
 */

public class TriGen {

    public static BodyArray generateMore(World world, int howMuch){
        BodyArray triangles = new BodyArray();
        for (int i = 0; i < howMuch; i++) {
            triangles.add(generateTriangle(world));
        }


        return null;
    }

    public static Vector2[] lastVertexes = {new Vector2(0,3f),new Vector2(0.8f,4.2f)};
    static Random rand = new Random();
    public static Body generateTriangle(World world){




        Body body;
        Vector2[] vertices = new Vector2[3];
        vertices[0] = lastVertexes[0];
        vertices[1] = lastVertexes[1];
        //vertices[2] = new Vector2(getCenterPoint(lastVertexes).x+rand.nextFloat()*7,getCenterPoint(lastVertexes).y+rand.nextFloat()*50-25f);

        for (int i = 2; i < 3; i++) {
            vertices[i] = new Vector2(getCenterPoint(lastVertexes).x+rand.nextFloat()*7,getCenterPoint(lastVertexes).y+rand.nextFloat()*50-25f);
        }
        lastVertexes =  getOkVertexes(vertices);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);


        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(new Vector2(0,0));
        body = world.createBody(def);

        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 0.4f;
        fixDef.friction = 0f;


        body.createFixture(fixDef);
        shape.dispose();

        for (Fixture fixture : body.getFixtureList()) {
            fixture.setUserData(BLOCK_USER_DATA);
        }


        return null;
    }

    public static Vector2 getCenterPoint(Vector2[] v) {
        float n = 0;
        float sumX=0, sumY=0;
        for (int i = 0; i < v.length; i++) {
            n++;
            sumX+=v[i].x;
            sumY+=v[i].y;
        }


        return new Vector2(sumX/n,sumY/n);
    }

    public static Vector2[] getOkVertexes(Vector2[] v) {
        Vector2[] vert = new Vector2[2];

        for (Vector2 ver: v) {
            if (ver != mostLeft(v)){
                vert[0]=ver;
            }
        }

        for (Vector2 ver: v) {
            if ((ver != mostLeft(v))&&(ver != vert[0])){
                vert[1]=ver;
            }
        }
        return vert;
    }


    public static Vector2 moreLeft(Vector2 a, Vector2 b) {
        return (a.x>b.x)?b:a;
    }
    public static Vector2 mostLeft(Vector2[] v) {
        return moreLeft(moreLeft(v[0],v[1]),v[2]);
    }

}
