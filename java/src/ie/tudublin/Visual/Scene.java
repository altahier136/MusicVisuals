package ie.tudublin.Visual;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Scene extends Object {
    private Visual v;

    protected Scene(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0));
    }
    Scene(Visual v, PVector position) {
        this(v, position, new PVector(0,0,0));
    }
    Scene(Visual v, PVector position, PVector rotation) {
        super(v, position, rotation);
    }

    public void render(int elapsed) {
        applyTransforms();
        v.colorMode(PApplet.RGB);
        v.fill(255, 0, 255);
        v.circle(10, 10, 10);
        v.popMatrix();
        System.out.println(this.getClass().getName() + "Warning: Empty Render Method");
    }

}
