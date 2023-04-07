package kaba4cow.ascii.toolbox.maths;

import java.util.ArrayList;

import kaba4cow.ascii.toolbox.maths.vectors.Vector2f;

public class Conrec {

	private static final int[] im = { 0, 1, 1, 0 };
	private static final int[] jm = { 0, 0, 1, 1 };
	private static final int[][][] castab = { { { 0, 0, 8 }, { 0, 2, 5 }, { 7, 6, 9 } },
			{ { 0, 3, 4 }, { 1, 3, 1 }, { 4, 3, 0 } }, { { 9, 6, 7 }, { 5, 2, 0 }, { 8, 0, 0 } } };

	private int[] sh;
	private float[] h;
	private float[] xh;
	private float[] yh;

	private int numberOfLayers;
	private float[] z;

	public Conrec(int numberOfLayers) {
		this.numberOfLayers = numberOfLayers;
		this.sh = new int[5];
		this.h = new float[5];
		this.xh = new float[5];
		this.yh = new float[5];
		this.z = new float[numberOfLayers];
		for (int i = 0; i < numberOfLayers; i++)
			z[i] = (float) i / (float) numberOfLayers;
	}

	public ArrayList<Vector2f[]> contour(float[][] data, int cols, int rows) {
		int m1, m2, m3, case_value;
		float dmin, dmax;
		float x1 = 0f;
		float x2 = 0f;
		float y1 = 0f;
		float y2 = 0f;
		int i, j, k, m;

		float[] x = new float[cols];
		float[] y = new float[rows];
		for (int iy = 0; iy < y.length; iy++) {
			y[iy] = iy;
			for (int ix = 0; ix < x.length; ix++)
				x[ix] = ix;
		}

		int ilb = 0;
		int iub = cols - 1;
		int jlb = 0;
		int jub = rows - 1;

		ArrayList<Vector2f[]> lines = new ArrayList<>();

		for (j = (jub - 1); j >= jlb; j--) {
			for (i = ilb; i <= iub - 1; i++) {
				float temp1, temp2;
				temp1 = Math.min(data[i][j], data[i][j + 1]);
				temp2 = Math.min(data[i + 1][j], data[i + 1][j + 1]);
				dmin = Math.min(temp1, temp2);
				temp1 = Math.max(data[i][j], data[i][j + 1]);
				temp2 = Math.max(data[i + 1][j], data[i + 1][j + 1]);
				dmax = Math.max(temp1, temp2);
			}

			for (j = (jub - 1); j >= jlb; j--) {
				for (i = ilb; i <= iub - 1; i++) {
					float temp1, temp2;
					temp1 = Maths.min(data[i][j], data[i][j + 1]);
					temp2 = Maths.min(data[i + 1][j], data[i + 1][j + 1]);
					dmin = Maths.min(temp1, temp2);
					temp1 = Maths.max(data[i][j], data[i][j + 1]);
					temp2 = Maths.max(data[i + 1][j], data[i + 1][j + 1]);
					dmax = Maths.max(temp1, temp2);

					if (dmax >= z[0] && dmin <= z[numberOfLayers - 1]) {
						for (k = 0; k < numberOfLayers; k++) {
							if (z[k] > dmin && z[k] < dmax) {
								for (m = 4; m >= 0; m--) {
									if (m > 0) {
										h[m] = data[i + im[m - 1]][j + jm[m - 1]] - z[k];
										xh[m] = x[i + im[m - 1]];
										yh[m] = y[j + jm[m - 1]];
									} else {
										h[0] = 0.25f * (h[1] + h[2] + h[3] + h[4]);
										xh[0] = 0.5f * (x[i] + x[i + 1]);
										yh[0] = 0.5f * (y[j] + y[j + 1]);
									}
									if (h[m] > 0.0) {
										sh[m] = 1;
									} else if (h[m] < 0.0) {
										sh[m] = -1;
									} else
										sh[m] = 0;
								}
								for (m = 1; m <= 4; m++) {
									m1 = m;
									m2 = 0;
									if (m != 4) {
										m3 = m + 1;
									} else {
										m3 = 1;
									}
									case_value = castab[sh[m1] + 1][sh[m2] + 1][sh[m3] + 1];
									if (case_value != 0) {
										switch (case_value) {
										case 1:
											x1 = xh[m1];
											y1 = yh[m1];
											x2 = xh[m2];
											y2 = yh[m2];
											break;
										case 2:
											x1 = xh[m2];
											y1 = yh[m2];
											x2 = xh[m3];
											y2 = yh[m3];
											break;
										case 3:
											x1 = xh[m3];
											y1 = yh[m3];
											x2 = xh[m1];
											y2 = yh[m1];
											break;
										case 4:
											x1 = xh[m1];
											y1 = yh[m1];
											x2 = xsect(m2, m3);
											y2 = ysect(m2, m3);
											break;
										case 5:
											x1 = xh[m2];
											y1 = yh[m2];
											x2 = xsect(m3, m1);
											y2 = ysect(m3, m1);
											break;
										case 6:
											x1 = xh[m3];
											y1 = yh[m3];
											x2 = xsect(m1, m2);
											y2 = ysect(m1, m2);
											break;
										case 7:
											x1 = xsect(m1, m2);
											y1 = ysect(m1, m2);
											x2 = xsect(m2, m3);
											y2 = ysect(m2, m3);
											break;
										case 8:
											x1 = xsect(m2, m3);
											y1 = ysect(m2, m3);
											x2 = xsect(m3, m1);
											y2 = ysect(m3, m1);
											break;
										case 9:
											x1 = xsect(m3, m1);
											y1 = ysect(m3, m1);
											x2 = xsect(m1, m2);
											y2 = ysect(m1, m2);
											break;
										default:
											break;
										}
										Vector2f lineStart = new Vector2f(x1, y1);
										Vector2f lineEnd = new Vector2f(x2, y2);
										lines.add(new Vector2f[] { lineStart, lineEnd });
									}
								}
							}
						}
					}
				}
			}
		}

		return lines;
	}

	private float xsect(int p1, int p2) {
		return (h[p2] * xh[p1] - h[p1] * xh[p2]) / (h[p2] - h[p1]);
	}

	private float ysect(int p1, int p2) {
		return (h[p2] * yh[p1] - h[p1] * yh[p2]) / (h[p2] - h[p1]);
	}

	public void setNumberOfLayers(int numberOfLayers) {
		this.numberOfLayers = numberOfLayers;
		this.z = new float[numberOfLayers];
		for (int i = 0; i < numberOfLayers; i++)
			z[i] = (float) i / (float) numberOfLayers;
	}

}
