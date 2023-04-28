package kaba4cow.ascii.gui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import kaba4cow.ascii.core.Input;
import kaba4cow.ascii.drawing.Drawer;
import kaba4cow.ascii.drawing.Glyphs;
import kaba4cow.ascii.toolbox.Colors;

public class GUIFileTree extends GUIObject {

	private Node root;

	private Node selectedNode;
	private Node newSelectedNode;
	private boolean openNode;

	private int scroll;

	private FileFilter filter;

	private char directoryClosedGlyph;
	private char directoryOpenGlyph;
	private char fileGlyph;

	public GUIFileTree(GUIFrame frame, int color, File directory) {
		super(frame, color);
		this.scroll = 0;
		this.filter = null;
		this.directoryClosedGlyph = Glyphs.RIGHTWARDS_ARROW;
		this.directoryOpenGlyph = Glyphs.DOWNWARDS_ARROW;
		this.fileGlyph = Glyphs.BULLET;
		setDirectory(directory);
	}

	@Override
	public void update(int mouseX, int mouseY, boolean clicked) {
		if (clicked && newSelectedNode != null) {
			selectedNode = null;
			if (openNode)
				newSelectedNode.switchOpen();
			else {
				selectedNode = newSelectedNode;
				if (selectedNode.file != null)
					onNewFileSelected(selectedNode.file.getAbsoluteFile());
			}
		}
		newSelectedNode = null;
		openNode = false;

		if (Input.isKey(Input.KEY_SHIFT_LEFT) && mouseX >= bX && mouseX < bX + bWidth && mouseY >= bY
				&& mouseY < bY + bHeight)
			scroll -= Input.getScroll();
		int maxScroll = root.maxWidth() - bWidth;
		if (maxScroll < 0)
			maxScroll = 0;
		if (scroll < 0)
			scroll = 0;
		else if (scroll > maxScroll)
			scroll = maxScroll;
	}

	@Override
	public int render(int x, int y, int width, int height) {
		root.render(x - scroll, y, Input.getTileX(), Input.getTileY());
		int totalLines = totalLines(width);
		updateBounds(x, y, width, totalLines);
		return totalLines;
	}

	@Override
	public int totalLines(int width) {
		return root.totalLines();
	}

	public void onNewFileSelected(File file) {

	}

	public GUIFileTree setDirectory(File directory) {
		root = new Node(directory, -1, true);
		selectedNode = null;
		newSelectedNode = null;
		return this;
	}

	public GUIFileTree refresh() {
		return setDirectory(getDirectory());
	}

	public File getDirectory() {
		return root.file;
	}

	public File getSelectedFile() {
		if (selectedNode == null)
			return null;
		return selectedNode.file;
	}

	public GUIFileTree setFileFilter(FileFilter filter) {
		this.filter = filter;
		return this;
	}

	public GUIFileTree setDirectoryGlyphs(char open, char closed) {
		this.directoryOpenGlyph = open;
		this.directoryClosedGlyph = closed;
		return this;
	}

	public GUIFileTree setFileGlyph(char glyph) {
		this.fileGlyph = glyph;
		return this;
	}

	private class Node {

		public final File file;
		public final ArrayList<Node> children;

		public final int depth;

		public boolean open;

		public Node(File file, int depth, boolean open) {
			File[] list = file.isFile() ? new File[0] : file.listFiles();
			this.file = file;
			this.children = new ArrayList<>();
			this.depth = depth;
			this.open = open;
			for (int i = 0; i < list.length; i++)
				if (list[i].isDirectory() || filter == null || filter.accept(list[i]))
					children.add(new Node(list[i], depth + 1, false));
		}

		public int render(int x, int y, int mX, int mY) {
			int currentColor = selectedNode == this ? Colors.swap(color) : color;
			if (depth != -1) {
				if (file.isFile())
					Drawer.draw(x, y, fileGlyph, currentColor);
				else if (open)
					Drawer.draw(x, y, directoryOpenGlyph, currentColor);
				else
					Drawer.draw(x, y, directoryClosedGlyph, currentColor);
				if (mY == y && mX >= x && mX <= x + file.getName().length()) {
					newSelectedNode = this;
					openNode = mX == x;
				}
				x++;
				Drawer.drawString(x, y, false, file.getName(), currentColor);
			}
			int totalLines = depth == -1 ? 0 : 1;
			if (open) {
				for (int i = 0; i < children.size(); i++)
					if (children.get(i).file.isDirectory())
						totalLines += children.get(i).render(x, y + totalLines, mX, mY);
				for (int i = 0; i < children.size(); i++)
					if (children.get(i).file.isFile())
						totalLines += children.get(i).render(x, y + totalLines, mX, mY);
			}
			return totalLines;
		}

		public int maxWidth() {
			int max = 0;
			if (depth != -1)
				max = depth + 2 + file.getName().length();
			if (open)
				for (int i = 0; i < children.size(); i++) {
					int width = children.get(i).maxWidth();
					if (width > max)
						max = width;
				}
			return max;
		}

		public int totalLines() {
			int total = depth == -1 ? 0 : 1;
			if (open)
				for (int i = 0; i < children.size(); i++)
					total += children.get(i).totalLines();
			return total;
		}

		public void switchOpen() {
			open = !open;
			if (!open)
				close();
		}

		public void close() {
			open = false;
			for (int i = 0; i < children.size(); i++)
				children.get(i).close();
		}

	}

}
