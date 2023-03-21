package ie.tudublin;

import ie.tudublin.Visual.*;
import c21348423.AdriansVisual;

public class HoldTheLine extends Visual {
    AdriansVisual av;

    HoldTheLine() throws VisualException {
        super(1024, 44100);
    }

    public void settings() {
        fullScreen(P3D);
    }

    public void setup() {
        startMinim();
        loadAudio("Toto - Hold The Line.wav");
        getAudioPlayer().play();

        colorMode(HSB, 360, 100, 100, 100);

        // Scenes
        av = new AdriansVisual(this);
        av.setTransition(100);
    }

    public void draw() {
        background(0);
        calculateFrequencyBands();
        calculateAverageAmplitude();

        av.render();


    }
}
