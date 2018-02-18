package sk.actplus.slime.version2.entities;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.graphics.Color;
    import com.badlogic.gdx.graphics.GL20;
    import com.badlogic.gdx.graphics.Mesh;
    import com.badlogic.gdx.graphics.OrthographicCamera;
    import com.badlogic.gdx.graphics.VertexAttributes.Usage;
    import com.badlogic.gdx.graphics.VertexAttribute;
    import com.badlogic.gdx.math.Vector2;
    import com.badlogic.gdx.physics.box2d.World;
    import sk.actplus.slime.version2.VertexShader;
    import sun.security.provider.certpath.Vertex;

/**
 * Created by Ja on 17.2.2018.
 */

public class Triangle extends Entity{
    private Vector2[] sharedSide;
    private Vector2 C;
    private Graphics graphics;

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

    public Triangle(World world,Vector2 []  vertex, OrthographicCamera camera) {
        super(world);
        sharedSide = new Vector2[]{vertex[0],vertex[1]};
        C = vertex[2];
        this.camera = camera;
        graphics = new Graphics(new Vector2[]{sharedSide[0],sharedSide[1],C});
    }

    @Override
    public void handleCollision() {
        super.handleCollision();
    }

    @Override
    public void render() {
        super.render();
        graphics.flush(camera);

    }

    @Override
    public void update() {
        super.update();
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
