package tfc.utils.vecmath;

public class Vector4 {
	public double x, y, z, w;
	
	public Vector4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4() {
	}
	
	public Vector4 transform(Matrix4 m) {
		double x = this.x;
		double y = this.y;
		double z = this.z;
		double w = this.w;
		this.x = x * m.a00 + y * m.a01 + z * m.a02 + w * m.a03;
		this.y = x * m.a10 + y * m.a11 + z * m.a12 + w * m.a13;
		this.z = x * m.a20 + y * m.a21 + z * m.a22 + w * m.a23;
		this.w = x * m.a30 + y * m.a31 + z * m.a32 + w * m.a33;
		return this;
	}
	
	public Vector4 scale(double amt) {
		x *= amt;
		y *= amt;
		z *= amt;
		w *= amt;
		return this;
	}
	
	public Vector4 subtract(Vector4 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		w -= vec.w;
		return this;
	}
	
	public Vector4 subtract(double x, double y, double z, double w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
		return this;
	}
	
	public Vector4 add(Vector4 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		w += vec.w;
		return this;
	}
	
	public Vector4 add(double x, double y, double z, double w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
	public Vector4 multiply(Vector4 vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		w *= vec.w;
		return this;
	}
	
	public Vector4 multiply(double x, double y, double z, double w) {
		this.x *= x;
		this.y *= y;
		this.z *= z;
		this.w *= w;
		return this;
	}
	
	public Vector4 divide(Vector4 vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		w /= vec.w;
		return this;
	}
	
	public Vector4 divide(double x, double y, double z, double w) {
		this.x /= x;
		this.y /= y;
		this.z /= z;
		this.w /= w;
		return this;
	}
	
	public Vector4 inverse() {
		x *= -1;
		y *= -1;
		z *= -1;
		w *= -1;
		return this;
	}
	
	public double length() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Vector4 normalize() {
		return scale(1 / length());
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{").append(x).append(", ").append(y).append(", ").append(z).append(", ").append(w).append("}");
		return builder.toString();
	}
}
