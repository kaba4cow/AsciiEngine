package kaba4cow.ascii.tracker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CompositionReader {

	private static final int INFO = 0x00;
	private static final int TRACK = 0x01;
	private static final int PATTERN = 0x02;
	private static final int SAMPLE = 0x03;

	private static final int INVALID = 0x80;
	private static final int BREAK = 0x81;

	public CompositionReader() {

	}

	public static Composition read(File file) throws Exception {
		FileInputStream input = new FileInputStream(file);
		return read(input);
	}

	public static Composition read(String path) throws Exception {
		InputStream input = CompositionReader.class.getClassLoader().getResourceAsStream(path);
		return read(input);
	}

	public static Composition read(InputStream input) throws Exception {
		Composition composition = new Composition();

		Pattern[] patternList = composition.getPatternList();

		int b;
		while ((b = input.read()) != -1) {
			if (b == INFO) {
				composition.setName(readString(input));
				composition.setAuthor(readString(input));
				composition.setLength(readInt(input));
				composition.setTempo(readInt(input));
				composition.setVolume(readFloat(input));
			} else if (b == TRACK) {
				int index = readInt(input);
				int sample = readInt(input);
				float volume = readFloat(input);
				String name = readString(input);
				Track track = composition.getTrack(index);
				track.setName(name);
				track.setDefaultSample(sample);
				track.setVolume(volume);
				int[] order = track.getPatternOrder();
				for (int i = 0; i < Composition.MAX_PATTERNS; i++) {
					int pattern = readInt(input);
					if (pattern == INVALID)
						order[i] = Composition.INVALID;
					else
						order[i] = pattern;
				}
			} else if (b == PATTERN) {
				int index = readInt(input);
				Pattern pattern = patternList[index];
				for (int track = 0; track < Composition.MAX_TRACKS; track++) {
					int notes = readInt(input);
					for (int i = 0; i < notes; i++) {
						int position = readInt(input);
						int note = readInt(input);
						if (note == BREAK)
							pattern.setNote(position, Composition.BREAK);
						else {
							int sample = readInt(input);
							pattern.setNote(position, note);
							pattern.setSample(position, sample);
						}
					}
				}
			} else if (b == SAMPLE) {
				String name = readString(input);
				byte[] bytes = readBytes(input);
				Sample sample = new Sample(name, bytes);
				composition.addSample(sample);
			}
		}

		input.close();
		return composition;
	}

	private static String readString(InputStream input) throws IOException {
		StringBuilder builder = new StringBuilder();
		int length = input.read();
		for (int i = 0; i < length; i++)
			builder.append((char) input.read());
		return builder.toString();
	}

	private static int readInt(InputStream input) throws IOException {
		return input.read();
	}

	private static float readFloat(InputStream input) throws IOException {
		return input.read() / (float) 0xFF;
	}

	private static byte[] readBytes(InputStream input) throws IOException {
		int length = 0;
		length |= input.read() << 24;
		length |= input.read() << 16;
		length |= input.read() << 8;
		length |= input.read() << 0;

		try {
			byte[] bytes = new byte[length];
			input.read(bytes, 0, length);
			return bytes;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	public static void write(Composition composition, File file) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		writeInt(output, INFO);
		writeString(output, composition.getName());
		writeString(output, composition.getAuthor());
		writeInt(output, composition.getLength());
		writeInt(output, composition.getTempo());
		writeFloat(output, composition.getVolume());

		Track[] tracks = composition.getTracks();
		for (int i = 0; i < tracks.length; i++) {
			Track track = tracks[i];
			writeInt(output, TRACK);
			writeInt(output, i);
			writeInt(output, track.getDefaultSample());
			writeFloat(output, track.getVolume());
			writeString(output, track.getName());
			int[] order = track.getPatternOrder();
			for (int j = 0; j < Composition.MAX_PATTERNS; j++)
				if (order[j] == Composition.INVALID)
					writeInt(output, INVALID);
				else
					writeInt(output, order[j]);
		}

		Pattern[] patternList = composition.getPatternList();
		for (int i = 0; i < patternList.length; i++) {
			Pattern pattern = patternList[i];
			writeInt(output, PATTERN);
			writeInt(output, pattern.getIndex());
			for (int track = 0; track < Composition.MAX_TRACKS; track++) {
				int notes = 0;
				for (int position = 0; position < Composition.PATTERN_LENGTH; position++)
					if (pattern.getNote(position) != Composition.INVALID)
						notes++;
				writeInt(output, notes);
				for (int position = 0; position < Composition.PATTERN_LENGTH; position++) {
					int note = pattern.getNote(position);
					if (note != Composition.INVALID) {
						writeInt(output, position);
						if (note == Composition.BREAK)
							writeInt(output, BREAK);
						else {
							int sample = pattern.getSample(position);
							writeInt(output, note);
							writeInt(output, sample);
						}
					}
				}
			}
		}

		ArrayList<Sample> samples = composition.getSamples();
		for (int i = 0; i < samples.size(); i++) {
			Sample sample = samples.get(i);
			writeInt(output, SAMPLE);
			writeString(output, sample.getName());
			writeBytes(output, getBytes(sample.getStream()));
		}

		output.writeTo(stream);
		output.close();
	}

	public static void writeSample(ByteArrayOutputStream output, InputStream input, File file) throws IOException {
		byte[] bytes = getBytes(input);
		output.write(bytes);
		FileOutputStream stream = new FileOutputStream(file);
		output.writeTo(stream);
		input.close();
		output.close();
		stream.close();
	}

	private static byte[] getBytes(InputStream stream) throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = stream.read(buffer)) != -1)
			output.write(buffer, 0, bytesRead);
		stream.close();
		return output.toByteArray();
	}

	private static void writeString(ByteArrayOutputStream output, String string) throws IOException {
		output.write(string.length());
		output.write(string.getBytes());
	}

	private static void writeInt(ByteArrayOutputStream output, int value) {
		output.write(value);
	}

	private static void writeFloat(ByteArrayOutputStream output, float value) {
		output.write((int) (0xFF * value));
	}

	private static void writeBytes(ByteArrayOutputStream output, byte[] bytes) throws IOException {
		output.write((bytes.length >> 24) & 0xFF);
		output.write((bytes.length >> 16) & 0xFF);
		output.write((bytes.length >> 8) & 0xFF);
		output.write((bytes.length >> 0) & 0xFF);
		output.write(bytes);
	}

}
