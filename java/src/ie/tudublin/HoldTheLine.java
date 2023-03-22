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
    AdriansVisual av;

    HoldTheLine() throws VisualException {
        super(1024, 44100);
    }

    public void settings() {
        fullScreen(P3D);
    }

    public void setup() {
    }

    public void draw() {
    }
}
