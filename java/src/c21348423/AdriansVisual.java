package c21348423;

import ie.tudublin.Visual.Reactive;
import ie.tudublin.Visual.Scene;
import ie.tudublin.Visual.Visual;
import processing.core.PApplet;

public class AdriansVisual extends Scene {
    private Cube cube;

    public AdriansVisual(Visual visual) {
        super(visual);
        cube = new Cube(v);
    }

    public void render() {
        v.background(0);

        v.stroke(0, 0, 100, Math.abs(transition - 100) + 100);
        v.noFill();
        v.circle(v.width / 2, v.height / 2, v.getSmoothedAmplitude() * v.height);
        cube.render();
    }

}

class Cube extends Reactive {

    public Cube(Visual visual) {
        super(visual);
    }

    public void render() {
        v.pushMatrix();
        v.translate(v.width / 2, v.height / 2);
        v.rotateX(PApplet.radians(v.getSmoothedAmplitude() * 360));
        v.rotateY(PApplet.radians(v.getSmoothedAmplitude() * 360));
        v.rotateZ(PApplet.radians(v.getSmoothedAmplitude() * 360));
        v.box(100);
        v.popMatrix();
    }
}