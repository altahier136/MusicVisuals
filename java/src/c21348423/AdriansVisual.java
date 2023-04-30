package c21348423;

import ie.tudublin.visual.EaseFunction;
import ie.tudublin.visual.VAnimation;
import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PVector;

public class AdriansVisual extends VScene {
    VAnimation sceneVisibility;
    Visual v;
    VObject circle;

    // Colour pallet

    private int background;
    private int foreground;
    private int magenta;
    private int cyan;
    private int yellow;

    public AdriansVisual(Visual v) {
        super(v);
        this.v = v;
        background = v.color(0, 0, 100);
        foreground = v.color(0, 0, 0);
        magenta = v.color(300, 100, 100);
        cyan = v.color(180, 100, 100);
        yellow = v.color(60, 100, 100);

        circle = new Circle(v, new PVector(0, 0, 0));

        setSceneAnimation();
    }

    public void setSceneAnimation() {
        sceneVisibility = new VAnimation(v.audioPlayer().length());
        sceneVisibility.addTransition(v.toMs(1, 48, 0), 0, 0, 100, EaseFunction.easeLinear);
        sceneVisibility.addTransition(v.toMs(2, 31, 0), 0, 100, 0, EaseFunction.easeLinear);
        sceneVisibility.addTransition(v.audioPlayer().length(), 0, 0, 0, EaseFunction.easeLinear);
    }

    public void render(int elapsed) {
        System.out.println(sceneVisibility.getValue(elapsed));
        if (Float.isNaN(sceneVisibility.getValue(elapsed)))
        System.out.println("Breakpoint");;

        if (Math.round(sceneVisibility.getValue(elapsed)) == 0) {
            return;
        }
        v.blendMode(PApplet.BLEND);
        v.fill(background, 20);
        v.rect(0, 0, v.width, v.height);
        v.blendMode(PApplet.SUBTRACT);
        // 1:48 - 2:30 - Instrumental
        circle.render();
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
            v.circle(v.width / 2, v.height / 2, v.audioAnalysis().mix().amplitude * 1000);
            v.popMatrix();
        }
    }
}
