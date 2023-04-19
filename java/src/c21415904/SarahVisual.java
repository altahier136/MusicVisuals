package c21415904;

import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PVector;
import ddf.minim.AudioBuffer;

public class SarahVisual extends VScene {
    Visual v;
    Clock clock;
    VObject so1;
    VObject wf;

    AudioBuffer ab;
    

    public SarahVisual(Visual v) {
        super(v);
        this.v = v;

        ab = v.audioPlayer().mix;

        clock = new Clock(v, new PVector(0, 0, 0));
        so1 = new SphereOrbit(v, new PVector(0, 0, 0));
        wf = new WaveForm(v, new PVector(0, 0, 0));       
    }

    public void render(int elapsed) {
        // 1:48 - 2:30 - Instrumental
        if (elapsed > v.toMs(0, 0, 0) && elapsed < v.toMs(1, 0, 2)) {
            //clock.render(elapsed);
            //so1.render();
            wf.render();
        }
        System.out.println(elapsed);
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

            v.background(0);
            v.noFill();   
            v.stroke(255);
            
            int radius = PApplet.min(v.width, v.height) / 2;
            secondsRadius = (float)(radius * 0.72);
            minutesRadius = (float)(radius * 0.60);
            hoursRadius = (float)(radius * 0.50);
            clockDiameter = (float)(radius * 1.8);
            
            cx = v.width / 2;
            cy = v.height / 2;
            

            v.circle(cx, cy, clockDiameter + v.analysisMix().lerpedAmplitude*1000);
            
            // Angles for sin() and cos() start at 3 o'clock;
            // subtract HALF_PI to make them start at the top
            // Seconds hand ticks in time to the beat (96BPM) -> 96/60 = 1.6
            float s = PApplet.map((int)(elapsed/(1000/1.6f)), 0, 60, 0, PApplet.TWO_PI) - PApplet.HALF_PI;
            float m = PApplet.map(PApplet.minute() + PApplet.norm(PApplet.second(), 0, 60), 0, 60, 0, PApplet.TWO_PI) - PApplet.HALF_PI; 
            float h = PApplet.map(PApplet.hour() + PApplet.norm(PApplet.minute(), 0, 60), 0, 24, 0, PApplet.TWO_PI * 2) - PApplet.HALF_PI;
            
            // Draw the hands of the clock
            v.stroke(255);
            v.strokeWeight(1);
            v.line(cx, cy, cx + PApplet.cos(s) * secondsRadius, cy + PApplet.sin(s) * secondsRadius);
            v.strokeWeight(2);
            v.line(cx, cy, cx + PApplet.cos(m) * minutesRadius, cy + PApplet.sin(m) * minutesRadius);
            v.strokeWeight(4);
            v.line(cx, cy, cx + PApplet.cos(h) * hoursRadius, cy + PApplet.sin(h) * hoursRadius);
            
             
            // Draw the minute ticks
            v.strokeWeight(2);
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

    class SphereOrbit extends VObject{

        SphereOrbit(Visual v, PVector pos) 
        {
            super(v, pos);
        }
        
        public void render() {

            v.pushMatrix();
            this.rotation.y += 10;
            applyTransforms(); // Push matrix

            v.fill(255);
            v.pushMatrix();
            v.translate(v.width, v.height/2, 0); 

            v.box(100);
            v.popMatrix();
            // End the apply Transform
            v.popMatrix();
            
        }
    }

    class WaveForm extends VObject{

        WaveForm(Visual v, PVector pos) {
            super(v, pos);
        }

        @Override
        public void render()
        {
            for(int i = 0; i < ab.size(); i++)
            {
                float c = PApplet.map(ab.get(i), -1, 1, 0, 255);
                v.stroke(c, 255, 255);
                float f = ab.get(i) * v.height/2;
                v.line(i,v.height/2 + f, i , v.height/2 - f);
            }
        }
        
    }
    
}

