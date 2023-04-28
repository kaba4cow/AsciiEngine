package kaba4cow.ascii.tracker;

import kaba4cow.ascii.audio.Source;
import kaba4cow.ascii.toolbox.maths.Maths;

public class Track {

	private final Composition composition;
	private final int index;

	private String name;
	private int defaultSample;
	private float volume;

	private final Source source;

	public Track(Composition composition, int index) {
		this.composition = composition;
		this.index = index;
		this.name = String.format("Track %02d", index + 1);
		this.defaultSample = 0;
		this.volume = 1f;
		this.source = new Source();
	}

	public void update(Pattern pattern, int position) {
		play(pattern.getNote(index, position), pattern.getSample(index, position));
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
		if (defaultSample < 0 || defaultSample >= Sample.getLibrary().size())
			return;
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

}
