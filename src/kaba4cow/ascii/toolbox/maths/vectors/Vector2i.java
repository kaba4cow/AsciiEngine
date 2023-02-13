package kaba4cow.ascii.toolbox.maths.vectors;

public class Vector2i implements AbstractVector {

	public int x;
	public int y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i() {
		this(0, 0);
	}

	public Vector2i(Vector2i vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y);
	}

	@Override
	public float lengthSq() {
		return x * x + y * y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
