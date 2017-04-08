package sk.actplus.slime.desktop;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Created by Ja on 6.4.2017.
 */

public class MapGenJava extends JFrame implements ComponentListener{
    
    private float zoom = 100f;
    private int FRAME_WIDTH = 600;
    private int FRAME_HEIGHT = 600;
    

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    public MapGenJava (int mode) {
        setTitle("Map Generator");
        setSize(FRAME_WIDTH,FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new DrawPane());
        addComponentListener(this);
        setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent componentEvent) {
        FRAME_WIDTH = getWidth();
        FRAME_HEIGHT = getHeight();
    }

    @Override
    public void componentMoved(ComponentEvent componentEvent) {

    }

    @Override
    public void componentShown(ComponentEvent componentEvent) {

    }

    @Override
    public void componentHidden(ComponentEvent componentEvent) {

    }

    class DrawPane extends JPanel {
        public void paintComponent(Graphics g){
            clearScreen(g);
            g.dispose();
        }
        
        public void clearScreen(Graphics g) {
            //draw on g here e.g.
            g.setColor(Color.WHITE);
            g.fillRect(20, 20, 100, 200);
            g.setColor(Color.BLACK);
            
            int numOfLinesHorizontal = (int) (FRAME_HEIGHT / 20 * zoom / 100);
            int numOfLinesVertical = (int) (FRAME_WIDTH / 20 * zoom / 100);

            for (int x = 0; x < numOfLinesHorizontal; x++) {
                g.drawLine(0, (int)(x * 20* zoom/100),FRAME_WIDTH,(int)(x * 20* zoom/100));
            }

            for (int y = 0; y < numOfLinesVertical; y++) {
                g.drawLine((int)(y * 20* zoom/100), 0,(int)(y * 20* zoom/100),FRAME_HEIGHT);
            }
        }
    }
}
