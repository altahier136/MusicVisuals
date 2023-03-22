package ie.tudublin.Visual;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Object {

    private Visual v;
    public PVector position;
    public PVector rotation;
    public PVector scale;

    public Visual visual() {
        return v;
    }
    public PVector position() {
        return position;
    }
    public PVector rotation() {
        return rotation;
    }
    public PVector scale() {
        return scale;
    }

    Object(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0));
    }
    Object(Visual v, PVector position) {
        this(v, position, new PVector(0,0,0));
    }
    Object(Visual v, PVector position, PVector rotation) {
        this.v = v;
        this.position = position;
        this.rotation = rotation;
    }

    public void render() {
        applyTransforms();
        v.colorMode(PApplet.RGB);
        v.fill(255, 0, 255);
        v.circle(10, 10, 10);
        v.popMatrix();
        System.out.println(this.getClass().getName() + "Warning: Empty Render Method");
    }

    public void applyTransforms() {
        v.pushMatrix();
        v.translate(position.x, position.y, position.z);
        v.rotateX(rotation.x);
        v.rotateY(rotation.y);
        v.rotateZ(rotation.z);
        v.scale(scale.x, scale.y, scale.z);
    }

}
