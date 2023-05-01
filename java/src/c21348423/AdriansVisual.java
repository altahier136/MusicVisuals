package c21348423;

import com.jogamp.common.nio.Buffers;

import ie.tudublin.visual.AudioAnalysis;
import ie.tudublin.visual.EaseFunction;
import ie.tudublin.visual.VAnimation;
import ie.tudublin.visual.VObject;
import ie.tudublin.visual.VScene;
import ie.tudublin.visual.Visual;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

public class AdriansVisual extends VScene {

    public final int BPM = 96;
    public final float BEATMS = 1 / (1000 / (BPM / 60.0f)); // Multiply with elapsed to get beat

    VAnimation sceneVisibility;
    Visual v;
    VObject circle;
    SquigglyArcs squigglyArcs;
    HappyHorse horse;
    SuperStars superStars;

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
        horse = new HappyHorse(v, new PVector(0, 0, 0));
        squigglyArcs = new SquigglyArcs(v, new PVector(0, 0, 0));
        superStars = new SuperStars(v, new PVector(0, 0, 0));

        v.seek(1, 48);
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
            System.out.println("Breakpoint");
        ;

        if (Math.round(sceneVisibility.getValue(elapsed)) == 0) {
            return;
        }

        v.blendMode(PApplet.BLEND);
        // v.background(0);
        v.fill(background, 20);
        v.pushMatrix();
        v.translateCenter();
        v.translate(0, 0, -1000);
        v.rectMode(PApplet.CENTER);
        v.rect(0, 0, v.width * 3, v.height * 3);
        v.popMatrix();

        // 1:48 - 2:30 - Instrumental
        superStars.render(elapsed);

        //
        v.translateCenter();

        v.rotateX(Visual.sin(-elapsed * BEATMS * Visual.HALF_PI * 0.1f) * Visual.HALF_PI / 2 - Visual.HALF_PI / 2);
        rotation.y = Visual.sin(elapsed * BEATMS * Visual.TWO_PI) * 0.3f;
        rotation.y = elapsed * BEATMS * 0.3f;
        v.ambientLight(300, 100, 100);
        v.pointLight(0, 100, 100, 100, -v.height, 1000);
        applyTransforms();
        // circle.render();
        // v.lights();
        v.blendMode(PApplet.BLEND);
        squigglyArcs.render(elapsed);
        horse.render(elapsed);
        v.popMatrix();
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
            v.circle(0, 0, v.audioAnalysis().mix().amplitude * 1000);
            v.popMatrix();
        }
    }

    class HappyHorse extends VObject {
        PShape horse;

        HappyHorse(Visual v, PVector pos) {
            super(v, pos);
            horse = v.loadShape("horse.obj");
        }

        @Override
        public void render(int elapsed) {
            applyTransforms();
            v.scale(40);

            // v.rotateZ(Visual.sin(elapsed / 1000f * Visual.TWO_PI) + Visual.PI);
            v.rotateZ(Visual.sin(elapsed * BEATMS * Visual.HALF_PI) / 2 + Visual.PI);
            v.translate(0, Visual.sin(elapsed * BEATMS * Visual.TWO_PI) * 2 + 2);
            // v.translate(0,200);
            v.shape(horse, 0, 2);
            v.popMatrix();
        }
    }

    /**
     * SquigglyArcs
     */
    public class SquigglyArcs extends VObject {
        private float halfH = v.height / 2;
        private float halfW = v.width / 2;
        private AudioAnalysis aa;
        private float PI;
        private float TWO_PI;

        public SquigglyArcs(Visual v, PVector position) {
            super(v, position);
            aa = v.audioAnalysis();
            PI = PApplet.PI;
            TWO_PI = PApplet.TWO_PI;
        }

        @Override
        public void render(int elapsed) {
            v.colorMode(PApplet.HSB, 360, 100, 100, 100);
            float lerpedAmplitude = aa.mix().lerpedAmplitude;
            float[] lerpedSpectrum = aa.mix().lerpedSpectrum;
            // 1000 / 60
            // float off = 16.6666666667f / elapsed;
            float off = 1 / elapsed;

            v.pushMatrix();
            v.rectMode(PApplet.CENTER);
            // v.translate(0, 100, -100);
            // rect(0,0, width, height);
            // v.square(halfW, halfH, v.height * 2);
            // v.rotateX(PApplet.PI );
            v.translate(0, 200, 0);
            v.rotateX(PApplet.HALF_PI);
            v.scale(2);
            v.noFill();
            float sinMap = PApplet.map(PApplet.sin(off * 1.00001f / (60 * 3)), -1, 1, 2.090f, 2.110f);
            // How many arcs to render
            int count = (int) PApplet.map(lerpedAmplitude * 2, 0, 0.2f, 0, lerpedSpectrum.length - 1);
            count += PApplet.constrain((int) PApplet.map(lerpedAmplitude, 0.01f, 0, 0, lerpedSpectrum.length - 1), 0,
                    lerpedSpectrum.length);
            v.blendMode(PApplet.BLEND);
            // count = ab.size();
            for (int i = 0; i < PApplet.constrain(count, 0, lerpedSpectrum.length) - 1; i++) {
                float hue = 350 - PApplet.map(i, 0, lerpedSpectrum.length, 0, 360);

                float sat = 100;
                float val = 100;
                float alpha = PApplet.map(i, 0, lerpedSpectrum.length, 100, 50) + (lerpedAmplitude * 2 * 100);
                v.stroke(hue, sat, val, alpha);

                float f = lerpedSpectrum[i] * PI; //
                f += 0.1f;

                float r = PApplet.sin(i * sinMap) / 1 * PI / 2; // Sine wave rotation
                r += PApplet.sin(off / (120)) / 1 * PI / 2; // Sine wave rotation
                r += off / (60); // 1 rotation per second

                // Two arcs
                v.arc(0, 0, i, i, r - f, r + f);
                r += PApplet.PI;
                v.arc(0, 0, i, i, r - f, r + f);

            }
            v.popMatrix();
        }

    }

    /**
     * Constillation of superellipses
     */
    private class SuperStars extends VObject {

        public SuperStars(Visual v, PVector position) {
            super(v, position);
        }

        @Override
        public void render(int elapsed) {
            v.blendMode(PApplet.SUBTRACT);
            // Super elipse
            v.pushMatrix();
            float lerpedAmplitude = v.audioAnalysis().mix().lerpedAmplitude * 10;
            v.noFill();
            float hue = PApplet.map(v.audioAnalysis().mix().amplitude * 10, 0, 1, 0, 360);
            v.stroke(hue, 100, 100);
            v.translate((-elapsed / 10) % 200, 0);
            for (int x = -200; x < v.width + 200; x += 200) {
                for (int y = -200; y < v.height + 200; y += 200) {
                    superellipse(x, y, 0.5f * lerpedAmplitude, 1.0f * lerpedAmplitude, 100 * lerpedAmplitude,
                            100 * lerpedAmplitude);
                }
            }
            v.popMatrix();
        }

        /**
         * Superellipse
         *
         * @param x
         * @param y
         * @param a
         * @param b
         * @param n
         * @param x
         * @return
         */
        public void superellipse(float posX, float posY, float m, float n, float a, float b) {
            v.beginShape();
            for (int i = 0; i < 360; i++) {
                float t = i * Visual.TWO_PI / 360;
                float x = PApplet.pow(PApplet.abs(PApplet.cos(t)), 2 / m) * a * Math.signum(PApplet.cos(t));
                float y = PApplet.pow(PApplet.abs(PApplet.sin(t)), 2 / n) * b * Math.signum(PApplet.sin(t));
                v.vertex(posX + x, posY + y);
            }
            v.endShape();
        }

    }
}
