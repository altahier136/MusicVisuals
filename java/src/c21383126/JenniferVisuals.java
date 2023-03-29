package c21383126;

import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
//import ie.tudublin.visual.VConstants.ChannelEnum;
import processing.core.PApplet;
//import processing.core.PVector;

public class JenniferVisuals extends VScene {
    Visual v;
   
    public JenniferVisuals(Visual v) {
        super(v);
        this.v = v;
    }

    public void render(int elapsed) {
      
        // 1:03 - 1:48 - Second verse & chorus
        if (elapsed > v.toMs(1, 3, 0) && elapsed < v.toMs(1, 48, 0)) {
            v.colorMode(PApplet.HSB, 360, 100, 100);
            v.background(0);
            v.stroke(255);
            int radius = 150;
            int border = radius + 20;

            // (x1,y1) (x3,y3)
            // (x2,y2) (x4,y4)

            int x1 = v.width/4;
            int y1 = v.height/4;
            int x2 = v.width/4;
            int y2 = v.height/4 * 3;
            int x3 = v.width/4 * 3;
            int y3 = v.height/4;
            int x4 = v.width/4 * 3;
            int y4 = v.height/4 * 3;

            v.rect(x1 - border, y1 - border, 2*border, 5*border);

            v.noStroke();
            v.frameRate(1);
            
            float h = v.random(0, 360);
            for (int r = radius; r > 0; --r) {
                v.fill(h, 90, 90);
                v.ellipse(x1, y1, r, r);
                v.ellipse(x2, y2, r, r);
                v.ellipse(x3, y3, r, r);
                v.ellipse(x4, y4, r, r);
                h = (h + 1) % 360;
            }          
        }
        System.out.println(elapsed);
    }
}
