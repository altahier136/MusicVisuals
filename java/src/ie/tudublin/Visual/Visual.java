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

    private float[] bands, lerpedBands;
    private float amplitude, lerpedAmplitude;

    private Minim minim;
    private AudioInput ai;
    private AudioPlayer ap;
    private AudioBuffer abMix;
    private AudioBuffer abLeft;
    private AudioBuffer abRight;
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
     * Gets the bands.
     *
     * @return {@link #bands}
     */
    public float[] bands() {
        return bands;
    }

    /**
     * Gets the smoothed bands.
     *
     * @return {@link #lerpedBands}
     */
    public float[] lerpedBands() {
        return lerpedBands;
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

    /**
     * Gets the AudioPlayer object.
     *
     * @param channel 0 for {@link #abMix}, 1 for {@link #abLeft}, 2 for
     *                {@link #abRight}
     * @return abMono
     */
    public AudioBuffer audioBuffer(ChannelEnum channel) {
        switch (channel) {
            case MIX:
                return abMix;
            case LEFT:
                return abLeft;
            case RIGHT:
                return abRight;
            default:
                throw new IllegalArgumentException("Invalid channel" + channel);
        }
    }

    /** Gets the amplitude. */
    public float amplitude() {
        return amplitude;
    }

    /**
     * Gets the amplitude.
     *
     * @param mode 0 for {@link #amplitude} and 1 for {@link #lerpedAmplitude}
     */
    public float lerpedAmplitude() {
        return lerpedAmplitude;
    }

    /** Gets the AudioPlayer object. */
    public AudioPlayer audioPlayer() {
        return ap;
    }

    /** Gets the FFT object. */
    public FFT fft() {
        return fft;
    }

    public Visual() throws VisualException {
        // Default buffer size and sample rate
        this(1024, 44100);
    }

    public Visual(int bufferSize, int sampleRate) {
        if (log2(bufferSize) % 1 != 0)
            throw new IllegalArgumentException("Buffer size must be a power of 2");

        this.bufferSize = bufferSize;
        this.sampleRate = sampleRate;

        // Audio analysis
        minim = new Minim(this);
        fft = new FFT(bufferSize, sampleRate);
        beat = new BeatDetect(bufferSize, sampleRate);
        beat.setSensitivity(100);

        bands = new float[(int) log2(bufferSize)];
        lerpedBands = new float[bands.length];

        amplitude = 0;
        lerpedAmplitude = 0;
    }

    abstract public void settings();

    abstract public void setup();

    abstract public void draw();

    // ======== Audio Analysis ========

    /**
     * Begins audio input from the default audio input device.
     *
     * @throws VisualException
     */
    public void beginAudio() {
        if (ap != null)
            ap.close();
        ai = minim.getLineIn(Minim.MONO, bufferSize, 44100, 16);
        abLeft = ai.left;
        abRight = ai.right;
        abMix = ai.mix;
    }

    /**
     * Begins audio input from the specified audio file.
     *
     * @param filename
     * @throws VisualException
     */
    public void beginAudio(String filename) {
        if (ap != null)
            ap.close();
        if (filename == null || filename.isEmpty()) {
            System.out.println("No filename specified, using default audio input");
            beginAudio();
        }
        ap = minim.loadFile(filename, bufferSize);
        abLeft = ap.left;
        abRight = ap.right;
        abMix = ap.mix;
        ap.play();
        System.out.println("Playing " + filename);
    }

    public void seek(int millis) {
        ap.cue(millis);
    }

    // ======== Waveform Analysis ========

    /**
     * Calculates the average amplitude of the audio buffer
     *
     * @param amt The amount to lerp the amplitude by (0.0 - 1.0) recommended 0.1
     */
    public void lerpAmplitude(float amt) {
        // Get the average amplitude of the audio buffer
        float sum = 0;
        for (float sample : abMix.toArray()) {
            sum += abs(sample);
        }
        amplitude = sum / abMix.size();

        // Lerp the amplitude
        // lerpedAmplitude = PApplet.lerp(lerpedAmplitude, amplitude, amt);
        lerpedAmplitude = lerp(lerpedAmplitude, amplitude, amt, 1 / frameRate);
    }

    // ======== Fast Fourier Analysis ========

    /** Calculates the FFT of the audio buffer */
    protected void calculateFFT() throws VisualException {
        fft.window(FFT.HAMMING);
        if (abMix != null) {
            fft.forward(abMix);
        } else {
            throw new VisualException("You must call startListening or loadAudio before calling fft");
        }
    }

    /**
     * Calculates the frequency bands of the audio buffer
     *
     * @param amt The amount to lerp the bands by (0.0 - 1.0) recommended 0.05
     */
    protected void calculateFrequencyBands(float amt) {
        for (int i = 0; i < bands.length; i++) {
            int start = (int) pow(2, i) - 1; // start at 0
            int w = (int) pow(2, i); // width of each band
            int end = start + w; // end at the last index
            end = min(end, fft.specSize() - 1); // make sure it doesn't go out of bounds

            float average = 0;
            // For each band, add the value of each bin

            for (int j = start; j < end; j++) {
                average += fft.getBand(j) * (j + 1);
            }
            average /= (float) w;
            bands[i] = average * 5.0f;
            lerpedBands[i] = lerp(lerpedBands[i], bands[i], 0.05f);
        }
    }

    // ======== Beat Detection ========

    // ======== Helpers ========

    /** Log_2(x) = log_e(x) / log_e(2) */
    float log2(float f) {
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
