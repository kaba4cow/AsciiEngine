package kaba4cow.ascii;

import java.io.File;

public interface MainProgram {

	public void init();

	public void update(float dt);

	public void render();

	public default void onGainedFocus() {

	}

	public default void onLostFocus() {

	}

	public default void onClose() {

	}

	public default void onFilesDropped(File[] files) {

	}

}
