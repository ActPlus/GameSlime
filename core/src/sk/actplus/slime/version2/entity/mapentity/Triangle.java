package sk.actplus.slime.version2.entity.mapentity;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Mesh;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.VertexAttributes.Usage;
    import com.badlogic.gdx.graphics.VertexAttribute;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.Body;
    import com.badlogic.gdx.physics.box2d.BodyDef;
    import com.badlogic.gdx.physics.box2d.Fixture;
    import com.badlogic.gdx.physics.box2d.FixtureDef;
    import com.badlogic.gdx.physics.box2d.PolygonShape;
    import com.badlogic.gdx.physics.box2d.World;

    import sk.actplus.slime.constants.Category;
    import sk.actplus.slime.version2.GameScreen;
    import sk.actplus.slime.version2.VertexShader;
    import sk.actplus.slime.version2.entity.Entity;
    import sk.actplus.slime.version2.input.Side;

    import static sk.actplus.slime.constants.Values.BLOCK_USER_DATA;

/**
 * Created by Ja on 17.2.2018.
 */

public class Triangle extends Entity {
    public static final float MAX_RADIUS = 2.5f;

    protected Vector2[] sharedSide;
    protected Vector2 C;
    protected Vector2 center;
    protected Graphics graphics;

    public Vector2[] getSharedSide() {
        return sharedSide;
    }

    public void setSharedSide(Vector2[] sharedSide) {
        this.sharedSide = sharedSide;
    }

    public Vector2 getC() {
        return C;
    }

    public void setC(Vector2 c) {
        C = c;
    }

    private OrthographicCamera camera;

    public Triangle(GameScreen screen, Vector2 []  vertex, OrthographicCamera camera) {
        super(screen);
        sharedSide = new Vector2[]{vertex[0],vertex[1]};
        C = vertex[2];
        this.camera = camera;
        graphics = new Graphics(new Vector2[]{sharedSide[0],sharedSide[1],C});
        center = getCenterPoint();
    }

    @Override
    public void render(float delta) {
        //TODO OpenGL Triangle graphics radial gradients in vertex, depending on seed color:-> darker, normal, light
        //graphics.flush(camera);
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
        vertices[0] = sharedSide[0];
        vertices[1] = sharedSide[1];
        vertices[2] = C;

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);


        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(0,0);
        body = world.createBody(def);
        //triangles.add(body);


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
        Vector2[] v = new Vector2[]{sharedSide[0],sharedSide[1],C};

        float sumX=0, sumY=0;
        for (int i = 0; i < v.length; i++) {
            sumX+=v[i].x;
            sumY+=v[i].y;
        }


        return new Vector2(sumX/v.length,sumY/v.length);
    }

    public static Vector2 getCenterPoint(Vector2[] vertex) {
        Vector2[] v = new Vector2[]{vertex[0],vertex[1],vertex[2]};

        float sumX=0, sumY=0;
        for (int i = 0; i < v.length; i++) {
            sumX+=v[i].x;
            sumY+=v[i].y;
        }


        return new Vector2(sumX/v.length,sumY/v.length);
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

    public class Graphics{
        private Mesh mesh;
        private VertexShader shader;
        //Position attribute - (x, y)
        public static final int POSITION_COMPONENTS = 2;

        //Color attribute - (r, g, b, a)
        public static final int COLOR_COMPONENTS = 4;

        //Total number of components for all attributes
        public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;

        //The "size" (total number of floats) for a single triangle
        public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;

        //The maximum number of triangles our mesh will hold
        public static final int MAX_TRIS = 1;

        //The maximum number of vertices our mesh will hold
        public static final int MAX_VERTS = MAX_TRIS * 3;

        //The array which holds all the data, interleaved like so:
//    x, y, r, g, b, a
//    x, y, r, g, b, a,
//    x, y, r, g, b, a,
//    ... etc ...

        protected float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];

        //The current index that we are pushing triangles into the array
        protected int idx = 0;

        public Graphics(Vector2 [] vertex) {
            //shader = new VertexShader();
            //TODO

            mesh = new Mesh(true, MAX_VERTS, 0,
                    new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
                    new VertexAttribute(Usage.ColorUnpacked, COLOR_COMPONENTS, "a_color"));
        }

        public int createVertex(int idx,Vector2 point, Color seed) {

            //bottom right vertex
            verts[idx++] = point.x;	 //Position(x, y)
            verts[idx++] = point.y;
            verts[idx++] = seed.r;		 //Color(r, g, b, a)
            verts[idx++] = seed.g;
            verts[idx++] = seed.b;
            verts[idx++] = seed.a;

            return idx;

        }

        void flush(OrthographicCamera camera) {
            //if we've already flushed
            if (idx==0)
                return;

            //sends our vertex data to the mesh
            mesh.setVertices(verts);

            //no need for depth...
            Gdx.gl.glDepthMask(false);

            //enable blending, for alpha
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            //number of vertices we need to render
            int vertexCount = (idx/NUM_COMPONENTS);

            //start the shader before setting any uniforms
            shader.begin();

            //update the projection matrix so our triangles are rendered in 2D
            shader.setUniformMatrix("u_projTrans", camera.combined);

            //render the mesh
            mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);

            shader.end();

            //re-enable depth to reset states to their default
            Gdx.gl.glDepthMask(true);

            //reset index to zero
            idx = 0;
        }


    }
}
