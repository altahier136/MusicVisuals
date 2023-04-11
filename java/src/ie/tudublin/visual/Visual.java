package ie.tudublin.visual;

import processing.core.PApplet;

import ddf.minim.*;
import ddf.minim.analysis.*;

/**
 * <p>
 * Visual is the main class which will be used to create the Music Visualiser
 * The {@link Visual} class is an abstract class that will be used to create
 * the Music Visualiser.
 * </p>
 *
 * <p>
 * Fields, Getters and setters:
 * <ul>
 * <li>{@link #bufferSize()}</li>
 * <li>{@link #sampleRate()}</li>
 * <li>{@link #minim()}</li>
 * <li>{@link #audioInput()}</li>
 * <li>{@link #audioPlayer()}</li>
 * <li>{@link #fft()}</li>
 * <li>{@link #beat()}</li>
 * <li>{@link #audioAnalysis()}</li>
 * <li>{@link #analysisLeft()}</li>
 * <li>{@link #analysisRight()}</li>
 * </ul>
 */
public abstract class Visual extends PApplet implements VConstants {

    private int bufferSize;
    private int sampleRate;

    private Minim minim;
    private AudioInput ai;
    private AudioPlayer ap;
    private AudioAnalysis aa;
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
     * Gets the {@link Minim} object.
     *
     * @return {@link #minim}
     */
    public Minim minim() {
        return minim;
    }

    /**
     * Gets the {@link AudioPlayer} object.
     *
     * @return {@link #ap}
     */
    public AudioInput audioInput() {
        return ai;
    }

    /**
     * Gets the {@link AudioPlayer} object.
     *
     * @return {@link #ap}
     */
    public AudioPlayer audioPlayer() {
        return ap;
    }

    /**
     * Gets the {@link FFT} object.
     *
     * @return {@link #fft}
     */
    public FFT fft() {
        return fft;
    }

    /**
     * Gets the {@link BeatDetect} object.
     *
     * @return {@link #beat}
     */
    public BeatDetect beat() {
        return beat;
    }

    /**
     * Gets mixed {@link AudioAnalysis} object.
     *
     * @return {@link #aa}
     */
    public AudioAnalysis audioAnalysis() {
        return aa;
    }

    /**
     * Gets left {@link AudioAnalysis} object.
     *
     * @return {@link #analysisLeft}
     */

    public Visual() {
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
        fft.logAverages(60, 3);
        // Making an annonymous inner class to override the default BeatDetect
        // to use our own thresholds
        beat = new BeatDetect(bufferSize, sampleRate) {
            // We can assume that BeatDetect is in FREQ_ENERGY mode
            @Override
            public boolean isHat() {
                int lower = super.detectSize() - 7 < 0 ? 0 : super.detectSize() - 7;
                int upper = super.detectSize() - 1;
                return isRange(lower, upper, 1);
            }

            @Override
            public boolean isKick() {
                int upper = 6 >= super.detectSize() ? super.detectSize() : 6;
                return isRange(1, upper, 2);
            }

            @Override
            public boolean isSnare() {
                int lower = 8 >= super.detectSize() ? super.detectSize() : 8;
                int upper = super.detectSize() - 1;
                int thresh = (upper - lower) / 3 + 1;
                return isRange(lower, upper, thresh);
            }
        };
        beat.setSensitivity(50);

        this.aa = new AudioAnalysis(fft, beat, lerpAmount);

    }

    abstract public void settings();

    abstract public void setup();

    abstract public void draw();

    // ======== Audio ========
    public void setLerpAmount(float lerpAmount) {
        aa.setLerpAmount(lerpAmount);
    }

    /** Begins audio input from the default audio input device. */
    public void beginAudio() {

        if (ap != null)
            ap.close();
        ai = minim.getLineIn(Minim.MONO, bufferSize, 44100, 16);
        ai.addListener(aa);

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
        ap.addListener(aa);
        ap.play();

        System.out.println("Playing " + filename);

    }

    public void seek(int ms) {
        ap.cue(ms);
        System.out.println("Seeking to " + ms + " ms");
    }

    public void seek(int m, int s) {
        int ms = toMs(m, s, 0);
        seek(ms);
    }

    public void seek(int m, int s, int ms) {
        int msNew = toMs(m, s, ms);
        seek(msNew);
    }

    public void pausePlay() {

        if (ap.isPlaying()) {
            ap.pause();
            System.out.println("Paused");
        } else {
            ap.play();
            System.out.println("Playing");
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

    // Matrix helpers

    /**
     * Translates the origin to the center of the screen.
     */
    public void translateCenter() {
        translate(width / 2, height / 2);
    }

    /**
     * Translates the origin to the center of the screen and then to the specified point.
     * @param x
     * @param y
     */
    public void translateCenter(float x, float y) {
        translate(width / 2 + x, height / 2 + y);
    }


}
