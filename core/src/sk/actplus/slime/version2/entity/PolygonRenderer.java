package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

import sk.actplus.slime.version2.GameScreen;

/**
 * Created by Timotej on 21.2.2018.
 */

public class PolygonRenderer {
    private PolygonSprite polygonSprite;
   // private static PolygonSpriteBatch polyBatch;
    ShapeRenderer shapeRenderer;
    private FloatArray vertices;
    private int numberOfVertices;
    private EarClippingTriangulator triangulator;
    Vector2 center;
    Texture texture;
    private TextureRegion textureRegion;
    private PolygonRegion polygonRegion;


    /**
     *
     * @param vector2s  array of vertices of polygon
     * @param numberOfVertices number of vertices of polygon
     * @param color Color of the polygon
     */
    public PolygonRenderer(Vector2[] vector2s, int numberOfVertices, Color color){
        this.numberOfVertices = numberOfVertices;
        shapeRenderer = new ShapeRenderer();

        center = new Vector2(GameScreen.CLIENT_WIDTH/2,GameScreen.CLIENT_HEIGHT/2);


        Pixmap pix = new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        texture = new Texture(pix);
        textureRegion = new TextureRegion(texture);


        // ordered array of x,y coordinates of all vertices
        vertices = new FloatArray(numberOfVertices *2);
        for (int i = 0; i < numberOfVertices; i++){
            vertices.add(vector2s[i].x * GameScreen.PPM + center.x);
            vertices.add(vector2s[i].y * GameScreen.PPM + center.y);
        }

        triangulator = new EarClippingTriangulator();
        ShortArray triangleINdeces = triangulator.computeTriangles(vertices);
        polygonRegion = new PolygonRegion(textureRegion,vertices.toArray(),triangleINdeces.toArray());
        polygonSprite = new PolygonSprite(polygonRegion);


    }

    //todo add update to update vertices

    public void update(Vector2[] vector2s){
       /* // ordered array of x,y coordinates of all vertices
       for (int i = 0; i < numberOfVertices; i++){
            vertices.add(vector2s[i].x * GameScreen.PPM + center.x);
            vertices.add(vector2s[i].y * GameScreen.PPM + center.y);
        }

        ShortArray triangleINdeces = triangulator.computeTriangles(vertices);
        polygonRegion = new PolygonRegion(this.textureRegion,vertices.toArray(),triangleINdeces.toArray());
        polygonSprite.setRegion(polygonRegion);*/

    }

    public void render(){
    }

    public PolygonSprite getPolygonSprite(){
        return polygonSprite;
    }



}
