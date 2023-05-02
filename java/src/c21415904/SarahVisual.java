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
    VObject so1;
    VObject wf;
    VObject cwf;
    VObject hex;
    VObject ecwf;
    Spiral sp;
    VObject circamp;
    VObject cube;
    VObject sw;
    VObject orb;
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
        cube = new Cube(v);
        sw = new soundWave(v, new PVector(0,0,0));
        orb = new orbit(v, new PVector(0,0,0));
        mb = new metaBalls(v, new PVector(0,0,0));
    }

    public void render(int elapsed) {
        // 1:48 - 2:30 - Instrumental
        if (elapsed > v.toMs(0, 0, 0) && elapsed < v.toMs(1, 2, 0)) {
            //sp.render();    
            //wf.render();
            //cube.render();
            //sw.render();
            //circamp.render();
            //td.render();
            //sw.render();
            //hex.render();
            //clock.render(elapsed);
            //orb.render();
            mb.render();
            
        }
        System.out.println(elapsed);
    }

    class Spiral extends VObject {

        Spiral(Visual v, PVector pos){
            super(v,pos);

            v.background(0);
        }

        float theta = 0.0f;
        float radius = 0.0f;
        float cx, cy;
        float rot = 0;
        float r = 20;
        
        
        @Override
        public void render(){    

            cx = v.width/2;
            cy = v.height/2;
            v.noFill();
            v.stroke(v.random(0,360), 100, 100);
            float x = cx + PApplet.sin(theta) * radius;
            float y = cx - PApplet.cos(theta) * radius;
            //float y2 = cx + PApplet.cos(theta) * radius;
            v.circle(x,y,20);
            //v.circle(x,y2,20);
            theta += 0.1f;
            radius += 0.5f;   
            

            /*
            v.noFill();
            v.beginShape();
            
            for(int i = 0; i < ab.size(); i++)
            {
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 100, 100);
                cx = v.width/2;
                cy = v.height/2;
                float x = cx + PApplet.sin(theta) * radius;
                float y = cx - PApplet.cos(theta) * radius;
                v.circle(x,y,20);
                theta += 0.1f;
                radius += 
            }
            v.endShape();
            */
        }
    }

    
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
                float c = PApplet.map(i, 0, bands.length, 0, 180);
                float c2 = PApplet.map(i, 0, bands.length, 180, 360);
   
                v.stroke(c, 100, 100);
                float r = bands[i] * 10 + 50;

                v.ellipseMode(PApplet.CENTER);
                v.circle(0 + 36, 0 + 36, r);
                v.circle(0 - 36, 0 - 36, r);
                v.circle(0 + 36, 0 - 36, r);
                v.circle(0 - 36, 0 + 36, r);     
                
                v.stroke(c2,100,100);
                v.circle(50, 0, r);
                v.circle(-50, 0, r);
                v.circle(0, 50, r);
                v.circle(0, -50, r);
            
            }
            rot += 1;  
        }

    }

    class orbit extends VObject {
        orbit(Visual v, PVector pos){
            super(v, pos);
        }

        float rot1 = 1.25f;
        float rot2 = -0.4f;
        public void render()
        {
            v.pushMatrix();
            v.translate(130, v.height/2, 0);
            v.rotateY(rot1++);
            v.rotateX(rot2++);
            v.rotateZ(rot1++);
            v.noStroke();
            v.box(100);
            v.popMatrix();

            v.pushMatrix();
            v.translate(500, v.height*0.35f, -200);
            v.noFill();
            v.stroke(255);
            v.sphere(280);
            v.popMatrix();
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
            v.background(0);
            for(int i = 0; i < ab.size(); i++)
            {                
                float c = PApplet.map(i, 0, ab.size(), 0, 360);
                v.stroke(c, 100, 100);
                float f = ab.get(i) * v.height/2;
                float x = PApplet.map(i, 0, ab.size(), 0, v.width);
                //v.line(x,v.height/4 + f, x, v.height/4 - f);
                v.line(x,y + f, x, y - f);
            }
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
            v.background(51);

            v.loadPixels();
            for(int x = 0; x < v.width; x++)
            {
                for(int y = 0; y < v.height; y++)
                {
                    int index = x + y * v.width;
                    float sum = 0;
                    for(Blob b: blobs)
                    {
                        float d = PApplet.dist(x, y, b.pos.x, b.pos.y);
                        sum += 50 * b.r / d * (aa.mix().lerpedAmplitude*50);
                    }
                    v.pixels[index] = v.color(sum, 100, 100);
                    
                }
            }
            v.updatePixels();

            for(Blob b: blobs)
            {
                b.update();
                //b.show(); 
            }
            
        }
    }

    
    class Cube extends VObject {

        Cube(Visual v){
            super(v);
        }
        
        float x, y;
        float size;
        float c;
        float rot = 0;

        @Override
        public void render(){

            v.background(0);
            v.stroke(100,100,100);
            v.strokeWeight(5);
            v.noFill();

            v.pushMatrix();
            v.translateCenter(PApplet.CENTER, PApplet.CENTER); 
            v.rotateY(rot);
            v.box(aa.mix().lerpedAmplitude * 1000 + 100);
            v.popMatrix();

            v.stroke(230,100,100);
            v.pushMatrix();
            v.translateCenter(PApplet.CENTER - 100, PApplet.CENTER + 100); 
            v.rotateY(rot);
            v.box(aa.mix().lerpedAmplitude * 1000 + 100);
            v.popMatrix();

            v.stroke(300,100,100);
            v.pushMatrix();
            v.translateCenter(PApplet.CENTER - 100, PApplet.CENTER - 100); 
            v.rotateY(rot);
            v.box(aa.mix().lerpedAmplitude * 1000 + 100);
            v.popMatrix();

            v.stroke(0,100,100);
            v.pushMatrix();
            v.translateCenter(PApplet.CENTER + 100, PApplet.CENTER - 100); 
            v.rotateY(rot);
            v.box(aa.mix().lerpedAmplitude * 1000 + 100);
            v.popMatrix();

            v.stroke(50,100,100);
            v.pushMatrix();
            v.translateCenter(PApplet.CENTER + 100, PApplet.CENTER + 100); 
            v.rotateY(rot);
            v.box(aa.mix().lerpedAmplitude * 1000 + 100);
            v.popMatrix();
            
            rot += 0.1f;
            

        }
    }
}