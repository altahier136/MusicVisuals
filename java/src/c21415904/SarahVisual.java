package c21415904;

import ie.tudublin.visual.AudioAnalysis;
import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PVector;

import ddf.minim.AudioBuffer;

public class SarahVisual extends VScene {
    Visual v;
    VObject wf;
    VObject cwf;
    VObject hex;
    Spiral sp;
    VObject circamp;
    VObject sw;
    VObject mb;
    AudioBuffer ab;
    AudioAnalysis aa;
    
    public SarahVisual(Visual v) {
        super(v);
        this.v = v;

        ab = v.audioPlayer().mix;
        aa = v.audioAnalysis();

        wf = new WaveForm(v, new PVector(0, 0, 0));  
        cwf = new CircleWF(v, new PVector(0,0, 0));      
        hex = new Hex(v, new PVector(v.width/2, v.height/2, 0));    
        sp = new Spiral(v, new PVector(v.width/2, v.height/2, 0));  // spiral 
        circamp = new CirclesAmp(v, new PVector(0, 0, 0)); 
        sw = new soundWave(v, new PVector(0,0,0));
        mb = new metaBalls(v, new PVector(0,0,0));
    }

    public void render(int elapsed) {
        // 0:00 - 1:02 - Intro, V1, C1
        if (elapsed > v.toMs(0, 0, 0) && elapsed < v.toMs(0, 10, 0)) {
            sw.render();  
            //mb.render(); 
            //wf.render(); 
            //sw.render();
                              
        }
        
        if (elapsed > v.toMs(0, 10, 0) && elapsed < v.toMs(0, 20, 0)) {
            v.background(0);
            circamp.render();  
            //mb.render(); 
            //wf.render(); 
            //sw.render();                      
        }
        if (elapsed > v.toMs(0, 20, 0) && elapsed < v.toMs(0, 30, 0)) {
            v.background(0);
            sp.render();                    
        }
        if (elapsed > v.toMs(0, 30, 0) && elapsed < v.toMs(0, 40, 0)) {
            v.background(0);
            //sp.render();  
            mb.render(); 
            wf.render(); 
            //sw.render();                    
        }
         
        
        System.out.println(elapsed);
    }

    class Spiral extends VObject {

        Spiral(Visual v, PVector pos){
            super(v,pos);
            v.background(0);
        }

        float cx, cy;
        float rot = 0;        
        
        @Override
        public void render(){  

            v.background(0);
            float radius = 1f;
            v.translateCenter(PApplet.CENTER, PApplet.CENTER);   
            v.pushMatrix();
            cx = 0;//v.width/2;
            cy = 0;//v.height/2;
            for (int i = 0; i < ab.size(); i++) {
                
                float c = PApplet.map(i, 0, ab.size(), 0, 360); 
                v.strokeWeight(2);
                v.stroke(c, c, c);
                float theta =  i * (PApplet.TWO_PI/3 + aa.mix().lerpedAmplitude * 5);

                v.pushMatrix();
                float x = PApplet.sin(theta) * radius;
                float y = -PApplet.cos(theta) * radius;
                radius += 0.5f + aa.mix().lerpedAmplitude;
            
                //v.rotate(rot);
                v.line(cx, cy, x, y);
                cx = x;
                cy = y;

                v.popMatrix();

            } // end for

        v.popMatrix();   
        //rot += PApplet.QUARTER_PI/100f;  

        } // end render      

    } // end Spiral

    
    class CirclesAmp extends VObject {
        
        CirclesAmp(Visual v, PVector pos)
        {
            super(v, pos);
        }

        float rot = 0;

        @Override 
        public void render(){

            v.background(0);
            v.noFill();
            //v.camera(0, 0, 200, 0, 0, 0, 1, 0, 0);
            v.translateCenter(PApplet.CENTER, PApplet.CENTER);
            v.rotate(PApplet.radians(rot));

            float bands[] = aa.mix().lerpedBands;
            v.strokeWeight(2);
                
            for(int i = 0 ; i < bands.length; i ++)
            {
                float c = PApplet.map(i, 0, bands.length, 0, 360);
   
                v.stroke(c, 100, 100);
                float r = bands[i] * 10 + 100;

                v.beginShape();
                v.ellipseMode(PApplet.CENTER);
                v.circle(36, 36, r);
                v.circle(-36, -36, r);
                v.circle(36, -36, r);
                v.circle(-36, 36, r);     
                
                v.circle(50, 0, r);
                v.circle(-50, 0, r);
                v.circle(0, 50, r);
                v.circle(0, -50, r);
                v.endShape();

                c = PApplet.map(aa.mix().lerpedAmplitude, 0, bands.length, 0, 360);

                v.pushMatrix();
                v.stroke(c, 50, 50);
                v.strokeWeight(0.5f);
                v.rotateX(PApplet.radians(rot));
                v.rotateY(PApplet.radians(rot));
                v.rotateZ(PApplet.radians(rot));
                v.sphere(2000* aa.mix().lerpedAmplitude + 200);
                v.popMatrix();

            
            }
            rot += 1;  
        }

    }

  
    class WaveForm extends VObject{

        WaveForm(Visual v, PVector pos) {
            super(v, pos);
        }

        @Override
        public void render()
        {
            v.beginShape();
            for(int i = 0; i < ab.size(); i++)
            {                
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 50,100);
                v.strokeWeight(4);
                float f = ab.get(i) * v.height/2;
                float x = PApplet.map(i, 0, aa.mix().lerpedAmplitude * 10000, 0, v.width);
                v.line(x,v.height/2 + f, x, v.height/2 - f);
            }
            v.endShape();
        }
        
    }

    class CircleWF extends VObject{

        CircleWF(Visual v, PVector pos) {
            super(v, pos);
        }
        
        @Override
        public void render()
        {
            v.background(0);
            v.noFill();
            v.beginShape();
            v.translateCenter(PApplet.CENTER, PApplet.CENTER);
            for(int i = 0; i < ab.size(); i++)
            {
                v.stroke(100, 100, 100);
                float angle = PApplet.map(i, 0, ab.size(), 0, PApplet.TWO_PI);    
                float radius = ab.get(i) * 200 + 300;

                float x1 = PApplet.sin(angle) * radius;
                float y1 = PApplet.cos(angle) * radius;
                v.vertex((float)x1, (float)y1);           
            }
            v.endShape();


        }
        
    }

    class Hex extends VObject{

        Hex(Visual v, PVector pos) {
            super(v, pos);
        }

        @Override
        public void render()
        {
            v.background(0);
            v.noFill();
            v.translateCenter();
            v.beginShape();
            for(int i = 0; i < ab.size(); i++)
            {
                v.stroke(v.random(0,360), 100, 100);
                //float angle = PApplet.map(ab.get(i), 0, ab.size(), 0, PApplet.TWO_PI);    
                float radius = ab.get(i) * 300 + 50;
                double x1 = (PApplet.cos(i)*(PApplet.PI/180)  * 100 * radius);
                double y1 =  (PApplet.sin(i)*(PApplet.PI/180) * 100 * radius);
                v.vertex((float)x1, (float)y1);
            }
            v.endShape();
        }
        
    }


    class soundWave extends VObject{

        soundWave(Visual v, PVector pos)
        {
            super(v, pos);
        }

        float cx, cy;
        float theta = 0;
        float radius = 100;
        float rot = 0;

        @Override
        public void render()
        {
            //inner waveform
            cx = v.width/2;
            cy = v.height/2;
            v.noFill();    
            v.strokeWeight(2);
            v.background(0);

            v.pushMatrix();
            for(int i = 0; i < ab.size(); i++)
            {    
                float x1 = cx + PApplet.sin(theta) * radius;
                float y1 = cx - PApplet.cos(theta) * radius;

                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 100, 100);
                float f = ab.get(i) * 200 + 20;

                float x2 = (float)(x1 + (PApplet.sin(i)*(PApplet.PI/180)*radius * f));
                float y2 = (float)(y1 + (-PApplet.cos(i)*(PApplet.PI/180)*radius * f));
                //float x2 = (float)(x1 + (PApplet.sin(theta)*(PApplet.PI/180)*radius * f)); // lines straight out of circle
                //float y2 = (float)(y1 + (-PApplet.cos(theta)*(PApplet.PI/180)*radius * f)); 
                //float y2 = (float)(y1 - (-PApplet.cos(theta)*(Math.PI/180)*radius * f)); // makes a cool diamond shape
                
                v.line(x1, y1, x2, y2);
                theta += 0.1f;
                
            }
            v.popMatrix();

            //outer waveform
            v.beginShape();
            v.translateCenter(PApplet.CENTER, PApplet.CENTER);
            for(int i = 0; i < ab.size(); i++)
            {
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 100, 100);
                float angle = PApplet.map(i, 0, ab.size(), 0, PApplet.TWO_PI);    
                float radius = ab.get(i) * 200 + 300;

                float x1 = PApplet.sin(angle) * radius;
                float y1 = PApplet.cos(angle) * radius;
                v.vertex(x1, y1);           
            }
            v.endShape();

            
            v.beginShape();
            for(int i = 0; i < ab.size(); i++)
            {                
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c,100,100);
                v.strokeWeight(4);
                float f = ab.get(i) * v.height/2;
                float x = PApplet.map(i, 0, aa.mix().lerpedAmplitude * 10000, -v.width, v.width);
                v.line(x,v.height/2 + f, x, v.height/2 - f);
                v.line(x, -v.height/2 + f, x, -v.height/2 - f);
                v.line(-v.width/2, x - v.height/2, -v.width/2 + f, x - v.height/2);
                v.line(v.width/2, x - v.height/2, v.width/2 + f, x - v.height/2);

            }
            v.endShape();
            
      
        }
    }

    class metaBalls extends VObject {

        class Blob{
            PVector pos;
            PVector vel;
            float r;  
            Blob(float x, float y)
            {
                // super(new PVector(A, y))
                pos = new PVector(x, y);
                vel = PVector.random2D();
                vel.mult(v.random(2,5));
                r = 40;
            }

        
            public void update() {
                pos.add(vel);
                if(pos.x > v.width || pos.x < 0)
                {
                vel.x *= -1;
                }
                if(pos.y > v.height || pos.y < 0)
                {
                vel.y *= -1;
                }
            }

        }

        Blob[] blobs = new Blob[20];

        metaBalls(Visual v, PVector pos) {
            super(v, pos);
            for(int i = 0;  i < blobs.length; i++)
            {
                blobs[i] = new Blob(v.random(v.width), v.random(v.height));
            }
        }


        @Override 
        public synchronized void render(){
            v.background(0,0,50);

            v.beginShape();
            v.loadPixels();
            for(int x = 0; x < v.width; x+=4)
            {
                for(int y = 0; y < v.height; y+=4)
                {
                    int index = x + y * v.width;
                    float sum = 0;
                    for(Blob b: blobs)
                    {
                        float d = PApplet.dist(x, y, b.pos.x, b.pos.y);
                        sum += 100 * b.r / d * (aa.mix().lerpedAmplitude*50);
                    }
                    v.pixels[index] = v.color(sum % 360, 100, 100);
                    
                }
            }
            v.updatePixels();

            for(Blob b: blobs)
            {
                b.update();
            }

            v.endShape();
            
        }
    }
}