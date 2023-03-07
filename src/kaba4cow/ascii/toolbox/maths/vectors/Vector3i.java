package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

public class Vector3i implements AbstractVector {

	public int x;
	public int y;
	public int z;

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.y = z;
	}

	public Vector3i() {
		this(0, 0, 0);
	}

	public Vector3i(Vector3i vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y, vector == null ? 0 : vector.z);
	}

	public Vector3i set(Vector3i src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
		return this;
	}

	public Vector3i set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vector3i scale(int scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		return this;
	}

	@Override
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
		Vector3i other = (Vector3i) obj;
		return x == other.x && y == other.y && z == other.z;
	}

}
