package sk.actplus.slime.other;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import sk.actplus.slime.constants.Category;

import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;

/**
 * Created by Ja on 7.11.2017.
 */

public class TriGen {
    public static void generateMore(World world, int howMuch){
        BodyArray triangles = new BodyArray();
        for (int i = 0; i < howMuch; i++) {
            triangles.add(generateTriangle(world));
        }
    }

    static Random rand = new Random();

    public static BodyArray triangles= new BodyArray();
    private static Vector2[] lastVertexes = new Vector2[2];

    public static void setStartingPoint(Vector2 startingPos){
        lastVertexes[0] = new Vector2(startingPos.x-4*rand.nextFloat(),startingPos.y-4*rand.nextFloat());
        lastVertexes[1] = new Vector2(startingPos.x-4*rand.nextFloat(),startingPos.y+4*rand.nextFloat());
    }

    public static Body generateTriangle(World world){

        Body body;
        Vector2[] vertices = new Vector2[3];

        vertices[0] = lastVertexes[0];
        vertices[1] = lastVertexes[1];
        vertices[2] = new Vector2(getCenterPoint(lastVertexes).x+rand.nextFloat()*4f,getCenterPoint(lastVertexes).y+2f-rand.nextFloat()*4f);




        lastVertexes =  getOkVertexes(vertices);
        Vector2 center = getCenterPoint(vertices);
        /*for (int i = 0; i < 3; i++) {
            vertices[i].x-=center.x;
            vertices[i].y-=center.y;
        }*/
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);


        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(0,0);
        body = world.createBody(def);
        triangles.add(body);


        FixtureDef fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.restitution = 0.4f;
        fixDef.friction = 0f;
        fixDef.filter.categoryBits = Category.MAPOBJECT_TRIANGLE;
        fixDef.filter.maskBits = Category.JELLY;


        body.createFixture(fixDef);
        shape.dispose();

        for (Fixture fixture : body.getFixtureList()) {
            fixture.setUserData(BLOCK_USER_DATA);
        }


        return body;
    }

    public static Vector2 getCenterPoint(Vector2[] v) {
        float sumX=0, sumY=0;
        for (int i = 0; i < v.length; i++) {
            sumX+=v[i].x;
            sumY+=v[i].y;
        }


        return new Vector2(sumX/v.length,sumY/v.length);
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
