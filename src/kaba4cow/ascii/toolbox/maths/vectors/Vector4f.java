package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

import kaba4cow.ascii.toolbox.rng.RNG;

public class Vector4f implements AbstractVector {

	public float x;
	public float y;
	public float z;
	public float w;

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.z = w;
	}

	public Vector4f() {
		this(0f, 0f, 0f, 0f);
	}

	public Vector4f(Vector4f vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y, vector == null ? 0 : vector.z,
				vector == null ? 0 : vector.w);
	}

	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.z = w;
		return this;
	}

	public Vector4f scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
		return this;
	}

	public Vector4f randomize() {
		x = RNG.randomFloat(-1f, 1f);
		y = RNG.randomFloat(-1f, 1f);
		z = RNG.randomFloat(-1f, 1f);
		w = RNG.randomFloat(-1f, 1f);
		return normalize();
	}

	public Vector4f normalize() {
		float invLength = 1f / length();
		x *= invLength;
		y *= invLength;
		z *= invLength;
		w *= invLength;
		return this;
	}

	public float lengthSq() {
		return x * x + y * y + z * z + w * w;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + ", " + w + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, w);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector4f other = (Vector4f) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x)
				&& Float.floatToIntBits(y) == Float.floatToIntBits(other.y)
				&& Float.floatToIntBits(z) == Float.floatToIntBits(other.z)
				&& Float.floatToIntBits(w) == Float.floatToIntBits(other.w);
	}

}
