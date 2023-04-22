package kaba4cow.ascii.toolbox.maths.vectors;

import kaba4cow.ascii.toolbox.maths.Maths;

public final class Vectors {
	
	private Vectors() {

	}

	public static Vector2f fromAngle(float angle, Vector2f dest) {
		if (dest == null)
			dest = new Vector2f();
		dest.x = Maths.cos(angle);
		dest.y = Maths.sin(angle);
		return dest;
	}

	public static Vector2i add(Vector2i vec1, Vector2i vec2, int scale, Vector2i dest) {
		if (dest == null)
			dest = new Vector2i();
		dest.x = vec1.x + scale * vec2.x;
		dest.y = vec1.y + scale * vec2.y;
		return dest;
	}

	public static Vector2i sub(Vector2i vec1, Vector2i vec2, int scale, Vector2i dest) {
		if (dest == null)
			dest = new Vector2i();
		dest.x = vec1.x - scale * vec2.x;
		dest.y = vec1.y - scale * vec2.y;
		return dest;
	}

	public static Vector3i add(Vector3i vec1, Vector3i vec2, int scale, Vector3i dest) {
		if (dest == null)
			dest = new Vector3i();
		dest.x = vec1.x + scale * vec2.x;
		dest.y = vec1.y + scale * vec2.y;
		return dest;
	}

	public static Vector3i sub(Vector3i vec1, Vector3i vec2, int scale, Vector3i dest) {
		if (dest == null)
			dest = new Vector3i();
		dest.x = vec1.x - scale * vec2.x;
		dest.y = vec1.y - scale * vec2.y;
		return dest;
	}

	public static Vector2f add(Vector2f vec1, Vector2f vec2, float scale, Vector2f dest) {
		if (dest == null)
			dest = new Vector2f();
		dest.x = vec1.x + scale * vec2.x;
		dest.y = vec1.y + scale * vec2.y;
		return dest;
	}

	public static Vector2f sub(Vector2f vec1, Vector2f vec2, float scale, Vector2f dest) {
		if (dest == null)
			dest = new Vector2f();
		dest.x = vec1.x - scale * vec2.x;
		dest.y = vec1.y - scale * vec2.y;
		return dest;
	}

	public static Vector3f add(Vector3f vec1, Vector3f vec2, float scale, Vector3f dest) {
		if (dest == null)
			dest = new Vector3f();
		dest.x = vec1.x + scale * vec2.x;
		dest.y = vec1.y + scale * vec2.y;
		return dest;
	}

	public static Vector3f sub(Vector3f vec1, Vector3f vec2, float scale, Vector3f dest) {
		if (dest == null)
			dest = new Vector3f();
		dest.x = vec1.x - scale * vec2.x;
		dest.y = vec1.y - scale * vec2.y;
		return dest;
	}

}
