package ie.tudublin.aavisual;

import ddf.minim.AudioListener;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import ie.tudublin.aavisual.VConstants.ChannelEnum;
import processing.core.PApplet;

/**
 * The AudioAnalysis class is used to analyse each new sample and store the
 * results in the class variables to be retrieved.<br><br>
 * <br><br>
 * Fields:<br><br>
 * {@value}
 * <ul>
 * <li>{@link #amplitude} - The amplitude of the sample</li>
 * <li>{@link #lerpedAmplitude} - The lerped amplitude of the sample</li>
 * <li>{@link #waveform} - The waveform of the sample</li>
 * <li>{@link #lerpedWaveform} - The lerped waveform of the sample</li>
 * <li>{@link #bands} - The bands of the sample</li>
 * <li>{@link #lerpedBands} - The lerped bands of the sample</li>
 * <li>{@link #spectrum} - The spectrum of the sample</li>
 * <li>{@link #lerpedSpectrum} - The lerped spectrum of the sample</li>
 * <li>{@link #channel} - The channel to use for the sample</li>
 * <li>{@link #lerpAmount} - The amount to lerp the values by</li>
 * </ul>
 * @see {@link ddf.minim.AudioListener}
 * @see {@link ddf.minim.AudioInput}
 * @see {@link ddf.minim.AudioPlayer}
 * @see {@link ddf.minim.analysis.BeatDetect}
 * @see {@link ddf.minim.analysis.FFT}
 */
public class AudioAnalysis implements AudioListener {
    private float[] samp;
    private FFT fft;
    private BeatDetect beat;
    public float amplitude;
    public float lerpedAmplitude;
    public float[] waveform;
    public float[] lerpedWaveform;
    public float[] bands;
    public float[] lerpedBands;
    public float[] spectrum;
    public float[] lerpedSpectrum;
    public ChannelEnum channel;
    public float lerpAmount;

    public AudioAnalysis(FFT fft, BeatDetect beat, ChannelEnum channel, float lerpAmount) {
        this.fft = fft;
        this.beat = beat;
        this.lerpAmount = lerpAmount;
        this.channel = channel;
        bands = new float[(int) Visual.log2(fft.specSize())];
        lerpedBands = new float[(int) Visual.log2(fft.specSize())];
        spectrum = new float[fft.specSize()];
        lerpedSpectrum = new float[fft.specSize()];
    }

    @Override
    /** Called by Recordable class (AudioPlayer or AudioInput) */
    public synchronized void samples(float[] samp) {
        this.samp = samp;
        amplitude();
        waveform();
        bands();
        spectrum();
        beat();
    }

    @Override
    /** Called by Recordable class (AudioPlayer or AudioInput) */
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
        waveform();
        bands();
        spectrum();
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
        fft.forward(samp);
        for (int i = 0; i < bands.length; i++) {
            bands[i] = fft.getBand(i);
            lerpedBands[i] = PApplet.lerp(lerpedBands[i], bands[i], lerpAmount);
        }
    }

    private synchronized void spectrum() {
        fft.forward(samp);
        for (int i = 0; i < spectrum.length; i++) {
            spectrum[i] = fft.getBand(i);
            lerpedSpectrum[i] = PApplet.lerp(lerpedSpectrum[i], spectrum[i], lerpAmount);
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

    public void setLerpAmount(float lerpAmount) {
        this.lerpAmount = lerpAmount;
    }

}