package c21383126;

import ie.tudublin.visual.AudioAnalysis;
import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import ddf.minim.AudioBuffer;
import ddf.minim.Minim;

public class JenniferVisuals extends VScene {
    Visual v;
    VObject speaker;
    VObject circle;
    VObject sw;
    Clock clock;
    VObject dots;
    VObject stars;
    VObject wf;

    Minim minim;
    AudioBuffer ab;
    AudioAnalysis aa;

   
    public JenniferVisuals(Visual v) {
        super(v);
        this.v = v;
        speaker = new Speaker(v, new PVector(v.width/4, v.height/4));
        circle = new Circles(v, new PVector(v.width/4, v.height/4));
        sw = new SinWave(v, new PVector(v.width/4, v.height/4));
        clock = new Clock(v, new PVector(v.width/4, v.height/4));
        dots = new Dots(v, new PVector(v.width/4, v.height/4));
        stars = new Stars(v, new PVector(v.width/4, v.height/4));
        wf = new WaveForm(v, new PVector(v.width/4, v.height/4));


        minim = new Minim(this);
        ab = v.audioPlayer().mix;
        aa = v.audioAnalysis();

    }

    public void render(int elapsed) {
      
        // 1:03 - 1:48 - Second verse & chorus
        if (elapsed > v.toMs(1, 3,0) && elapsed < v.toMs(1, 48, 0)) 
        {
            //wf.render();
            //speaker.render();

            //circle.render();   
                  
            //sw.render(); 

            //dots.render(elapsed);  
            //clock.render(elapsed);

            stars.render(elapsed);
        }
        System.out.println(elapsed);
    }




    class Stars extends VObject {

        PShape star;
        Stars(Visual v, PVector pos){
            super(v, pos);
            star = v.loadShape("estrellica.obj");
        }
        
        int OFF_MAX = 150;
        float rot = 0;
        @Override
        public void render(int elapsed)
        {
            v.lights();
            float avg = aa.mix().lerpedAmplitude; 
            v.translate(v.width/2, v.height/2, 0);        
            v.rotateX(rot * .01f);
            v.rotateY(rot * .01f);
            v.rotateZ(rot * .01f);
            for(int x = -OFF_MAX; x <= OFF_MAX; x += 50)
            {
                for(int y = -OFF_MAX; y <= OFF_MAX; y += 50)
                {
                    for(int z = -OFF_MAX; z <= OFF_MAX; z += 50)
                    {
                        v.pushMatrix();
                        v.translate(x, y, z);
                        v.rotateX(rot * 0.02f);
                        v.rotateY(rot * .02f);
                        v.rotateZ(rot * .02f);
                        
                        float c = v.noise(elapsed / (100f), x + y + z) * 360;
                        star.setFill(v.color(c, 100, 100));
                        v.scale(avg * 50);
                        v.shape(star);
                        v.popMatrix();
                    }
                }
            }
            rot++;            
            
        }

    }

    class Dots extends VObject { 
        Dots(Visual v, PVector pos){
            super(v, pos);
        }
        
        @Override
        public void render(){

            float max_distance;
            v.background(0);
            v.noStroke();
            max_distance = PApplet.dist(0, 0, v.width, v.height);

            for(int i = 0; i <= v.width; i += 20) {
                for(int j = 0; j <= v.height; j += 20) {
                    float c = PApplet.map(i, 0, ab.size(), 0, 360);
                    v.fill(c, 100, 100);
                    float size = PApplet.dist(v.random(0, v.width), v.random(0, v.height), i, j);
                    size = size/max_distance * 66;
                    v.ellipse(i, j, size, size);
                }
            }
        }
    }

    
    class Clock extends VObject {

        Clock(Visual v, PVector pos) {
            super(v, pos);
        }

        public void render(int elapsed) {
            int cx, cy;
            float secondsRadius;
            float minutesRadius;
            float hoursRadius;
            float clockDiameter;

            v.fill(0);
            v.stroke(255);
            
            int radius = PApplet.min(v.width, v.height) / 3;
            secondsRadius = (float)(radius * 0.72);
            minutesRadius = (float)(radius * 0.60);
            hoursRadius = (float)(radius * 0.50);
            clockDiameter = (float)(radius * 1.8);
            
            cx = v.width / 2;
            cy = v.height / 2;
            
            v.circle(cx, cy, clockDiameter + v.audioAnalysis().mix().lerpedAmplitude * 1000);
            
            // Angles for sin() and cos() start at 3 o'clock;
            // subtract HALF_PI to make them start at the top
            // Seconds hand ticks in time to the beat (96BPM) -> 96/60 = 1.6
            float s = PApplet.map((int)(elapsed/(1000/1.6f)), 0, 60, 0, PApplet.TWO_PI) - PApplet.HALF_PI;
            float m = PApplet.map(PApplet.minute() + PApplet.norm(PApplet.second(), 0, 60), 0, 60, 0, PApplet.TWO_PI) - PApplet.HALF_PI; 
            float h = PApplet.map(PApplet.hour() + PApplet.norm(PApplet.minute(), 0, 60), 0, 24, 0, PApplet.TWO_PI * 2) - PApplet.HALF_PI;
            
            // Draw the hands of the clock
            v.stroke(255);
            v.strokeWeight(3);
            v.line(cx, cy, cx + PApplet.cos(s) * secondsRadius, cy + PApplet.sin(s) * secondsRadius);
            v.strokeWeight(4);
            v.line(cx, cy, cx + PApplet.cos(m) * minutesRadius, cy + PApplet.sin(m) * minutesRadius);
            v.strokeWeight(8);
            v.line(cx, cy, cx + PApplet.cos(h) * hoursRadius, cy + PApplet.sin(h) * hoursRadius);
            
            
            // Draw the minute ticks
            v.strokeWeight(5);
            v.beginShape(PApplet.POINTS);
            for (int a = 0; a < 360; a+=6) {
                float angle = PApplet.radians(a);
                float x = cx + PApplet.cos(angle) * secondsRadius;
                float y = cy + PApplet.sin(angle) * secondsRadius;
                v.vertex(x, y);
            }
            v.strokeWeight(2);
            v.endShape();
            
        }

    }   


    class SinWave extends VObject{
        SinWave(Visual v, PVector pos){
            super(v, pos);
        }

        int xspacing = 16;   // How far apart should each horizontal location be spaced
        int w = v.width + 16;              // Width of entire wave

        float theta = 0;  // Start angle at 0
        float amplitude = 75;  // Height of wave
        float period = 500;  // How many pixels before the wave repeats
        float dx = (PApplet.TWO_PI / period) * xspacing;  // Value for incrementing X, a function of period and xspacing
        float[] yvalues = new float[w/xspacing];  // Using an array to store height values for the wave
        
        void calcWave() {
            // Increment theta (try different values for 'angular velocity' here
            theta += 0.02;
          
            // For every x value, calculate a y value with sine function
            float x = theta;
            for (int i = 0; i < yvalues.length; i++) {
              yvalues[i] = PApplet.sin(x)*amplitude;
              x+=dx;
            }
          }

          void renderWave() {
            v.noStroke();
            //v.fill(255);
            // A simple way to draw the wave with an ellipse at each location
            for (int x = 0; x < yvalues.length; x++) 
            {
                float c = PApplet.map(ab.get(x), -1, 1, 0, 360);
                v.fill(c, 100, 100);
                v.ellipse(x*xspacing, v.height/2+yvalues[x], 16, 16);
                v.fill(c+40, 100, 100);
                v.ellipse(x*xspacing, v.height/4+yvalues[x], 16, 16);
                v.fill(c+80, 100, 100);
                v.ellipse(x*xspacing, (3*v.height/4)+yvalues[x], 16, 16);
            }
          }

        @Override
        public void render(){
            calcWave();
            renderWave();
        }
    }

    class Circles extends VObject {
        Circles(Visual v, PVector pos){
            super(v, pos);
        }

        @Override
        public void render(){
            for (int i=0; i<360; i++)
            {
                // (x2, y2) (x3, y3)
                //     (x1, x2)
                // (x4, y4) (x5, y5)

                v.stroke(109, 247, 240);
                float f = ab.get(i) * v.height/2;
                double x1 = v.width/2 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y1 = v.height/2 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(v.width/2, v.height/2, (float)x1, (float)y1);

                v.stroke(176, 65, 240);
                double x2 = v.width/4 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y2 = v.height/4 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(v.width/4, v.height/4, (float)x2, (float)y2);

                double x3 = 3*v.width/4 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y3 = v.height/4 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(3*v.width/4, v.height/4, (float)x3, (float)y3);

                double x4 = v.width/4 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y4 = 3*v.height/4 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(v.width/4, 3*v.height/4, (float)x4, (float)y4);

                double x5 = 3*v.width/4 + (Math.cos(i)*(Math.PI/180) * 100 * f);
                double y5 = 3*v.height/4 + (Math.sin(i)*(Math.PI/180) * 100 * f);
                v.line(3*v.width/4, 3*v.height/4, (float)x5, (float)y5);
                
            }
        }
    }

    class Speaker extends VObject {

        Speaker(Visual v, PVector pos){
            super(v, pos);
        }

        @Override
        public void render(){
            v.stroke(255);

            // (x1,y1) (x3,y3)
            // (x2,y2) (x4,y4)
            
            int x1 = v.width/3;
            int y1 = v.height/3;
            int x2 = v.width/3;
            int y2 = v.height/3 * 2;
            int x3 = v.width/3 * 2;
            int y3 = v.height/3;
            int x4 = v.width/3 * 2;
            int y4 = v.height/3 * 2;

            //int radius = 100;
            int border = 105;            

            int length = ((y2 + border) - (y1 - border));
            int width = ((x2 + border) - (x1 - border));
            float col = v.random(0,360);
            v.fill(col, 100, 100);
            //v.rect(x1 - border, y1 - border, width, length);
            v.translate(x1, v.height/2);
            v.rotateX((float)1.5 * 0.2f);
            v.box(width, length, width);

            v.translate(-x1, -v.height/2);
            v.translate(x3, v.height/2);
            //v.rect(x3 - border, y3 - border, width, length);
            v.box(width, length, width);

            v.noStroke();
            v.frameRate(1); 
            v.noFill();
            v.translate(-x3, -v.height/2, width);
            v.rotateX((float).02 * 0.2f);
            
            for(int i=0; i< ab.size(); i++)
            {
                float c = PApplet.map(ab.get(i), -1, 1, 0, 360);
                v.stroke(c, 100, 100);
                float radius = ab.get(i) * 1000 + 50;
                v.circle(x1+30, y1+60, radius-1);
                v.circle(x2+30, y2, radius-1);
                v.circle(x3-30, y3+60, radius-1);
                v.circle(x4-30, y4, radius-1);
            }

            /*
            float h = v.random(ab.get(radius), 360);
            for (int r = radius; r > 0; --r) {
                v.fill(h, 90, 90);
                v.ellipse(x1, y1, r, r);
                v.ellipse(x2, y2, r, r);
                v.ellipse(x3, y3, r, r);
                v.ellipse(x4, y4, r, r);
                h = (h + 1) % 360;
            }    
            */
        }
        
    }

    class WaveForm extends VObject{
        WaveForm(Visual v, PVector pos) {
            super(v, pos);
        }

        float y = v.height;
        @Override
        public void render()
        {
            for(int i = 0; i < ab.size(); i++)
            {                
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 100, 100);
                float f = ab.get(i) * v.height/2;
                float x = PApplet.map(i, 0, ab.size(), 0, v.width);
                //v.line(x,v.height/4 + f, x, v.height/4 - f);
                v.line(x, y + f, x, y - f);
                v.line(x, f, x, - f);
                
            }
        }
        
    }


}
