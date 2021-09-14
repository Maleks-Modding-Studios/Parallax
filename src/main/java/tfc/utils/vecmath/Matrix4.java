package tfc.utils.vecmath;

public class Matrix4 {
	public double a00, a01, a02, a03;
	public double a10, a11, a12, a13;
	public double a20, a21, a22, a23;
	public double a30, a31, a32, a33;
	
	// http://www.calcul.com/show/calculator/matrix-multiplication_;4;4;4;4
	public Matrix4 multiply(Matrix4 m2) {
		Matrix4 m1 = this;
		Matrix4 m3 = new Matrix4();
		
//		m3.a00 = (m1.a00 * m2.a00) + (m1.a01 * m2.a01) + (m1.a02 * m2.a02) + (m1.a03 * m2.a03);
//		m3.a01 = (m1.a00 * m2.a10) + (m1.a01 * m2.a11) + (m1.a02 * m2.a12) + (m1.a03 * m2.a13);
//		m3.a02 = (m1.a00 * m2.a20) + (m1.a01 * m2.a21) + (m1.a02 * m2.a22) + (m1.a03 * m2.a23);
//		m3.a03 = (m1.a00 * m2.a30) + (m1.a01 * m2.a31) + (m1.a02 * m2.a32) + (m1.a03 * m2.a33);
//
//		m3.a10 = (m1.a10 * m2.a00) + (m1.a11 * m2.a01) + (m1.a12 * m2.a02) + (m1.a13 * m2.a03);
//		m3.a11 = (m1.a10 * m2.a10) + (m1.a11 * m2.a11) + (m1.a12 * m2.a12) + (m1.a13 * m2.a13);
//		m3.a12 = (m1.a10 * m2.a20) + (m1.a11 * m2.a21) + (m1.a12 * m2.a22) + (m1.a13 * m2.a23);
//		m3.a13 = (m1.a10 * m2.a30) + (m1.a11 * m2.a31) + (m1.a12 * m2.a32) + (m1.a13 * m2.a33);
//
//		m3.a20 = (m1.a20 * m2.a00) + (m1.a21 * m2.a01) + (m1.a22 * m2.a02) + (m1.a23 * m2.a03);
//		m3.a21 = (m1.a20 * m2.a10) + (m1.a21 * m2.a11) + (m1.a22 * m2.a12) + (m1.a23 * m2.a13);
//		m3.a22 = (m1.a20 * m2.a20) + (m1.a21 * m2.a21) + (m1.a22 * m2.a22) + (m1.a23 * m2.a23);
//		m3.a23 = (m1.a20 * m2.a30) + (m1.a21 * m2.a31) + (m1.a22 * m2.a32) + (m1.a23 * m2.a33);
//
//		m3.a30 = (m1.a30 * m2.a00) + (m1.a31 * m2.a01) + (m1.a32 * m2.a02) + (m1.a33 * m2.a03);
//		m3.a31 = (m1.a30 * m2.a10) + (m1.a31 * m2.a11) + (m1.a32 * m2.a12) + (m1.a33 * m2.a13);
//		m3.a32 = (m1.a30 * m2.a20) + (m1.a31 * m2.a21) + (m1.a32 * m2.a22) + (m1.a33 * m2.a23);
//		m3.a33 = (m1.a30 * m2.a30) + (m1.a31 * m2.a31) + (m1.a32 * m2.a32) + (m1.a33 * m2.a33);

		m3.a00 = (m1.a00 * m2.a00) + (m1.a01 * m2.a10) + (m1.a02 * m2.a20) + (m1.a03 * m2.a30);
		m3.a01 = (m1.a00 * m2.a01) + (m1.a01 * m2.a11) + (m1.a02 * m2.a21) + (m1.a03 * m2.a31);
		m3.a02 = (m1.a00 * m2.a02) + (m1.a01 * m2.a12) + (m1.a02 * m2.a22) + (m1.a03 * m2.a32);
		m3.a03 = (m1.a00 * m2.a03) + (m1.a01 * m2.a13) + (m1.a02 * m2.a23) + (m1.a03 * m2.a33);

		m3.a10 = (m1.a10 * m2.a00) + (m1.a11 * m2.a10) + (m1.a12 * m2.a20) + (m1.a13 * m2.a30);
		m3.a11 = (m1.a10 * m2.a01) + (m1.a11 * m2.a11) + (m1.a12 * m2.a21) + (m1.a13 * m2.a31);
		m3.a12 = (m1.a10 * m2.a02) + (m1.a11 * m2.a12) + (m1.a12 * m2.a22) + (m1.a13 * m2.a32);
		m3.a13 = (m1.a10 * m2.a03) + (m1.a11 * m2.a13) + (m1.a12 * m2.a23) + (m1.a13 * m2.a33);

		m3.a20 = (m1.a20 * m2.a00) + (m1.a21 * m2.a10) + (m1.a22 * m2.a20) + (m1.a23 * m2.a30);
		m3.a21 = (m1.a20 * m2.a01) + (m1.a21 * m2.a11) + (m1.a22 * m2.a21) + (m1.a23 * m2.a31);
		m3.a22 = (m1.a20 * m2.a02) + (m1.a21 * m2.a12) + (m1.a22 * m2.a22) + (m1.a23 * m2.a32);
		m3.a23 = (m1.a20 * m2.a03) + (m1.a21 * m2.a13) + (m1.a22 * m2.a23) + (m1.a23 * m2.a33);

		m3.a30 = (m1.a30 * m2.a00) + (m1.a31 * m2.a10) + (m1.a32 * m2.a20) + (m1.a33 * m2.a30);
		m3.a31 = (m1.a30 * m2.a01) + (m1.a31 * m2.a11) + (m1.a32 * m2.a21) + (m1.a33 * m2.a31);
		m3.a32 = (m1.a30 * m2.a02) + (m1.a31 * m2.a12) + (m1.a32 * m2.a22) + (m1.a33 * m2.a32);
		m3.a33 = (m1.a30 * m2.a03) + (m1.a31 * m2.a13) + (m1.a32 * m2.a23) + (m1.a33 * m2.a33);
		
		return m3;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
				.append("[\n [")
				.append(a00).append(",").append(a01).append(",").append(a02).append(",").append(a03).append("],\n [")
				.append(a10).append(",").append(a11).append(",").append(a12).append(",").append(a13).append("],\n [")
				.append(a20).append(",").append(a21).append(",").append(a22).append(",").append(a23).append("],\n [")
				.append(a30).append(",").append(a31).append(",").append(a32).append(",").append(a33).append("]\n]");
		return builder.toString();
	}
	
	public Vector4 transform(Vector4 vec) {
		Vector4 output = new Vector4();
		output.x = vec.x * a00 + vec.y * a10 + vec.z * a20 + vec.w * a30;
		output.y = vec.x * a10 + vec.y * a11 + vec.z * a21 + vec.w * a31;
		output.z = vec.x * a20 + vec.y * a12 + vec.z * a22 + vec.w * a32;
		output.w = vec.x * a30 + vec.y * a13 + vec.z * a23 + vec.w * a33;
		return output;
	}
	
	public double[] toArray() {
		return new double[] {
				a00, a01, a02, a03,
				a10, a11, a12, a13,
				a20, a21, a22, a23,
				a30, a31, a32, a33
		};
	}
	
	public static Matrix4 createScaleMatrix(Vector4 scale) {
		Matrix4 mat = new Matrix4();
		mat.a00 = scale.x;
		mat.a11 = scale.y;
		mat.a22 = scale.z;
		mat.a33 = scale.w;
		return mat;
	}
	
	public static Matrix4 createTranslationMatrix(Vector4 offset) {
		Matrix4 mat = new Matrix4();
		mat.a00 = 1;
		mat.a11 = 1;
		mat.a22 = 1;
		mat.a03 = offset.x;
		mat.a13 = offset.y;
		mat.a23 = offset.z;
		mat.a33 = offset.w;
		return mat;
	}
	
	public static Matrix4 identity() {
		Matrix4 mat = new Matrix4();
		mat.a00 = 1;
		mat.a11 = 1;
		mat.a22 = 1;
		mat.a33 = 1;
		return mat;
	}
}
