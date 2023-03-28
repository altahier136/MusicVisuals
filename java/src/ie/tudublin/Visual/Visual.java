package ie.tudublin.Visual;

import processing.core.PApplet;

import ddf.minim.*;
import ddf.minim.analysis.*;

/* The type of the super class can be used for storing the sub class
 * E.g. SuperClass sub = new SubClass();
 * However you cannot access the sub class methods and variables
*/

/*
 * Visual class diagram
 * processing.core.PApplet
 *   Visual <- VisualConstants
 *     - ddf.minim.*
 *
 * Object
 *   - Visual
 *   Scene
 *   Reactive
 */

/**
 * <p>
 * Visual is the main class which will be used to create the Music Visualiser
 * The {@link Visual} class is an abstract class that will be used to create
 * the Music Visualiser.
 * </p>
 *
 * <p>
 * Getters and setters:
 * <ul>
 * <li>{@link #getFrameSize}</li>
 * <li>{@link #setFrameSize}</li>
 * <li>{@link #getSampleRate}</li>
 * <li>{@link #setSampleRate}</li>
 * <li>{@link #getBands}</li>
 * <li>{@link #getSmoothedBands}</li>
 * <li>{@link #getMinim}</li>
 * <li>{@link #getAudioInput}</li>
 * <li>{@link #getAudioPlayer}</li>
 * <li>{@link #getAudioBuffer}</li>
 *
 */
public abstract class Visual extends PApplet implements VisualConstants {

    private int bufferSize;
    private int sampleRate;

    private Minim minim;
    private AudioInput ai;
    private AudioPlayer ap;
    private AudioAnalysis analysisMix;
    private AudioAnalysis analysisLeft;
    private AudioAnalysis analysisRight;
    private FFT fft;
    private BeatDetect beat;

    /**
     * Gets the frame size.
     *
     * @return {@link #bufferSize}
     */
    public int bufferSize() {
        return bufferSize;
    }

    /**
     * Gets the sample rate.
     *
     * @return {@link #sampleRate}
     */
    public int sampleRate() {
        return sampleRate;
    }

    /**
     * Gets the Minim object.
     *
     * @return {@link #minim}
     */
    public Minim minim() {
        return minim;
    }

    /**
     * Gets the AudioPlayer object.
     *
     * @return {@link #ap}
     */
    public AudioInput audioInput() {
        return ai;
    }

    /** Gets the AudioPlayer object. */
    public AudioPlayer audioPlayer() {
        return ap;
    }

    /** Gets the FFT object. */
    public FFT fft() {
        return fft;
    }

    /** Gets the BeatDetect object. */
    public BeatDetect beat() {
        return beat;
    }

    /**
     * Gets the AudioAnalysis object for the mix.
     * @return
     */
    public AudioAnalysis analysisMix() {
        return analysisMix;
    }
    /**
     * Gets the AudioAnalysis object for the left channel.
     * @return
     */
    public AudioAnalysis analysisLeft() {
        return analysisLeft;
    }
    /**
     * Gets the AudioAnalysis object for the right channel.
     * @return
     */
    public AudioAnalysis analysisRight() {
        return analysisRight;
    }

    public Visual() {

        // Default buffer size and sample rate
        this(1024, 44100, 0.1f);

    }

    public Visual(int bufferSize, int sampleRate, float lerpAmount) {

        if (log2(bufferSize) % 1 != 0)
            throw new IllegalArgumentException("Buffer size must be a power of 2");

        this.bufferSize = bufferSize;
        this.sampleRate = sampleRate;

        // Audio analysis
        minim = new Minim(this);

        fft = new FFT(bufferSize, sampleRate);
        beat = new BeatDetect(bufferSize, sampleRate);
        beat.setSensitivity(50);

        analysisMix = new AudioAnalysis(fft, beat, ChannelEnum.MIX, lerpAmount);
        analysisLeft = new AudioAnalysis(fft, beat, ChannelEnum.LEFT, lerpAmount);
        analysisRight = new AudioAnalysis(fft, beat, ChannelEnum.RIGHT, lerpAmount);

    }

    abstract public void settings();

    abstract public void setup();

    abstract public void draw();

    // ======== Audio Analysis ========
    public void setLerpAmount(float lerpAmount) {
        analysisMix.setLerpAmount(lerpAmount);
        analysisLeft.setLerpAmount(lerpAmount);
        analysisRight.setLerpAmount(lerpAmount);
    }

    /** Begins audio input from the default audio input device.  */
    public void beginAudio() {

        if (ap != null)
            ap.close();
        ai = minim.getLineIn(Minim.MONO, bufferSize, 44100, 16);
        ai.addListener(analysisMix);
        ai.addListener(analysisLeft);
        ai.addListener(analysisRight);

        System.out.println("Using default audio input");

    }

    /**
     * Begins audio input from the specified audio file.
     *
     * @param filename
     */
    public void beginAudio(String filename) {

        if (ap != null)
            ap.close();
        if (filename == null || filename.isEmpty()) {
            System.out.println("No filename specified, using default audio input");
            beginAudio();
        }
        ap = minim.loadFile(filename, bufferSize);
        ap.addListener(analysisMix);
        ap.addListener(analysisLeft);
        ap.addListener(analysisRight);
        ap.play();

        System.out.println("Playing " + filename);

    }

    public void seek(int ms) {
        ap.cue(ms);
    }

    public void seek(int m, int s) {
        int ms = toMs(m, s, 0);
        ap.cue(ms);
    }

    public void seek(int m, int s, int ms) {
        int msNew = toMs(m, s, ms);
        ap.cue(msNew);
    }

    public void pausePlay() {

        ap.pause();
        if (ap.isPlaying()) {
            ap.pause();
        } else {
            ap.play();
        }

    }


    // ======== Helpers ========

    /** Converts minutes, seconds, and milliseconds to milliseconds. */
    public int toMs(int m, int s, int ms) {
        return m * 60000 + s * 1000 + ms;
    }

    /** Log_2(x) = log_e(x) / log_e(2) */
    static float log2(float f) {
        return log(f) / log(2.0f);
    }

    /*
     * LERP (Linear Interpolation)
     * Issue with base lerp is it has different behaviors at different frame
     * rates, below is the different methods of lerp for a moving target
     * Method 1: (not frame rate independent)
     * K = constant (such as 0.1)
     * pos = lerp(pos, target, K)
     *
     * Method 2: (frame rate independent but not smooth)
     * K = constant * frameTime
     * pos = lerp(pos, target, K)
     *
     * Method 3: (frame rate independent and smooth)
     * K = constant - (K * frameTime)
     * pos = lerp(pos, target, K)
     *
     * See: https://youtu.be/YJB1QnEmlTs
     */
    /**
     * Implementation of lerp that is frame rate independent and smooth for
     * a moving target
     *
     * @param start
     * @param stop
     * @param amt
     * @param frameTime
     * @return
     */
    public static float lerp(float start, float stop, float amt, float frameTime) {
        float K = amt - (amt * frameTime);
        return start + (stop - start) * K;
    }

    interface EaseFunction {
        float ease(float t);
    }

    EaseFunction easelinear = (t) -> t;
    EaseFunction easeSmooth = (t) -> t * t * (3 - 2 * t);
    EaseFunction easeInQuad = (t) -> t * t;
    EaseFunction easeOutQuad = (t) -> t * (2 - t);
    EaseFunction easeInOutQuad = (t) -> t < 0.5 ? 2 * t * t : 1 - pow(-2 * t + 2, 2) / 2;
    EaseFunction easeOutBounce = (t) -> {
        final float n1 = 7.5625f;
        final float d1 = 2.75f;

        if (t < 1 / d1)
            return n1 * t * t;

        if (t < 2 / d1)
            return n1 * (t -= 1.5f / d1) * t + 0.75f;

        if (t < 2.5 / d1)
            return n1 * (t -= 2.25f / d1) * t + 0.9375f;

        return n1 * (t -= 2.625f / d1) * t + 0.984375f;
    };

    public float interpolate(float start, float stop, float amt, EaseFunction ease) {
        return lerp(start, stop, ease.ease(amt));
    }

    public float interpolate(float start, float stop, float amt, EaseFunction ease, float frameTime) {
        return lerp(start, stop, ease.ease(amt), frameTime);
    }

}
