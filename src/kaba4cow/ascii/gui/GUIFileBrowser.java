package kaba4cow.ascii.gui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.Colors;

public class GUIFileBrowser extends GUIObject {

	private File directory;
	private File parent;
	private ArrayList<File> files;

	private int selectedFile;
	private int newSelectedFile;

	private int scroll;

	private FileFilter filter;

	public GUIFileBrowser(GUIFrame frame, int color, File home) {
		super(frame, color);
		this.files = new ArrayList<>();
		this.filter = null;
		setDirectory(home.getAbsoluteFile());
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked && newSelectedFile != -1) {
			if (selectedFile == newSelectedFile) {
				File newDirectory = files.get(selectedFile);
				setDirectory(newDirectory);
			} else {
				selectedFile = newSelectedFile;
				File file = getSelectedFile();
				if (file != null)
					onNewFileSelected(file.getAbsoluteFile());
			}
		}
		newSelectedFile = -1;

		if (Input.isKeyDown(Input.KEY_B))
			System.out.println(getSelectedFile());

		if (Input.isKey(Input.KEY_SHIFT_LEFT) && mouseX >= bX && mouseX < bX + bWidth && mouseY >= bY
				&& mouseY < bY + bHeight)
			scroll -= Input.getScroll();
		int maxScroll = (directory == null ? 6 : directory.getAbsolutePath().length()) + 1;
		for (int i = 1; i < files.size(); i++) {
			int width = 2 + files.get(i).getName().length();
			if (width > maxScroll)
				maxScroll = width;
		}
		maxScroll -= bWidth;
		if (maxScroll < 0)
			maxScroll = 0;
		if (scroll < 0)
			scroll = 0;
		else if (scroll > maxScroll)
			scroll = maxScroll;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		x -= scroll;
		Drawer.drawString(x, y++, false, directory == null ? "Drives" : directory.getAbsolutePath(), color);
		int mX = Input.getTileX();
		int mY = Input.getTileY();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			int currentColor = i == selectedFile ? Colors.swap(color) : color;
			if (i == 0)
				Drawer.draw(x, y, Glyphs.UPWARDS_ARROW, currentColor);
			else if (file.isDirectory())
				Drawer.draw(x, y, Glyphs.RIGHTWARDS_ARROW, currentColor);
			else
				Drawer.draw(x, y, Glyphs.BULLET, color);
			String name;
			if (file == null || file == parent)
				name = "..";
			else if (directory == null)
				name = file.getAbsolutePath();
			else
				name = file.getName();
			Drawer.drawString(x + 1, y, false, name, currentColor);
			if (mY == y && mX >= x && mX <= x + name.length())
				newSelectedFile = i;
			y++;
		}
		return totalLines;
	}

	@Override
	public int totalLines(int width) {
		return files.size() + 2;
	}

	public void onNewFileSelected(File file) {

	}

	public GUIFileBrowser setDirectory(File newDirectory) {
		if (newDirectory != null && !newDirectory.isDirectory())
			return this;
		selectedFile = -1;
		newSelectedFile = -1;
		directory = newDirectory;
		parent = directory == null ? null : directory.getParentFile();
		files.clear();
		files.add(parent);
		File[] list = directory == null ? File.listRoots() : directory.listFiles();
		for (int i = 0; i < list.length; i++)
			if (list[i].isDirectory() && list[i].listFiles() != null)
				files.add(list[i]);
		for (int i = 0; i < list.length; i++)
			if (list[i].isFile())
				if (filter == null || filter.accept(list[i]))
					files.add(list[i]);
		return this;
	}

	public GUIFileBrowser openDriveList() {
		selectedFile = -1;
		newSelectedFile = -1;
		directory = null;
		files.clear();

		File[] list = File.listRoots();
		files.add(null);
		for (int i = 0; i < list.length; i++) {
			files.add(list[i]);
		}

		return this;
	}

	public GUIFileBrowser refresh() {
		return setDirectory(directory);
	}

	public File getDirectory() {
		return directory;
	}

	public File getSelectedFile() {
		if (selectedFile <= 0)
			return directory;
		return files.get(selectedFile);
	}

	public GUIFileBrowser setFileFilter(FileFilter filter) {
		this.filter = filter;
		return this;
	}

}
