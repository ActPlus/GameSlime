package sk.actplus.slime.version2.entity.mapentity;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Mesh;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.VertexAttributes.Usage;
    import com.badlogic.gdx.graphics.VertexAttribute;
    import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.Body;
    import com.badlogic.gdx.physics.box2d.BodyDef;
    import com.badlogic.gdx.physics.box2d.Fixture;
    import com.badlogic.gdx.physics.box2d.FixtureDef;
    import com.badlogic.gdx.physics.box2d.PolygonShape;
    import com.badlogic.gdx.physics.box2d.World;

    import java.util.Random;

    import sk.actplus.slime.constants.Category;
    import sk.actplus.slime.version2.GameScreen;
    import sk.actplus.slime.version2.VertexShader;
    import sk.actplus.slime.version2.entity.Entity;
    import sk.actplus.slime.version2.Side;
    import sk.actplus.slime.version2.entity.PolygonGenerator;
    import sun.rmi.runtime.Log;

    import static com.badlogic.gdx.graphics.Color.*;
    import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;

/**
 * Created by Ja on 17.2.2018.
 */

public class Triangle extends Entity {
    public static final float MAX_RADIUS = 2.5f;

    // shared side has index 0,1 C Side has index 2
    private Vector2[] vertices;
    private Vector2 center;
    private OrthographicCamera camera;
    private Random rand;
    private PolygonGenerator graphics;

    public Triangle(GameScreen screen, Vector2 []  vertices, OrthographicCamera camera) {
        super(screen);
        this.camera = camera;
        this.vertices = vertices;
        this.center = getCenterPoint();
        this.rand = new Random();
        this.graphics = new PolygonGenerator(vertices,3,new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1f));

        this.generateTriangle(world);
    }

    @Override
    public void render(float delta, PolygonSpriteBatch polyBatch) {
        //graphics.getPolygonSprite().draw(polyBatch);
    }

    @Override
    public void handleCollision(short collisionBIT) {
        switch (collisionBIT){

            //TODO collision switch
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }


    public Body generateTriangle(World world){

        Vector2 center = getCenterPoint();

        Vector2[]  relativeToBodyVertices = new Vector2[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            relativeToBodyVertices[i] = vertices[i].cpy().sub(center);
        }


        //////////////////////////////////////////
        PolygonShape shape = new PolygonShape();
        shape.set(relativeToBodyVertices);

        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(center);
        body = world.createBody(def);

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

    public Vector2 getCenterPoint() {
        return getCenterPoint(vertices);
    }

    public static Vector2 getCenterPoint(Vector2[] vertices) {
        Vector2 center = new Vector2(0,0);
        float x=0,y=0;

        for (int i = 0; i < vertices.length; i++) {
            x+=vertices[i].x;
            y+=vertices[i].y;
        }
        //System.out.println(vertices.length);

        x= x/vertices.length;
        y= x/vertices.length;


       // for (Vector2 aVertex : vertices) {
        //    center.add(aVertex.cpy());
        //}
        //return center.scl(1/vertices.length);
        return new Vector2(x,y);
    }

    public boolean isTooFar(Vector2 otherPoint) {
        Vector2 distanceVector = otherPoint.cpy().sub(center);

        //TODO is too far radius to constant
        if(distanceVector.len() >= MAX_RADIUS*1.5f) {
            return true;
        }
        return false;
    }


    public Side[] getSides() {
        Side[] sides = new Side[3];
        sides[0] = new Side(vertices[0].cpy(),vertices[1].cpy());
        sides[1] = new Side(vertices[0].cpy(),vertices[2].cpy());
        sides[2] = new Side(vertices[1].cpy(),vertices[2].cpy());
        return sides;
    }

    private float getArea()
    {
        return Triangle.getArea(vertices);
    }

    private static float getArea(Vector2[] vertices)
    {
        if(vertices.length!=3){
            System.err.println("Triangle get Area only works on triangles!!! ");
        }

        return Math.abs((vertices[0].x*(vertices[1].y-vertices[2].y) + vertices[1].x*(vertices[2].y-vertices[0].y)+ vertices[2].x*(vertices[0].y-vertices[1].y))/2f);
    }

    /* A function to check whether point P(x, y) lies inside the triangle  */
    public boolean pointInArea(Vector2 point)
    {

   /* Calculate area of triangle ABC */
        float A = getArea();


        Vector2[] pbc = new Vector2[3];
        pbc[0] = point;
        pbc[1] = vertices[1];
        pbc[2] = vertices[2];

   /* Calculate area of triangle PBC */
        float A1 = getArea(pbc);

        Vector2[] pac = new Vector2[3];
        pac[0] = point;
        pac[1] = vertices[0];
        pac[2] = vertices[2];
   /* Calculate area of triangle PAC */
        float A2 = getArea(pac);


        Vector2[] pab = new Vector2[3];
        pab[0] = point;
        pab[1] = vertices[0];
        pab[2] = vertices[1];
   /* Calculate area of triangle PAB */
        float A3 = getArea(pab);


   /* Check if sum of A1, A2 and A3 is same as A */
        return (Math.floor(A) == Math.floor((A1 + A2 + A3)));
    }


    public Vector2[] getVertices() {
        return vertices;
    }
}
