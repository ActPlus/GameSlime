package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Mesh;



/**
 * Created by Timotej on 20.2.2018.
 */

public class MeshGenerator {
    Mesh meshGenerated;

    public MeshGenerator(float [] floats, int maxVertices){
        meshGenerated = new Mesh( true, maxVertices, 0,  // static mesh with 4 vertices and no indices
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );
        meshGenerated.setVertices(floats);
    }

}
