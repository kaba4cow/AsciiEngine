package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

import kaba4cow.ascii.toolbox.rng.RNG;

public class Vector3f implements AbstractVector {

	public float x;
	public float y;
	public float z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f() {
		this(0f, 0f, 0f);
	}

	public Vector3f(Vector3f vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y, vector == null ? 0 : vector.z);
	}

	public Vector3f set(Vector3f src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
		return this;
	}

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3f scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}

	public Vector3f randomize() {
		x = RNG.randomFloat(-1f, 1f);
		y = RNG.randomFloat(-1f, 1f);
		z = RNG.randomFloat(-1f, 1f);
		return normalize();
	}

	public Vector3f normalize() {
		float invLength = 1f / length();
		x *= invLength;
		y *= invLength;
		z *= invLength;
		return this;
	}

	public float lengthSq() {
		return x * x + y * y + z * z;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x)
				&& Float.floatToIntBits(y) == Float.floatToIntBits(other.y)
				&& Float.floatToIntBits(z) == Float.floatToIntBits(other.z);
	}

}
