package c21383126;

import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
//import ie.tudublin.visual.VConstants.ChannelEnum;
import processing.core.PApplet;
import processing.core.PVector;
import ddf.minim.AudioBuffer;
import ddf.minim.Minim;



public class JenniferVisuals extends VScene {
    Visual v;
    VObject speaker;
    VObject circle;

    Minim minim;
    AudioBuffer ab;

   
    public JenniferVisuals(Visual v) {
        super(v);
        this.v = v;
        speaker = new Speaker(v, new PVector(v.width/4, v.height/4));
        circle = new Circles(v, new PVector(v.width/4, v.height/4));

        minim = new Minim(this);
        ab = v.audioPlayer().mix;

    }

    public void render(int elapsed) {
      
        // 1:03 - 1:48 - Second verse & chorus
        if (elapsed > v.toMs(1, 3,0) && elapsed < v.toMs(1, 48, 0)) 
        {
            //speaker.render();
            circle.render();                  
        }
        System.out.println(elapsed);
    }

    class Circles extends VObject {
        Circles(Visual v, PVector pos){
            super(v, pos);
        }

        @Override
        public void render(){
            v.background(0);
            for (int i=0; i<360; i++)
            {
                float c = v.map(ab.get(i), -1, 1, 0, 255);
                v.stroke(c, 255, 255);
                float f = ab.get(i) * v.height/2;
                double x2 = v.width/2 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y2 = v.height/2 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(v.width/2, v.height/2, (float)x2, (float)y2);

                double x3 = v.width/4 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y3 = v.height/4 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(v.width/4, v.height/4, (float)x3, (float)y3);
                
            }
        }
    }

    class Speaker extends VObject {

        Speaker(Visual v, PVector pos){
            super(v, pos);
        }

        @Override
        public void render(){
            v.colorMode(PApplet.HSB, 360, 100, 100);
            v.background(360);
            v.stroke(255);

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

            int radius = 200;
            int border = radius + 5;            

            int length = ((y2 + border) - (y1 - border));
            int width = ((x2 + border) - (x1 - border));
            v.fill(100);
            v.rect(x1 - border, y1 - border, width, length);

            v.rect(x3 - border, y3 - border, width, length);

            v.noStroke();
            v.frameRate(1); //1/96 of a second bc 96bpm
           
            //float h = v.random(0, 360);
            float h = v.random(ab.get(radius), 360);
            for (int r = radius; r > 0; --r) {
                v.fill(h, 90, 90);
                v.ellipse(x1, y1, r, r);
                v.ellipse(x2, y2, r, r);
                v.ellipse(x3, y3, r, r);
                v.ellipse(x4, y4, r, r);
                h = (h + 1) % 360;
            }    
        }
        
    }

}
