package kaba4cow.ascii.toolbox.maths.vectors;

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

}
