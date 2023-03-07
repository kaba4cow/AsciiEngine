package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

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

	public Vector2i set(Vector2i src) {
		this.x = src.x;
		this.y = src.y;
		return this;
	}

	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2i scale(int scale) {
		x *= scale;
		y *= scale;
		return this;
	}

	@Override
	public float lengthSq() {
		return x * x + y * y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2i other = (Vector2i) obj;
		return x == other.x && y == other.y;
	}

}
