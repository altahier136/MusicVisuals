package c21348423;

import ie.tudublin.aavisual.VObject;
import ie.tudublin.aavisual.VScene;
import ie.tudublin.aavisual.Visual;
import processing.core.PApplet;
import processing.core.PVector;

public class AdriansVisual extends VScene {
    Visual v;
    VObject circle;

    public AdriansVisual(Visual v) {
        super(v);
        this.v = v;
        circle = new Circle(v, new PVector(0, 0, 0));
    }

    public void render(int elapsed) {
        // 1:48 - 2:30 - Instrumental
        if (elapsed > v.toMs(1, 48, 0) && elapsed < v.toMs(2, 30, 0)) {
            circle.render();
        }
        System.out.println(elapsed);
    }

    class Circle extends VObject {

        Circle(Visual v, PVector pos) {
            super(v, pos);
        }

        @Override
        public void render() {
            applyTransforms();
            v.colorMode(PApplet.RGB);
            v.fill(255, 0, 255);
            v.circle(v.width / 2, v.height / 2, v.analysisMix().lerpedAmplitude * 1000);
            v.popMatrix();
        }
    }
}
