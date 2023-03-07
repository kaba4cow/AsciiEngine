package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

import kaba4cow.ascii.toolbox.rng.RNG;

public class Vector2f implements AbstractVector {

	public float x;
	public float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f() {
		this(0f, 0f);
	}

	public Vector2f(Vector2f vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y);
	}

	public Vector2f set(Vector2f src) {
		this.x = src.x;
		this.y = src.y;
		return this;
	}

	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2f scale(float scale) {
		x *= scale;
		y *= scale;
		return this;
	}

	public Vector2f randomize() {
		x = RNG.randomFloat(-1f, 1f);
		y = RNG.randomFloat(-1f, 1f);
		return normalize();
	}

	public Vector2f normalize() {
		float invLength = 1f / length();
		x *= invLength;
		y *= invLength;
		return this;
	}

	public float lengthSq() {
		return x * x + y * y;
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
		Vector2f other = (Vector2f) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x)
				&& Float.floatToIntBits(y) == Float.floatToIntBits(other.y);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}

}
