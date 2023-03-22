package c21348423;

import ie.tudublin.Visual.Reactive;
import ie.tudublin.Visual.Scene;
import ie.tudublin.Visual.Visual;
import ie.tudublin.Visual.VisualConstants.ChannelEnum;
import processing.core.PApplet;
import processing.core.PVector;

public class AdriansVisual extends Scene {
    Visual v;
    Reactive circle;
    public AdriansVisual(Visual v) {
        super(v);
        this.v = v;
        circle = new Circle(v, new PVector(0, 0, 0), new PVector(0, 0, 0), ChannelEnum.MIX, true);
    }

    public void render() {
        applyTransforms();


        v.popMatrix();
    }

    class Circle extends Reactive {

        Circle(Visual v, PVector pos, PVector vel, ChannelEnum channel, boolean lerped) {
            super(v, pos, vel, channel, lerped);
        }

        @Override
        public void render() {
            applyTransforms();
            v.colorMode(PApplet.RGB);
            v.fill(255, 0, 255);
            v.circle(v.width / 2, v.height / 2, v.lerpedAmplitude() * 100);
            v.popMatrix();
        }
    }
}
