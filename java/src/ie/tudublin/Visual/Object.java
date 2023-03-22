package ie.tudublin.Visual;

import processing.core.PVector;

public abstract class Object {

    private Visual v;
    public PVector position;
    public PVector rotation;

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

    abstract void render();

}
