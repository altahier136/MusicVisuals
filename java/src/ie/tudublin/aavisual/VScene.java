package ie.tudublin.aavisual;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Base class for all objects in the scene which will control rendering of your
 * objects within the scene, this class is then called by the subclass of
 * the Visual class<br><br>
 * Example:<br>
 * <pre><code>
 * public class MyScene extends Scene {
 *    BeatCircle circle;
 *    public void MyScene(Visual v) {
 *        super(v);
 *        circle = new BeatCircle(v);
 * }
 * <pre><code>
 * @see {@link VObject}
 */
public abstract class VScene extends VObject {
    protected VScene(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0));
    }
    VScene(Visual v, PVector position) {
        this(v, position, new PVector(0,0,0));
    }
    VScene(Visual v, PVector position, PVector rotation) {
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
