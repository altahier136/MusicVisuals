package ie.tudublin.visual;

// Graphics
import processing.core.PApplet;

// Audio
import ddf.minim.*;
import ddf.minim.analysis.*;

/**
 * Visual is an abstract class that will be used to create the Music Visualiser.<br>
 * <br>
 * Visual extends {@link PApplet} and encapsulates {@link Minim},
 * {@link AudioInput}, {@link AudioPlayer}, {@link FFT}, {@link BeatDetect} and {@link AudioAnalysis}.
 *
 * @see <a href="https://processing.org/reference/">Processing Reference</a>
 * @see <a href="https://code.compartmental.net/minim/index.html">Minim Reference</a>
 */
public abstract class Visual extends PApplet {

    private int bufferSize;
    private int sampleRate;
    private String[][] lyrics; // [[00:00][lyrics], [00:00][lyrics], ...

    private Minim minim;
    private AudioInput aIn;
    private AudioPlayer aPlayer;
    private AudioAnalysis aAnalysis;
    private FFT fft;
    private BeatDetect beat;

    /** @return {@link #bufferSize} */
    public int bufferSize() {
        return bufferSize;
    }

    /** @return {@link #sampleRate} */
    public int sampleRate() {
        return sampleRate;
    }

    /** @return {@link #minim} */
    public Minim minim() {
        return minim;
    }

    /** @return {@link #aPlayer} */
    public AudioInput audioInput() {
        return aIn;
    }

    /**
     * @return {@link #aPlayer}
     */
    public AudioPlayer audioPlayer() {
        return aPlayer;
    }

    /** @return {@link #fft} */
    public FFT fft() {
        return fft;
    }

    /** @return {@link #beat} */
    public BeatDetect beat() {
        return beat;
    }

    /** @return {@link #aAnalysis} */
    public AudioAnalysis audioAnalysis() {
        return aAnalysis;
    }

    public Visual() {
        this(1024, 44100, 0.1f);
    }

    /**
     * @param bufferSize    Must be a power of 2
     * @param sampleRate    The sample rate of the audio input
     * @param lerpAmount    The lerp amount for the {@link AudioAnalysis}
     */
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

        // Could potentially encapsulate all of the above into AudioAnalysis class
        // Not going to do it as it's not necessary, it works fine as is
        this.aAnalysis = new AudioAnalysis(fft, beat, lerpAmount);

    }

    abstract public void settings();

    abstract public void setup();

    abstract public void draw();

    // ======== Audio ========

    public void setAudioLerpAmount(float audioLerpAmount) {
        aAnalysis.setLerpAmount(audioLerpAmount);
    }

    /** Begins audio input from the default audio input device. */
    public void beginAudio() {

        if (aPlayer != null)
            aPlayer.close();
        aIn = minim.getLineIn(Minim.MONO, bufferSize, 44100, 16);
        aIn.addListener(aAnalysis);

        System.out.println("Using default audio input");

    }

    /**
     * Begins audio input from the specified audio file.
     * @param filename
     */
    public void beginAudio(String filename) {

        if (aPlayer != null)
            aPlayer.close();
        if (filename == null || filename.isEmpty()) {
            System.out.println("No filename specified, using default audio input");
            beginAudio();
        }
        aPlayer = minim.loadFile(filename, bufferSize);
        aPlayer.addListener(aAnalysis);
        aPlayer.play();

        System.out.println("Playing " + filename);

    }

    public void beginAudio(String audioFilename, String lyricsFilename) {
        beginAudio(audioFilename);
        loadLyrics(lyricsFilename);
    }

    public void seek(int ms) {
        aPlayer.cue(ms);
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

        if (aPlayer.isPlaying()) {
            aPlayer.pause();
            System.out.println("Paused");
        } else {
            aPlayer.play();
            System.out.println("Playing");
        }

    }

    public void pause() {
        aPlayer.pause();
        System.out.println("Paused");
    }

    public void play() {
        aPlayer.play();
        System.out.println("Playing");
    }

    public void mute() {
        aPlayer.mute();
        System.out.println("Muted");
    }

    // ======== Lyrics ========

    /**
     * Loads lyrics from file into 2D array of [time, string]<br><br>
     * File format:<br><br>
     * <pre>
     * 00:00|String
      *00:00|String
     * ...
     * </pre>
     * @param fileName
     */
    public void loadLyrics(String fileName) {
        String[] rawLyrics = loadStrings(fileName);
        // Convert to 2D array
        lyrics = new String[rawLyrics.length][2];
        for (int i = 0; i < rawLyrics.length; i++) {
            String[] split = rawLyrics[i].split("\\|"); // 00:00|String -> [00:00, String]
            // Copy strings to 2D array
            lyrics[i][0] = split[0];
            lyrics[i][1] = split[1];
        }
    }

    /**
     * Gets lyrics at current time
     * @param offset    Offset by line
     * @return          Lyrics at current time
     */
    public String getLyrics(int offset) {
        if (lyrics == null) {
            System.out.println("No lyrics loaded");
            return "No lyrics loaded";
        }

        String result = "...";
        for (int i = 0; i < lyrics.length - 1; i++) {
            int current = timestampToMs(lyrics[i][0]);
            int next = timestampToMs(lyrics[i + 1][0]);
            if (aPlayer.position() >= current && aPlayer.position() < next) {
                result = lyrics[i + offset][1];
                break;
            }
        }
        return result;
    }

    // ======== Helpers ========

    /**
     * Converts timestamp to milliseconds: 01:05 -> 65000
     * @param timestamp
     * @return
     */
    public int timestampToMs(String timestamp) {
        String[] split = timestamp.split(":");
        int m = Integer.parseInt(split[0]);
        int s = Integer.parseInt(split[1]);
        return toMs(m, s, 0);
    }

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

    // Position helpers

    /**
     * Translates the origin to the center of the screen.
     */
    public void translateCenter() {
        translate(width / 2, height / 2);
    }

    /**
     * Translates the origin to the center of the screen and then to the specified
     * point.
     * @param x
     * @param y
     */
    public void translateCenter(float x, float y) {
        translate(width / 2 + x, height / 2 + y);
    }

}
