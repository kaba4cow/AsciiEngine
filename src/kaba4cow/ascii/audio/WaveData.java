package kaba4cow.ascii.audio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class WaveData {

	public final int format;
	public final int samplerate;
	public final int totalBytes;
	public final int bytesPerFrame;
	public final ByteBuffer data;

	private final AudioInputStream stream;
	private final byte[] dataArray;

	private WaveData(AudioInputStream stream) throws IOException {
		AudioFormat format = stream.getFormat();
		format = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);
		stream = AudioSystem.getAudioInputStream(format, stream);

		this.stream = stream;
		this.format = getFormat(format.getChannels(), format.getSampleSizeInBits());
		this.samplerate = (int) format.getSampleRate();
		this.bytesPerFrame = format.getFrameSize();
		this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
		this.data = BufferUtils.createByteBuffer(totalBytes);
		this.dataArray = new byte[totalBytes];

		int bytesRead = stream.read(dataArray, 0, totalBytes);
		data.clear();
		data.put(dataArray, 0, bytesRead);
		data.flip();
	}

	protected void dispose() {
		try {
			stream.close();
			data.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static WaveData create(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
		AudioInputStream stream = AudioSystem.getAudioInputStream(inputStream);
		WaveData waveData = new WaveData(stream);
		return waveData;
	}

	private static int getFormat(int channels, int bitsPerSample) {
		if (channels == 1)
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		else
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
	}

}
