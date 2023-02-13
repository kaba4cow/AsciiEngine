package kaba4cow.ascii.toolbox.maths.vectors;

import kaba4cow.ascii.toolbox.maths.Maths;

public interface AbstractVector {
	
	public float lengthSq();
	
	public default float length() {
		return Maths.sqrt(lengthSq());
	}

}
