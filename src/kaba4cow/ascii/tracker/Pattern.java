package kaba4cow.ascii.tracker;

public class Pattern {

	private final Composition composition;
	private final int index;

	private final int[] notes;
	private final int[] samples;

	public Pattern(Composition composition, int index) {
		this(composition, index, new Data());
	}

	public Pattern(Composition composition, int index, Data data) {
		this.composition = composition;
		this.index = index;
		this.notes = data.notes;
		this.samples = data.samples;
	}

	public Data getData() {
		return new Data(notes, samples);
	}

	public void setData(Data data) {
			for (int i = 0; i < Composition.PATTERN_LENGTH; i++) {
				this.notes[i] = data.notes[i];
				this.samples[i] = data.samples[i];
			}
	}

	public void clear() {
			for (int i = 0; i < Composition.PATTERN_LENGTH; i++) {
				this.notes[i] = Composition.INVALID;
				this.samples[i] = 0;
			}
	}

	public int[] getNotes() {
		return notes;
	}

	public int[] getSamples() {
		return samples;
	}

	public void deleteNote(int position) {
		notes[position] = Composition.INVALID;
		samples[position] = 0;
	}

	public void setBreak(int position) {
		notes[position] = Composition.BREAK;
	}

	public void setNote(int position, int note) {
		notes[position] = note;
	}

	public void prevNote(int position) {
		if (notes[position] == Composition.INVALID || notes[position] == Composition.BREAK)
			notes[position] = Composition.NOTE_C;
		else if (notes[position] > 0)
			notes[position]--;
	}

	public void nextNote(int position) {
		if (notes[position] == Composition.INVALID || notes[position] == Composition.BREAK)
			notes[position] = Composition.NOTE_C;
		else if (notes[position] < 127)
			notes[position]++;
	}

	public void prevOctave(int position) {
		if (notes[position] == Composition.INVALID || notes[position] == Composition.BREAK)
			notes[position] = Composition.NOTE_C;
		else if (notes[position] > 12)
			notes[position] -= 12;
	}

	public void nextOctave(int position) {
		if (notes[position] == Composition.INVALID || notes[position] == Composition.BREAK)
			notes[position] = Composition.NOTE_C;
		else if (notes[position] < 115)
			notes[position] += 12;
	}

	public void setSample(int position, int sample) {
		if (notes[position] == Composition.INVALID)
			notes[position] = Composition.NOTE_C;
		samples[position] = sample;
	}

	public void prevSample(int position) {
		if (notes[position] == Composition.INVALID)
			notes[position] = Composition.NOTE_C;
		if (samples[position] - 1 >= 0)
			samples[position]--;
		else
			samples[position] = composition.getSamples().size();
	}

	public void nextSample(int position) {
		if (notes[position] == Composition.INVALID)
			notes[position] = Composition.NOTE_C;
		if (samples[position] + 1 <= composition.getSamples().size())
			samples[position]++;
		else
			samples[position] = 0;
	}

	public boolean contains(int position) {
		return notes[position] != Composition.INVALID;
	}

	public int getNote(int position) {
		return notes[position];
	}

	public int getSample(int position) {
		return samples[position];
	}

	public int getIndex() {
		return index;
	}

	public static class Data {

		private final int[] notes;
		private final int[] samples;

		public Data(int[] notes, int[] samples) {
			this.notes = new int[Composition.PATTERN_LENGTH];
			this.samples = new int[Composition.PATTERN_LENGTH];
			for (int i = 0; i < Composition.PATTERN_LENGTH; i++) {
				this.notes[i] = notes[i];
				this.samples[i] = samples[i];
			}
		}

		public Data() {
			this.notes = new int[Composition.PATTERN_LENGTH];
			this.samples = new int[Composition.PATTERN_LENGTH];
			for (int i = 0; i < Composition.PATTERN_LENGTH; i++) {
				this.notes[i] = Composition.INVALID;
				this.samples[i] = 0;
			}
		}

	}

}
