package kaba4cow.ascii.audio;

import org.lwjgl.openal.AL10;
import org.lwjgl.util.vector.Vector3f;

public class Source {

	private final int source;

	private boolean wasStopped;
	private boolean deleteOnStop;

	public Source(String tag, Vector3f position) {
		this.source = AL10.alGenSources();
		this.deleteOnStop = false;
		this.wasStopped = false;
		this.setGain(1f);
		this.setPitch(1f);
		this.setPosition(position);
	}

	public Source(String tag) {
		this(tag, new Vector3f());
	}

	public Source play(int buffer) {
		stop();
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(source);
		return this;
	}

	public Source stop() {
		if (isPlaying())
			wasStopped = true;
		AL10.alSourceStop(source);
		return this;
	}

	public Source setGain(float gain) {
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
		return this;
	}

	public Source setPitch(float pitch) {
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
		return this;
	}

	public Source setPosition(Vector3f position) {
		AL10.alSource3f(source, AL10.AL_POSITION, position.x, position.y, position.z);
		return this;
	}

	public Source setVelocity(Vector3f velocity) {
		AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
		return this;
	}

	public Source setRolloffFactor(float factor) {
		AL10.alSourcef(source, AL10.AL_ROLLOFF_FACTOR, factor);
		return this;
	}

	public Source setReferenceDistance(float distance) {
		AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, distance);
		return this;
	}

	public Source setMaxDistance(float distance) {
		AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, distance);
		return this;
	}

	public Source pauseOn() {
		AL10.alSourcePause(source);
		return this;
	}

	public Source pauseOff() {
		AL10.alSourcePlay(source);
		return this;
	}

	public Source loopOn() {
		AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_TRUE);
		return this;
	}

	public Source loopOff() {
		AL10.alSourcei(source, AL10.AL_LOOPING, AL10.AL_FALSE);
		return this;
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public boolean isDeleteOnStop() {
		return deleteOnStop;
	}

	public Source setDeleteOnStop(boolean deleteOnStop) {
		this.deleteOnStop = deleteOnStop;
		return this;
	}

	public boolean wasStopped() {
		return wasStopped;
	}

	public void delete() {
		stop();
		AL10.alDeleteSources(source);
		AudioManager.remove(this);
	}

	public int getSource() {
		return source;
	}

}
