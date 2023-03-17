package kaba4cow.ascii.drawing.gui;

public abstract class GUIObject {

	public final GUIFrame frame;
	public int color;

	protected int bX;
	protected int bY;
	protected int bWidth;
	protected int bHeight;

	public GUIObject(GUIFrame frame, int color) {
		this.frame = frame;
		this.color = color == -1 ? frame.color : color;
		frame.add(this);
	}

	public void update(int mouseX, int mouseY, boolean clicked) {

	}

	public abstract int render(int x, int y, int width, int height);

	public abstract int totalLines(int width);

	protected void updateBounds(int x, int y, int width, int height) {
		bX = x;
		bY = y;
		bWidth = width;
		bHeight = height;
	}

	protected boolean mouseInBounds(int mouseX, int mouseY) {
		return mouseX >= bX && mouseX < bX + bWidth && mouseY >= bY && mouseY < bY + bHeight;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

}
