package kaba4cow.ascii.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector3f;

public class AudioManager {

	private static final ArrayList<Integer> buffers = new ArrayList<>();
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

	public static int load(InputStream inputStream) throws Exception {
		int buffer = AL10.alGenBuffers();
		WaveData waveData = WaveData.create(inputStream);
		AL10.alBufferData(buffer, waveData.format, waveData.data, waveData.samplerate);
		waveData.dispose();
		buffers.add(buffer);
		return buffer;
	}

	public static int load(File file) throws Exception {
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		return load(inputStream);
	}

	public static int load(String path) throws Exception {
		InputStream inputStream = AudioManager.class.getClassLoader().getResourceAsStream(path);
		return load(inputStream);
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
		for (int i = 0; i < buffers.size(); i++)
			AL10.alDeleteBuffers(buffers.get(i));
		AL.destroy();
	}

}
