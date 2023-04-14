package global;

import ie.tudublin.visual.AudioAnalysis;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PVector;

public class GlobalVisual extends VScene {

    public GlobalVisual(Visual v) {
        super(v);
    }

    @Override
    public void render(int elapsed) {
        {
            // Waveform
            // Slowly Fades away during the instrumentals at 1:48 -> 2:30
            float effect = 1;
            int start = v.toMs(2, 48, 0);
            int end = v.toMs(3, 30, 0);
            int transDurHalf = 500;

            if (elapsed >= start - transDurHalf
                    && elapsed <= start + transDurHalf) {

            }

        }

    }

    private void waveformFrame(float effect) {
        effect = PApplet.constrain(effect, 0, 1);
        float QUARTER_PI = Visual.QUARTER_PI;
        v.noFill();
        v.stroke(255, PApplet.map(effect, 0, 1, 0, 255));

        // Waveform line top left
        v.pushMatrix();
        v.translate(0, 0, 0);
        v.rotate(QUARTER_PI);
        waveformLine(new PVector(0, 0, 0), new PVector(0, 0, -4800), v.audioAnalysis());
        v.popMatrix();

        // Waveform line top right
        v.pushMatrix();
        v.translate(v.width, 0, 0);
        v.rotate(QUARTER_PI * 3);
        waveformLine(new PVector(0, 0, 0), new PVector(0, 0, -4800), v.audioAnalysis());
        v.popMatrix();

        // Waveform line bottom left
        v.pushMatrix();
        v.translate(0, v.height, 0);
        v.rotate(QUARTER_PI * 7);
        waveformLine(new PVector(0, 0, 0), new PVector(0, 0, -4800), v.audioAnalysis());
        v.popMatrix();

        // Waveform line bottom right
        v.pushMatrix();
        v.translate(v.width, v.height, 0);
        v.rotate(QUARTER_PI * 5);
        waveformLine(new PVector(0, 0, 0), new PVector(0, 0, -4800), v.audioAnalysis());
        v.popMatrix();

    }

    private void waveformLine(PVector p1, PVector p2, AudioAnalysis aa) {
        v.beginShape();
        float[] waveform = aa.mix().waveform;
        for (int i = 0; i < waveform.length; i++) {
            float x = Visual.lerp(p1.x, p2.x, i / (float) waveform.length);
            float y = Visual.lerp(p1.y, p2.y, i / (float) waveform.length);
            float z = Visual.lerp(p1.z, p2.z, i / (float) waveform.length);

            y += waveform[i] * 1000;

            v.vertex(x, y, z);
        }
        v.endShape();
    }

}
