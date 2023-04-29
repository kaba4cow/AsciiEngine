package kaba4cow.ascii.tracker;

import kaba4cow.ascii.audio.Source;
import kaba4cow.ascii.toolbox.maths.Maths;

public class Track {

	private final Composition composition;

	private String name;
	private int defaultSample;
	private float volume;

	private final int[] patternOrder;

	private final Source source;

	public Track(Composition composition, int index) {
		this.composition = composition;
		this.name = String.format("Track %02d", index + 1);
		this.defaultSample = 0;
		this.volume = 1f;
		this.patternOrder = new int[Composition.MAX_PATTERNS];
		for (int i = 0; i < patternOrder.length; i++)
			patternOrder[i] = Composition.INVALID;
		this.source = new Source();
	}

	public void update(int bar, int position) {
		int patternIndex = patternOrder[bar];
		if (patternIndex == Composition.INVALID)
			return;
		Pattern pattern = composition.getPatternList()[patternIndex];
		play(pattern.getNote(position), pattern.getSample(position));
	}

	public void play(int note, int sample) {
		if (note == Composition.INVALID)
			return;
		if (note == Composition.BREAK) {
			stop();
			return;
		}
		if (sample == 0)
			sample = defaultSample;
		else
			sample--;
		Sample s = composition.getSample(sample);
		if (s != null)
			playNote(note).play(s.getBuffer());
	}

	public void play(Pattern pattern, int position) {
		play(pattern.getNote(position), pattern.getSample(position));
	}

	private Source playNote(int note) {
		stop();
		return source.setGain(composition.getVolume() * volume).setPitch(getPitch(note));
	}

	private static float getPitch(int note) {
		float pow = (note - Composition.NOTE_C) / 12f;
		return Maths.pow(2f, pow);
	}

	public void stop() {
		source.stop();
	}

	public int getDefaultSample() {
		return defaultSample;
	}

	public void setDefaultSample(int defaultSample) {
		this.defaultSample = defaultSample;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getPatternOrder() {
		return patternOrder;
	}

	public void setPattern(int bar, Pattern pattern) {
		patternOrder[bar] = pattern.getIndex();
	}

	public void removePattern(int bar) {
		patternOrder[bar] = Composition.INVALID;
	}

	public Pattern getPattern(int bar) {
		if (patternOrder[bar] == Composition.INVALID)
			return null;
		return composition.getPatternList()[patternOrder[bar]];
	}

	public void prevPattern(int bar) {
		if (patternOrder[bar] == Composition.INVALID)
			patternOrder[bar] = Composition.MAX_PATTERNS - 1;
		else if (patternOrder[bar] - 1 >= 0)
			patternOrder[bar]--;
		else
			patternOrder[bar] = Composition.INVALID;
	}

	public void nextPattern(int bar) {
		if (patternOrder[bar] == Composition.INVALID)
			patternOrder[bar] = 0;
		else if (patternOrder[bar] + 1 < Composition.MAX_PATTERNS)
			patternOrder[bar]++;
		else
			patternOrder[bar] = Composition.INVALID;
	}

}
