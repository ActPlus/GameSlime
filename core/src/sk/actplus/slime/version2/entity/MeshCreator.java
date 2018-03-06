package sk.actplus.slime.version2.entity;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Timotej on 6.3.2018.
 */

public class MeshCreator {
    private float[] verts;
    private Mesh mesh;

    //TODO mesh creator for jelly
    public MeshCreator(Vector2[] vetVector2s) {
        //dont know why this size will find out later
        //probably x,y,z and 2 roattion floats ???
        verts = new float[vetVector2s.length * 2 + 1];


        for (int i = 0; i < vetVector2s.length * 2 + 1;i += 5){
                verts[i] = vetVector2s[i].x;    //xi
                verts[i+1] = vetVector2s[i].y;  //yi
                verts[i+2] = 0f;                //zi
                verts[i+3] = 0f;                //??i
                verts[i+4] = 0f;                //??i

        }

       // mesh = new Mesh(false,(vetVector2s.length * 2 + 1),)
    }
}
