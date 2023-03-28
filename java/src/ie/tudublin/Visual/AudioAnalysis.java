package ie.tudublin.Visual;

import ddf.minim.AudioListener;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ie.tudublin.Visual.VisualConstants.ChannelEnum;
import processing.core.PApplet;

/**
 * The AudioAnalysis class is used to analyse each new sample and store the
 * results in the class variables to be retrieved.
 */
class AudioAnalysis implements AudioListener {
    private float[] samp;
    private FFT fft;
    private BeatDetect beat;
    public float amplitude;
    public float lerpedAmplitude;
    public float[] bands;
    public float[] lerpedBands;
    public float[] waveform;
    public float[] lerpedWaveform;
    public ChannelEnum channel;
    public float lerpAmount;

    public AudioAnalysis(FFT fft, BeatDetect beat, ChannelEnum channel, float lerpAmount) {
        this.fft = fft;
        this.beat = beat;
        this.lerpAmount = lerpAmount;
        this.channel = channel;
        bands = new float[(int) Visual.log2(fft.specSize())];
        lerpedBands = new float[(int) Visual.log2(fft.specSize())];
    }

    @Override
    public synchronized void samples(float[] samp) {
        this.samp = samp;
        amplitude();
        bands();
        waveform();
        beat();
    }

    @Override
    public synchronized void samples(float[] sampL, float[] sampR) {
        switch (channel) {
            case MIX:
                this.samp = new float[sampL.length];
                for (int i = 0; i < sampL.length; i++) {
                    samp[i] = (sampL[i] + sampR[i]) / 2;
                }
                break;
            case LEFT:
                this.samp = sampL;
                break;
            case RIGHT:
                this.samp = sampR;
                break;
            default:
                throw new IllegalArgumentException("Invalid channel" + channel);
        }

        amplitude();
        bands();
        waveform();
        beat();
    }

    /**
     * Calculates the amplitude of the sample
     * along with the lerped amplitude
     */
    private synchronized void amplitude() {
        amplitude = 0;
        for (int i = 0; i < samp.length; i++) {
            amplitude += Math.abs(samp[i]);
        }
        amplitude /= samp.length;
        // ? Maybe we should take into account the sample time and use the lerp function
        // we made to account for this
        lerpedAmplitude = PApplet.lerp(lerpedAmplitude, amplitude, lerpAmount);
    }

    /**
     * Calculates the bands of the sample
     * along with the lerped bands
     */
    private synchronized void bands() {
        for (int i = 0; i < bands.length; i++) {
            bands[i] = fft.getBand(i);
            lerpedBands[i] = PApplet.lerp(lerpedBands[i], bands[i], lerpAmount);
        }
    }

    /**
     * Calculates the waveform of the sample
     * along with the lerped waveform
     */
    private synchronized void waveform() {
        waveform = samp;
        lerpedWaveform = new float[samp.length];
        for (int i = 0; i < samp.length; i++) {
            lerpedWaveform[i] = PApplet.lerp(lerpedWaveform[i], samp[i], lerpAmount);
        }
    }

    /**
     * Calculates if the sample is a beat
     * BeatDetect must be in FREQ_ENERGY mode
     * You can use the already made functions
     * isKick(), isSnare(), isHat()
     *
     * If you want to use your own thresholds
     * isRange() can be used
     *
     * Example:
     *
     * <pre>
     * <code>
     * if (beat.isKick()) {
     *    v.squareSize = 100;
     * }
     * </code>
     * </pre>
     */
    private synchronized void beat() {
        beat.detect(samp);
    }

}