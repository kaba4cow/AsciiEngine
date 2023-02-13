package kaba4cow.ascii.toolbox.maths.vectors;

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

	@Override
	public float lengthSq() {
		return x * x + y * y + z * z;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}

}
