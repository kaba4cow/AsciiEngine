package kaba4cow.ascii.audio;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector3f;

import kaba4cow.ascii.toolbox.Printer;

public class AudioManager {

	private static final HashMap<String, Integer> buffers = new HashMap<>();
	private static final ArrayList<Source> sources = new ArrayList<>();

	public static final int LINEAR_DISTANCE = AL11.AL_LINEAR_DISTANCE;
	public static final int LINEAR_DISTANCE_CLAMPED = AL11.AL_LINEAR_DISTANCE_CLAMPED;
	public static final int EXPONENT_DISTANCE = AL11.AL_EXPONENT_DISTANCE;
	public static final int EXPONENT_DISTANCE_CLAMPED = AL11.AL_EXPONENT_DISTANCE_CLAMPED;

	public static void update() {
		Source current = null;
		for (int i = sources.size() - 1; i >= 0; i--) {
			current = sources.get(i);
			if (current.wasStopped() && current.isDeleteOnStop())
				current.delete();
		}
	}

	public static void setDistanceModel(int distanceModel) {
		AL10.alDistanceModel(distanceModel);
	}

	public static void setListenerData(Vector3f position, Vector3f velocity) {
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}

	public static int load(String file) {
		if (buffers.containsKey(file))
			return buffers.get(file);
		Printer.println("Loading audio file: " + file);
		int buffer = AL10.alGenBuffers();
		WaveData waveData = WaveData.create(file);
		AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.samplerate);
		waveData.dispose();
		buffers.put(file, buffer);
		return buffer;
	}

	public static int get(String file) {
		if (!buffers.containsKey(file))
			load(file);
		return buffers.get(file);
	}

	protected static void add(Source source) {
		if (source != null && !sources.contains(source))
			sources.add(source);
	}

	protected static void remove(Source source) {
		if (source != null && sources.contains(source)) {
			sources.remove(source);
		}
	}

	public static void cleanUp() {
		for (int i = 0; i < sources.size(); i++)
			sources.get(i).delete();
		for (String key : buffers.keySet())
			AL10.alDeleteBuffers(buffers.get(key));
		AL.destroy();
	}

}
