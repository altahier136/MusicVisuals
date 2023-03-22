package ie.tudublin;

import ie.tudublin.Visual.*;
import c21348423.AdriansVisual;

/*
    Natural breaks in the song:
    0:00 - 1:03 - Intro & first verse & chorus
    1:03 - 1:48 - Second verse & chorus
    1:48 - 2:30 - Instrumental
    2:30 - 3:33 - Third verse & chorus(extra)
    3:33 - 3:54 - Outro
*/
public class HoldTheLine extends Visual {
    Scene av;
    int start;
    int elapsed;

    HoldTheLine() {
        super(1024, 44100);
        av = new AdriansVisual(this);
    }

    public void settings() {
        fullScreen(P3D);
    }

    public void setup() {
        beginAudio("Toto - Hold The Line.wav");
        start = millis();
    }

    public void draw() {
        elapsed = millis() - start;
        background(0);
        lerpAmplitude(0.9f);
        av.render(elapsed);
    }
}
