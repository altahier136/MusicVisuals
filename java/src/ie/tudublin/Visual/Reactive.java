package ie.tudublin.Visual;

import ddf.minim.AudioBuffer;
import ie.tudublin.Visual.VisualConstants.ChannelEnum;
import processing.core.PVector;

/**
 * AudioReactive is an abstract class which will be used to create
 * objects within the Music Visualiser which are audio reactive
 * ??? Is reactive even needed, other devs will just use AudioAnalysis along with Object
 *
 * @version 1.0
 * @since 06-03-2023
 * @author AdrianCapacite
 */
public abstract class Reactive extends Object {
    AudioBuffer waveform;
    float amplitude;
    float[] bands;
    ChannelEnum channel;
    boolean lerped;

    protected Reactive(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0), ChannelEnum.MIX, true);
        this.v = v;
    }
    Reactive(Visual v, PVector pos) {
        this(v, pos, new PVector(0,0,0), ChannelEnum.MIX, true);
    }
    Reactive(Visual v, PVector pos, PVector vel) {
        this(v, pos, vel, ChannelEnum.MIX, true);
    }
    protected Reactive(Visual v, PVector pos, PVector vel, ChannelEnum channel, boolean lerped) {
        super(v, pos, vel);
        this.channel = channel;
        this. lerped = lerped;
    }

    public void update() {
        // waveform = v.audioBuffer(channel);
        // if (lerped) {
        //     bands = v.lerpedBands();
        //     amplitude = v.lerpedAmplitude();
        // } else {
        //     bands = v.bands();
        //     amplitude = v.amplitude();
        // }
    }

    public abstract void render();

}
