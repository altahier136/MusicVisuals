package global;

import com.jogamp.opengl.util.packrect.Rect;

import ddf.minim.AudioBuffer;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ie.tudublin.Visual.Scene;
import ie.tudublin.Visual.Visual;
import ie.tudublin.Visual.VisualConstants.ChannelEnum;
import processing.core.PApplet;

public class Demo extends Scene {
    float height4 = v.height / 4;
    float height8 = v.height / 8;
    float width4 = v.width / 4;

    public Demo(Visual v) {
        super(v);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void render() {
        // 4 sections
        v.colorMode(PApplet.HSB);
        for (int i = 0; i < 4; i++) {
            v.noStroke();
            v.fill(i * 50, 100, 50);
            v.pushMatrix();
            v.translate(0, i * v.height / 4);
            v.rect(0, 0, v.width, v.height / 4);
            v.popMatrix();
        }
        waveform();
        spectrum();
        spectrumLog();
        beat();
    }

    public void waveform() {
        float[] ab = v.analysisMix().waveform;
        for (int i = 0; i < ab.length; i++) {
            float x = PApplet.map(i, 0, ab.length, 0, v.width);
            float y = PApplet.map(ab[i], -1, 1, height8, -height8);
            v.stroke(255);
            v.pushMatrix();
            v.translate(0, height4 * 4 - height8);
            v.line(x, 0, x, y);
            v.popMatrix();
        }
    }

    public void spectrum() {
        FFT fft = v.fft();
        for (int i = 0; i < fft.specSize(); i++) {
            float x = PApplet.map(i, 0, fft.specSize(), 0, v.width);
            float h = PApplet.map(fft.getBand(i), 0, 100, 0, -height4);

            v.stroke(255);
            v.pushMatrix();
            v.translate(0, height4 * 3);
            v.line(x, 0, x, h);
            v.popMatrix();
        }

    }

    public void spectrumLog() {
        FFT fft = v.fft();
        for (int i = 0; i < fft.specSize(); i++) {
            float x = PApplet.map(i, 0, fft.specSize(), 0, v.width);
            float h = PApplet.map(fft.getBand(i), 0, 100, 0, -height4);

            v.stroke(255);
            v.pushMatrix();
            v.translate(0, height4 * 2);
            v.line(x, 0, x, h);
            v.popMatrix();
        }
    }

    public void beat() {
        BeatDetect beat = v.beat();
        float hat, kick, snare;
        hat = kick = snare = 0;
        // 4 rects int a row, one for isHat, isKick, isSnare
        if (beat.isHat())
            hat = 255;
        if (beat.isKick())
            kick = 255;
        if (beat.isSnare())
            snare = 255;

        // hat = Visual.lerp(hat, 0, .9f);
        // kick = Visual.lerp(kick, 0, .9f);
        // snare = Visual.lerp(snare, 0, .9f);

        v.pushMatrix();
        v.noStroke();
        v.fill(255);
        v.text("Hat", 0, 0);
        v.text("Kick", width4, 0);
        v.text("Snare", width4 * 2, 0);

        v.fill(255, 0);
        v.translate(0, 0);
        v.textAlign(PApplet.LEFT, PApplet.TOP);
        v.fill(255, hat);
        v.rect(0, 0, width4, height4);
        v.fill(255, kick);
        v.rect(width4, 0, width4, height4);
        v.fill(255, snare);
        v.rect(width4 * 2, 0, width4, height4);
        v.popMatrix();

    }

}
