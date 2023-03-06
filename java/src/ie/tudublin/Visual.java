package ie.tudublin;

import processing.core.PApplet;
import ddf.minim.*;
import ddf.minim.analysis.*;

/**
 * Visual is the main class which will be used to create the Music Visualiser
 *
 */
public abstract class Visual extends PApplet
{
	private int frameSize = 512;
	private int sampleRate = 44100;

	private float[] bands;
	private float[] smoothedBands;

	private Minim minim;
	private AudioInput ai;
	private AudioPlayer ap;
	private AudioBuffer abMono;
	private AudioBuffer abLeft;
	private AudioBuffer abRight;
	private FFT fft;

	private float amplitude  = 0;
	private float smothedAmplitude = 0;

	/**
	 * Sets minim, fft, bands and smoothedBands
	 */
	public void startMinim()
	{
		minim = new Minim(this);

		fft = new FFT(frameSize, sampleRate);

		bands = new float[(int) log2(frameSize)];
  		smoothedBands = new float[bands.length];
	}

	/**
	 * Log_2(x) = log_e(x) / log_e(2)
	 */
	float log2(float f) {
		return log(f) / log(2.0f);
	}

	/**
	 * Calculates the FFT of the audio buffer
	 */
	protected void calculateFFT() throws VisualException
	{
		fft.window(FFT.HAMMING);
		if (abMono != null)
		{
			fft.forward(abMono);
		}
		else
		{
			throw new VisualException("You must call startListening or loadAudio before calling fft");
		}
	}

	/**
	 * Calculates the average amplitude of the audio buffer
	 */
	public void calculateAverageAmplitude()
	{
		float total = 0;
		for(int i = 0 ; i < abMono.size() ; i ++)
        {
			total += abs(abMono.get(i));
		}
		amplitude = total / abMono.size();
		smothedAmplitude = PApplet.lerp(smothedAmplitude, amplitude, 0.1f);
	}

	/**
	 * Calculates the frequency bands of the audio buffer
	 */
	protected void calculateFrequencyBands() {
		for (int i = 0; i < bands.length; i++) {
			int start = (int) pow(2, i) - 1;
			int w = (int) pow(2, i);
			int end = start + w;
			float average = 0;
			for (int j = start; j < end; j++) {
				average += fft.getBand(j) * (j + 1);
			}
			average /= (float) w;
			bands[i] = average * 5.0f;
			smoothedBands[i] = lerp(smoothedBands[i], bands[i], 0.05f);
		}
	}

	/**
	 * Starts listening to mic input
	 */
	public void startListening()
	{
		ai = minim.getLineIn(Minim.MONO, frameSize, 44100, 16);
		abLeft = ai.left;
		abRight = ai.right;
		abMono = ai.mix;
	}

	/**
	 * Loads an audio file
	 * @param filename
	 */
	public void loadAudio(String filename)
	{
		ap = minim.loadFile(filename, frameSize);
		abLeft = ap.left;
		abRight = ap.right;
		abMono = ap.mix;
	}

	/**
	 * Gets the frame size.
	 * @return frameSize
	 */
	public int getFrameSize() {
		return frameSize;
	}

	/**
	 * Sets the frame size.
	 * @param frameSize
	 */
	public void setFrameSize(int frameSize) {
		this.frameSize = frameSize;
	}

	/**
	 * Gets the sample rate.
	 * @return sampleRate
	 */
	public int getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate.
	 * @param sampleRate
	 */
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Gets the bands.
	 * @return bands
	 */
	public float[] getBands() {
		return bands;
	}

	/**
	 * Gets the smoothed bands.
	 * @return smoothedBands
	 */
	public float[] getSmoothedBands() {
		return smoothedBands;
	}

	/**
	 * Gets the {@link Minim} object.
	 * @return minim
	 */
	public Minim getMinim() {
		return minim;
	}

	/**
	 * Gets the {@link AudioInput} object.
	 * @return ai
	 */
	public AudioInput getAudioInput() {
		return ai;
	}

	/**
	 * Gets the {@link AudioBuffer} object.
	 * @return ab
	 */
	public AudioBuffer getAudioBuffer() {
		return abMono;
	}

	/**
	 * Gets the amplitude.
	 * @return amplitude
	 *
	 */
	public float getAmplitude() {
		return amplitude;
	}

	/**
	 * Gets the smoothed amplitude.
	 * @return smothedAmplitude
	 */
	public float getSmoothedAmplitude() {
		return smothedAmplitude;
	}

	/**
	 * Gets the {@link AudioPlayer} object.
	 * @return ap
	 */
	public AudioPlayer getAudioPlayer() {
		return ap;
	}

	/**
	 * Gets the {@link FFT} object.
	 * @return fft
	 */
	public FFT getFFT() {
		return fft;
	}
}
