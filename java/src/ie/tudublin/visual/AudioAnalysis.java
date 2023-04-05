package ie.tudublin.visual;

import ddf.minim.AudioListener;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import processing.core.PApplet;

/**
 * The AudioAnalysis class is used to analyse the audio samples for each channel
 * and store the results in the relative channel class variables.<br>
 * <br>
 * Fields for AudioAnalysis<br>
 * <ul>
 * <li>{@link #left()} - The left channel of the audio</li>
 * <li>{@link #right()} - The right channel of the audio</li>
 * <li>{@link #mix()} - The mix of the left and right channels</li>
 * </ul>
 * <br>
 * Fields for AAChannel<br>
 * <ul>
 * <li>{@link #amplitude} - The amplitude of the channel</li>
 * <li>{@link #lerpedAmplitude} - The lerped amplitude of the channel</li>
 * <li>{@link #waveform} - The waveform of the channel</li>
 * <li>{@link #lerpedWaveform} - The lerped waveform of the channel</li>
 * <li>{@link #bands} - The bands of the channel</li>
 * <li>{@link #lerpedBands} - The lerped bands of the channel</li>
 * <li>{@link #spectrum} - The spectrum of the channel</li>
 * <li>{@link #lerpedSpectrum} - The lerped spectrum of the channel</li>
 * </ul>
 *
 * @see {@link ddf.minim.AudioListener}
 * @see {@link ddf.minim.AudioInput}
 * @see {@link ddf.minim.AudioPlayer}
 * @see {@link ddf.minim.analysis.BeatDetect}
 * @see {@link ddf.minim.analysis.FFT}
 */
public class AudioAnalysis implements AudioListener {
    private FFT fft;
    private BeatDetect beat;
    private AAChannel left;
    private AAChannel right;
    private AAChannel mix;

    public AudioAnalysis(FFT fft, BeatDetect beat, float lerpAmount) {
        this.fft = fft;
        this.beat = beat;
        this.left = new AAChannel(lerpAmount);
        this.right = new AAChannel(lerpAmount);
        this.mix = new AAChannel(lerpAmount);
    }

    @Override
    /** Called by Recordable class (AudioPlayer or AudioInput) */
    public synchronized void samples(float[] samp) {
        left.samp = samp;
        right.samp = samp;
        mix.samp = samp;
        beat.detect(samp);
        left.update(samp.length);
        right.update(samp.length);
        mix.update(samp.length);
    }

    @Override
    /** Called by Recordable class (AudioPlayer or AudioInput) */
    public synchronized void samples(float[] sampL, float[] sampR) {
        left.samp = sampL;
        right.samp = sampR;

        // Mix the samples
        mix.samp = new float[sampL.length];
        for (int i = 0; i < sampL.length; i++) {
            mix.samp[i] = (sampL[i] + sampR[i]) / 2;
        }

        beat.detect(sampL);
        left.update(sampL.length);
        right.update(sampR.length);
        mix.update(sampL.length);
    }

    public AAChannel mix() {
        return mix;
    }

    public AAChannel left() {
        return left;
    }

    public AAChannel right() {
        return right;
    }

    public void setLerpAmount(float lerpAmount) {
        left.setLerpAmount(lerpAmount);
        right.setLerpAmount(lerpAmount);
        mix.setLerpAmount(lerpAmount);
    }


    /**
     * This class is used to
     */
    public class AAChannel {
        private float[] samp;
        public float amplitude;
        public float lerpedAmplitude;
        public float[] waveform;
        public float[] lerpedWaveform;
        public float[] bands;
        public float[] lerpedBands;
        public float[] spectrum;
        public float[] lerpedSpectrum;
        public float lerpAmount;

        public AAChannel(float lerpAmount) {
            this.lerpAmount = lerpAmount;
            bands = new float[(int) Visual.log2(fft.specSize())];
            lerpedBands = new float[(int) Visual.log2(fft.specSize())];
            spectrum = new float[fft.specSize()];
            lerpedSpectrum = new float[fft.specSize()];
        }

        public void setLerpAmount(float lerpAmount) {
            this.lerpAmount = lerpAmount;
        }

        public void update(int samp) {
            amplitude();
            waveform();
            bands();
            spectrum();
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

        // Beat is not called here because we want dont want
        // to call beat.detect() multiple times for each channel
        // it will cause the beat detection to be inaccurate

        public boolean isKick() {
            return beat.isKick();
        }

        public boolean isSnare() {
            return beat.isSnare();
        }

        public boolean isHat() {
            return beat.isHat();
        }
    }
}