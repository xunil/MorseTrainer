package com.ak6l.MorseTrainer;

import java.nio.DoubleBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import android.util.Log;

public class WaveformGenerator {
	private double toneFrequency;
	private double wordsPerMinute;
	private double sampleRate;
	private double samplesPerCycle;
	private double samplesPerDit;
	private ShortBuffer ditWaveformBuffer;
	private ShortBuffer dahWaveformBuffer;
	@SuppressWarnings("unused")
	private double[] ditWindow;
	@SuppressWarnings("unused")
	private DoubleBuffer ditWindowBuffer;
	@SuppressWarnings("unused")
	private double[] dahWindow;
	@SuppressWarnings("unused")
	private DoubleBuffer dahWindowBuffer;
	private final double TUKEY_ALPHA = 0.65;
	private HashMap<String, MorseElement> morseLookupTable;

	public WaveformGenerator(double toneFrequency, double wordsPerMinute, double sampleRate) {
		this.toneFrequency = toneFrequency;
		this.wordsPerMinute = wordsPerMinute;
		this.sampleRate = sampleRate;
    	this.samplesPerCycle = sampleRate*(1/toneFrequency);
    	this.samplesPerDit = ((((1200.0/this.getWordsPerMinute()))/((1.0/this.getToneFrequency())*1000.0)) * this.samplesPerCycle);

    	Log.i("WaveformGenerator", "toneFrequency=" + this.toneFrequency
    			+ " wordsPerMinute=" + this.wordsPerMinute
    			+ " sampleRate=" + this.sampleRate
    			+ " samplesPerCycle=" + this.samplesPerCycle
    			+ " samplesPerDit=" + this.samplesPerDit);
    	this.initializeMorseLookupTable();
		this.initializeWaveformBuffers();
	}

	public double getToneFrequency() {
		return toneFrequency;
	}

	public double getWordsPerMinute() {
		return wordsPerMinute;
	}

	public double getSampleRate() {
		return sampleRate;
	}

	private void initializeMorseLookupTable() {
		morseLookupTable = new HashMap<String, MorseElement>(54);
		
		morseLookupTable.put("A", new MorseElement("A", ".-", 4));
		morseLookupTable.put("B", new MorseElement("B", "-...", 6));
		morseLookupTable.put("C", new MorseElement("C", "-.-.", 8));
		morseLookupTable.put("D", new MorseElement("D", "-..", 5));
		morseLookupTable.put("E", new MorseElement("E", ".", 1));
		morseLookupTable.put("F", new MorseElement("F", "..-.", 6));
		morseLookupTable.put("G", new MorseElement("G", "--.", 7));
		morseLookupTable.put("H", new MorseElement("H", "....", 4));
		morseLookupTable.put("I", new MorseElement("I", "..", 2));
		morseLookupTable.put("J", new MorseElement("J", ".---", 10));
		morseLookupTable.put("K", new MorseElement("K", "-.-", 7));
		morseLookupTable.put("L", new MorseElement("L", ".-..", 6));
		morseLookupTable.put("M", new MorseElement("M", "--", 6));
		morseLookupTable.put("N", new MorseElement("N", "-.", 4));
		morseLookupTable.put("O", new MorseElement("O", "---", 9));
		morseLookupTable.put("P", new MorseElement("P", ".--.", 8));
		morseLookupTable.put("Q", new MorseElement("Q", "--.-", 10));
		morseLookupTable.put("R", new MorseElement("R", ".-.", 5));
		morseLookupTable.put("S", new MorseElement("S", "...", 3));
		morseLookupTable.put("T", new MorseElement("T", "-", 3));
		morseLookupTable.put("U", new MorseElement("U", "..-", 5));
		morseLookupTable.put("V", new MorseElement("V", "...-", 6));
		morseLookupTable.put("W", new MorseElement("W", ".--", 7));
		morseLookupTable.put("X", new MorseElement("X", "-..-", 8));
		morseLookupTable.put("Y", new MorseElement("Y", "-.--", 10));
		morseLookupTable.put("Z", new MorseElement("Z", "--..", 8));
		morseLookupTable.put("0", new MorseElement("0", "-----", 15));
		morseLookupTable.put("1", new MorseElement("1", "----.", 13));
		morseLookupTable.put("2", new MorseElement("2", "---..", 11));
		morseLookupTable.put("3", new MorseElement("3", "--...", 9));
		morseLookupTable.put("4", new MorseElement("4", "-....", 7));
		morseLookupTable.put("5", new MorseElement("5", ".....", 5));
		morseLookupTable.put("6", new MorseElement("6", "....-", 7));
		morseLookupTable.put("7", new MorseElement("7", "...--", 9));
		morseLookupTable.put("8", new MorseElement("8", "..---", 11));
		morseLookupTable.put("9", new MorseElement("9", ".----", 13));
		morseLookupTable.put(".", new MorseElement(".", ".-.-.-", 12));
		morseLookupTable.put(",", new MorseElement(",", "--..--", 14));
		morseLookupTable.put("?", new MorseElement("?", "..--..", 10));
		morseLookupTable.put("'", new MorseElement("'", ".----.", 14));
		morseLookupTable.put("!", new MorseElement("!", "-.-.--", 14));
		morseLookupTable.put("/", new MorseElement("/", "-..-.", 9));
		morseLookupTable.put("(", new MorseElement("(", "-.--.", 11));
		morseLookupTable.put(")", new MorseElement(")", "-.--.-", 14));
		morseLookupTable.put("&", new MorseElement("&", ".-...", 7));
		morseLookupTable.put(":", new MorseElement(":", "---...", 12));
		morseLookupTable.put(";", new MorseElement(";", "-.-.-.", 12));
		morseLookupTable.put("=", new MorseElement("=", "-...-", 9));
		morseLookupTable.put("+", new MorseElement("+", ".-.-.", 9));
		morseLookupTable.put("-", new MorseElement("-", "-....-", 10));
		morseLookupTable.put("_", new MorseElement("_", "..--.-", 12));
		morseLookupTable.put("\"", new MorseElement("\"", ".-..-.", 10));
		morseLookupTable.put("$", new MorseElement("$", "...-..-", 11));
		morseLookupTable.put("@", new MorseElement("@", ".--.-.", 12));
		
		// TODO: Add prosigns
	}
	
    @SuppressWarnings("unused")
	private double[] hann_window(int N) {
    	// Returns a copy of the passed-in array of audio samples,
    	// windowed with the Hann function.
    	double window[] = new double[N];
    	
    	for (int n = 0; n < N; n++) {
    		window[n] = ((0.5 * (1 - Math.cos((2.0*Math.PI*n/N-1)))));
    	}
    	
    	return window;
    }

    private double[] tukeyWindow(int N, double alpha) {
    	double window[] = new double[N];
    	int i, n;
    	
    	for (i = 0,n = -(N/2); n < (N/2); i++,n++) {
    		if ((alpha*N)/2 <= Math.abs(n) && Math.abs(n) <= N/2) {
    			window[i] = (0.5 * (0 + Math.cos(Math.PI * ((Math.abs(n) - (alpha * N/2))/((1-alpha)*(N/2))))));
    		} else if (0 <= Math.abs(n) && Math.abs(n) <= ((alpha*N)/2)) {
    			window[i] = 1.0;
    		}
    	}
    	
    	return window;
    }
    
    private void initializeWaveformBuffers() {
    	double totalSamples;
    	DoubleBuffer windowBuffer;
    	double[] windowArray;
    	ShortBuffer waveformBuffer;
    	final int DIT = 0;
    	final int DAH = 1;
    	short sineValue;
    	int i;
    	
    	for (i = DIT; i <= DAH; i++) {
	    	totalSamples = (this.samplesPerDit * (i == DIT ? 1 : 3)) + (Math.ceil(this.samplesPerCycle) * 2);
	    	
	    	
	    	// Generate the array of window coefficients
	    	windowArray = this.tukeyWindow((int)totalSamples, TUKEY_ALPHA);
	    	windowBuffer = DoubleBuffer.wrap(windowArray);
	    	
	    	// Fill the buffer with sine wave samples at the desired frequency,
	    	// and multiply these samples against the window coefficients
	    	waveformBuffer = ShortBuffer.allocate((int) totalSamples);
	    	
	    	for (int n = 0; n < (int)totalSamples; n++) {
	    		sineValue = (short) (
	    				Math.pow(2, 15) *	// Scale to short
	    				(windowBuffer.get(n) * (2 * Math.PI * this.getToneFrequency() * (n/this.getSampleRate())))
	    							// Produce a sine at the desired frequency and sample rate
	    		);
	    		
	    		if ((n % 10) == 0) {
	    			Log.i("WaveformGenerator", "windowBuffer[" + n + "]=" + windowBuffer.get(n) 
	    					+ " waveformBuffer[" + n + "]=" + sineValue);
	    		}

				waveformBuffer.put(sineValue);
	    	}
	    	
	    	
	    	if (i == DIT) {
	    		this.ditWindowBuffer = windowBuffer;
	    		this.ditWaveformBuffer = waveformBuffer;
	    	} else if (i == DAH) {
	    		this.dahWindowBuffer = windowBuffer;
	    		this.dahWaveformBuffer = waveformBuffer;
	    	}
    	}
    }
        
    public short[] audioBufferFromText(String text) {
    	int i;
    	int dits = 0;
    	StringBuilder morseString;
    	MorseElement element;
    	Character c;
    	ShortBuffer audioOutputBuffer;
    	short[] zeros = new short[(int)this.samplesPerDit];
    	
    	// Rough guess that on average, there's 3 dits or dahs for a given letter
    	morseString = new StringBuilder(3 * text.length());
    	
    	for (i = 0; i < text.length(); i++) {
    		switch(text.charAt(i)) {
    		case ' ':
    			morseString.append("   ");	// Interpreted as long delay
    			dits += 7;
    			while (i < text.length() && text.charAt(i) == ' ')
    				i++;
    			break;
    			
    		default:
    			c = new Character(text.charAt(i));
    			element = morseLookupTable.get(c.toString().toUpperCase());
    			morseString.append(element.getCode());
    			morseString.append(" ");
    			dits += 3 + element.getDitCount();
    		}
    	}
    	
    	audioOutputBuffer = ShortBuffer.allocate((int) (this.samplesPerDit * (dits + 5)));
    	
    	for (i = 0; i < morseString.length(); i++) {
    		switch(morseString.charAt(i)) {
    		case ' ':
    			// Java will automatically allocate the new shorts as zeros, which should
    			// come out as silence.
    			audioOutputBuffer.put(zeros, 0, zeros.length);
    			break;
    		case '.':
    			// Dit
    			audioOutputBuffer.put(this.ditWaveformBuffer);
    			audioOutputBuffer.put(zeros, 0, zeros.length);
    			this.ditWaveformBuffer.rewind();
    			break;
    		case '-':
    			// Dah
    			audioOutputBuffer.put(this.dahWaveformBuffer);
    			audioOutputBuffer.put(zeros, 0, zeros.length);
    			this.dahWaveformBuffer.rewind();
    			break;
    		default:
    			// Do something else
    			Log.w("WaveformGenerator", "Unknown character '" + morseString.charAt(i) + "' found in morse string, ignoring");
    		}
    	}

    	return audioOutputBuffer.array();
    }
}