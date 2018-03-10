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
    import com.badlogic.gdx.math.Vector3;
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
    import sk.actplus.slime.version2.entity.PolygonRenderer;

    import static com.badlogic.gdx.graphics.Color.*;
    import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;

/**
 * Created by Ja on 17.2.2018.
 */

public class Triangle2 extends Entity {
    public static final float MAX_RADIUS = 2.5f;

    protected static Vector2[] sharedSide;
    protected static Vector2 C;
    protected Vector2 center;
    private OrthographicCamera camera;


    protected PolygonRenderer polygonRenderer1;
    protected PolygonRenderer polygonRenderer2;
    protected PolygonRenderer polygonRenderer3;
    protected Vector2[] vec;

    protected Color color;


    public Triangle2(GameScreen screen, Vector2 []  vertex, OrthographicCamera camera) {
        super(screen);

        Random rand = new Random();
        color = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat(),1.00f);


        sharedSide = new Vector2[]{vertex[0],vertex[1]};
        C = vertex[2];
        this.camera = camera;

        Vector2[] vec = new Vector2[3];
        center = getCenterPoint();
        generateTriangle(world);
        //polygonRenderer1 = new PolygonRenderer(this.getArrayOfVertices(),3, color);
        vec[0] = this.getArrayOfVertices()[0];
        vec[1] = this.getArrayOfVertices()[1];
        vec[2] = this.getCenterPoint();
        polygonRenderer1 = new PolygonRenderer(vec,3, color);

        vec[0] = this.getArrayOfVertices()[1];
        vec[1] = this.getArrayOfVertices()[2];
        vec[2] = this.getCenterPoint();
        color.set(color.r,color.g,color.b,0.7f);
        polygonRenderer2 = new PolygonRenderer(vec,3, color);

        vec[0] = this.getArrayOfVertices()[2];
        vec[1] = this.getArrayOfVertices()[0];
        vec[2] = this.getCenterPoint();
        color.set(color.r,color.g,color.b,0.5f);
        polygonRenderer3 = new PolygonRenderer(vec,3, color);
    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void render(float delta, PolygonSpriteBatch polyBatch) {


        for (int i = 0; i < 3; i++){
            this.getPolygonRenderer(i).getPolygonSprite().setPosition(- camera.position.x*GameScreen.PPM,  - camera.position.y*GameScreen.PPM);
            this.getPolygonRenderer(i).getPolygonSprite().draw(polyBatch);
        }


        /*this.getPolygonRenderer().getPolygonSprite().setPosition(- camera.position.x*GameScreen.PPM,  - camera.position.y*GameScreen.PPM);
        this.getPolygonRenderer().getPolygonSprite().draw(polyBatch);*/
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

        Body body;

        Vector2[] vertices = new Vector2[3];

        vertices[0] = sharedSide[0].cpy();
        vertices[1] = sharedSide[1].cpy();
        vertices[2] = C.cpy();

        Vector2 center = getCenterPoint();


            for(int i = 0; i < vertices.length; i ++) {
                vertices[i].x -= center.x;
                vertices[i].y -= center.y;
            }



        PolygonShape shape = new PolygonShape();
        shape.set(vertices);


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
        Vector2[] v = getPoints();

        return getCenterPoint(v).cpy();
    }

    public static Vector2 getCenterPoint(Vector2[] vertex) {
        float sumX=0, sumY=0;
        for (int i = 0; i < vertex.length; i++) {
            sumX+=vertex[i].x;
            sumY+=vertex[i].y;
        }

        return new Vector2(sumX/vertex.length,sumY/vertex.length);
    }



    public boolean isTooFar(Vector2 other) {
        float dx = other.x-center.x;
        float dy = other.y-center.y;

        float distance = (float)Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

        if(distance >= MAX_RADIUS*1.5f) {
            return true;
        }


        return false;
    }

    public static boolean isTooFar(Vector2 newTriangle, Vector2 other) {
        float dx = other.x-newTriangle.x;
        float dy = other.y-newTriangle.y;

        float distance = (float)Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));

        if(distance >= MAX_RADIUS*1.5f) {
            return true;
        }
        return false;
    }


    public Side[] getSides() {
        Side[] sides = new Side[3];
        sides[0] = new Side(sharedSide[0].cpy(),sharedSide[1].cpy());
        sides[1] = new Side(sharedSide[0].cpy(),C.cpy());
        sides[2] = new Side(sharedSide[1].cpy(),C.cpy());

        return sides;
    }


    public boolean contains(Vector2 point, boolean openInterval){
        Side[] side = getSides();

        Side normalSide = new Side(point,getCenterPoint());

        for (int i = 0; i < side.length; i++) {
            if(side[i].isIntersecting(normalSide,openInterval)) {
                return true;
            }
        }
        return false;
    }

    private float area(Vector2[] vertices)
    {
        return Math.abs((vertices[0].x*(vertices[1].y-vertices[2].y) + vertices[1].x*(vertices[2].y-vertices[0].y)+ vertices[2].x*(vertices[0].y-vertices[1].y))/2f);
    }

    /* A function to check whether point P(x, y) lies inside the triangle  */
    public boolean contains(Vector2 point)
    {
        Vector2[] vertices = new Vector2[]{sharedSide[0].cpy(),sharedSide[1].cpy(),C.cpy()};


   /* Calculate area of triangle ABC */
        float A = area (vertices);


        Vector2[] pbc = new Vector2[3];
        pbc[0] = point.cpy();
        pbc[1] = sharedSide[1].cpy();
        pbc[2] = C.cpy();

   /* Calculate area of triangle PBC */
        float A1 = area (pbc);

        Vector2[] pac = new Vector2[3];
        pac[0] = point.cpy();
        pac[1] = sharedSide[0].cpy();
        pac[2] = C.cpy();
   /* Calculate area of triangle PAC */
        float A2 = area (pac);


        Vector2[] pab = new Vector2[3];
        pab[0] = point.cpy();
        pab[1] = sharedSide[0].cpy();
        pab[2] = sharedSide[1].cpy();
   /* Calculate area of triangle PAB */
        float A3 = area (pab);


   /* Check if sum of A1, A2 and A3 is same as A */
        return (A == A1 + A2 + A3);
    }

    //todo resize
    private int  width = (int) GameScreen.CLIENT_WIDTH;
    private int height = (int) GameScreen.CLIENT_HEIGHT;
    public void resize(int width, int height){
        this.width += width;
        this.height += height;
        this.getPolygonRenderer().getPolygonSprite().setScale(this.width,this.height);
    }


    public void setC(Vector2 c) {
        C = c;
    }
    public void setSharedSide(Vector2[] sharedSide) {
        this.sharedSide = sharedSide;
    }
    public static Vector2 getC() {
        return C.cpy();
    }
    public static Vector2[] getSharedSide() {
        return sharedSide;
    }

    public Vector2[] getPoints() {
        return new Vector2[]{sharedSide[0].cpy(),sharedSide[1].cpy(),C.cpy()};
    }

    public Vector2[] getArrayOfVertices(){
        Vector2[] vecArray = new Vector2[3];
        int index = 0;

        //all
        vecArray[index++] = getSharedSide()[0];
        vecArray[index++] = getSharedSide()[1];
        vecArray[index] = getC();

        return vecArray;
    }

    public PolygonRenderer getPolygonRenderer(){
        return polygonRenderer1;
    }
    public PolygonRenderer getPolygonRenderer(int index){
       switch (index){
           case 0: return polygonRenderer1;

           case 1: return polygonRenderer2;

           case 2: return polygonRenderer3;

       }

        return polygonRenderer1;
    }
    public Vector3 getCameraPosition(){return camera.position;}
    public Color getColor(){return color;}
}
