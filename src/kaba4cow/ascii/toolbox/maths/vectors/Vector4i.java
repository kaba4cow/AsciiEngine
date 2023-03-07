package kaba4cow.ascii.toolbox.maths.vectors;

import java.util.Objects;

public class Vector4i implements AbstractVector {

	public int x;
	public int y;
	public int z;
	public int w;

	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.y = w;
	}

	public Vector4i() {
		this(0, 0, 0, 0);
	}

	public Vector4i(Vector4i vector) {
		this(vector == null ? 0 : vector.x, vector == null ? 0 : vector.y, vector == null ? 0 : vector.z,
				vector == null ? 0 : vector.w);
	}

	public Vector4i set(Vector4i src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
		this.w = src.w;
		return this;
	}

	public Vector4i set(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Vector4i scale(int scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
		return this;
	}

	@Override
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
		Vector4i other = (Vector4i) obj;
		return x == other.x && y == other.y && z == other.z && w == other.w;
	}

}
