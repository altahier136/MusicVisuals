package ie.tudublin.Visual;

import ddf.minim.AudioBuffer;
import ie.tudublin.Visual.VisualConstants.ChannelEnum;
import processing.core.PVector;

/**
 * AudioReactive is an abstract class which will be used to create
 * objects within the Music Visualiser which are audio reactive
 *
 * @version 1.0
 * @since 06-03-2023
 * @author AdrianCapacite
 */
public abstract class Reactive extends Object {
    AudioBuffer waveform;
    float[] bands;
    private Visual v;
    ChannelEnum channel;

    Reactive(Visual v) {
        this(v, new PVector(0,0,0), new PVector(0,0,0), ChannelEnum.MIX);
        this.v = v;
    }
    Reactive(Visual v, PVector pos) {
        this(v, pos, new PVector(0,0,0), ChannelEnum.MIX);
    }
    Reactive(Visual v, PVector pos, PVector vel) {
        this(v, pos, vel, ChannelEnum.MIX);
    }
    Reactive(Visual v, PVector pos, PVector vel, ChannelEnum channel) {
        super(v, pos, vel);
        this.channel = channel;
    }

    public void update() {
        waveform = v.audioBuffer(channel);
        bands = v.bands();
    }

    public abstract void render();

}
