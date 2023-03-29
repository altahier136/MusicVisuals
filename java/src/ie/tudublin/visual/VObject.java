package ie.tudublin.visual;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Base class for all objects in the scene
 * If you are creating a new object, extend this class
 * and override the render method <br>
 * <br>
 * Fields, getters and setters:
 * <ul>
 * <li>{@link #visual()}</li>
 * <li>{@link #position()}</li>
 * <li>{@link #rotation()}</li>
 * <li>{@link #scale()}</li>
 * </ul>
 *
 * Example:
 * <pre><code>
 *  public class MyScene extends Scene {
 *      BeatCircle circle;
 *      public void MyScene(Visual v) {
 *          super(v);
 *          circle = new BeatCircle(v);
 *      }
 *
 *      public void render() {
 *      circle.render();
 *      }

 *      class BeatCircle extends Object {
 *          public BeatCircle(Visual v) {
 *          super(v);
 *          }
 *          public void render() {
 *              // Draw a circle
 *          }
 *      }
 * }
 * </code></pre>
 */
public abstract class VObject {

    public Visual v;
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

    protected VObject(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0), new PVector(1,1,1));
    }
    protected VObject(Visual v, PVector position) {
        this(v, position, new PVector(0,0,0), new PVector(1,1,1));
    }
    protected VObject(Visual v, PVector position, PVector rotation) {
        this(v, position, rotation, new PVector(1,1,1));
    }
    protected VObject(Visual v, PVector position, PVector rotation, PVector scale) {
        this.v = v;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Render the object.
     * object.render() is called in the render method of the scene
     */
    public void render() {
        applyTransforms();
        v.colorMode(PApplet.RGB);
        v.fill(255, 0, 255);
        v.circle(10, 10, 10);
        v.popMatrix();
        System.out.println(this.getClass().getName() + "Warning: Empty Render Method");
    }

    /**
     * Apply the transforms to the object. <br><br>
     * Example:
     * <pre><code>
     * public void render() {
     *    v.pushMatrix();
     *    position.x = 10;
     *    position.y = 10;
     *    applyTransforms(); // Object can now be rendered at 10, 10
     *    v.circle(0, 0, 10);
     *    v.popMatrix();
     * }
     * </code></pre>
     */
    public void applyTransforms() {
        v.pushMatrix();
        v.translate(position.x, position.y, position.z);
        v.rotateX(rotation.x);
        v.rotateY(rotation.y);
        v.rotateZ(rotation.z);
        v.scale(scale.x, scale.y, scale.z);
    }

}
