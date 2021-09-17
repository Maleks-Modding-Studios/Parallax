package tfc.utils.rendering.font;

/**
 * These functions are taken from the java specifications
 * {@link java.awt.geom.PathIterator}
 */
public class PointInterpolator {
	// https://www.baeldung.com/java-calculate-factorial
	protected static long fact(int v) {
		long fact = 1;
		for (int i = v; i > 0; i--) fact = fact * i;
		return fact;
	}
	
	/**
	 * C(x, y) = fact(x) / (fact(y) * fact(x-y))
	 */
	public static long combinations(int x, int y) {
		return fact(x) / (fact(y) * fact(x - y));
	}
	
	/**
	 * B(x, y) = C(x, y) * prog ^ (y) * (1 - prog) ^ (x - y)
	 */
	public static double coefficient(double pct, int x, int y) {
//		return C(x, y) * pct ^ (y) * (1 - pct) ^ (x - y);
//		return C(x, y) * ((int)(pct * 32)) ^ (y) * (1 - (int)(pct * 32)) ^ (x - y);
//		return C(x, y) * ((((int)(pct * 100)) ^ ((y * 100))) / 100d) * ((((int)((1 - pct) * 100)) ^ (((x - y) * 100))) / 100d);
		return combinations(x, y) * Math.pow((pct), (y)) * Math.pow((1 - pct), (x - y));
	}
	
	/**
	 * P(prog) = B(2, 0) * currentPoint + B(2, 1) * control1 + B(2, 2) * control2
	 * B(x, y) = C(x, y) * prog ^ (y) * (1 - prog) ^ (x - y)
	 * C(x, y) = fact(x) / (fact(y) * fact(x - y))
	 */
	public static class QuadSegment {
		public static double[] interp(double pct, double curX, double curY, double ctrl1x, double ctrl1y, double ctrl2x, double ctrl2y) {
			double b20 = coefficient(pct, 2, 0);
			double b21 = coefficient(pct, 2, 1);
			double b22 = coefficient(pct, 2, 2);
			
			curX *= b20;
			curY *= b20;
			
			ctrl1x *= b21;
			ctrl1y *= b21;
			
			ctrl2x *= b22;
			ctrl2y *= b22;
			
			// cur + ctrl1 + ctrl2
			double oX = curX + ctrl1x + ctrl2x;
			double oY = curY + ctrl1y + ctrl2y;
			
			return new double[] {oX, oY};
		}
	}
	
	/**
	 * P(prog) = B(3, 0) * currentPoint + B(3, 1) * control1 + B(3, 2) * control2 + B(3, 3) * control3
	 * B(x, y) = C(x, y) * prog ^ (y) * (1 - prog) ^ (x - y)
	 * C(x, y) = fact(x) / (fact(y) * fact(x - y))
	 */
	public static class CubicSegment {
		public static double[] interp(double pct, double curX, double curY, double ctrl1x, double ctrl1y, double ctrl2x, double ctrl2y, double ctrl3x, double ctrl3y) {
			double b30 = coefficient(pct, 2, 0);
			double b31 = coefficient(pct, 2, 1);
			double b32 = coefficient(pct, 2, 2);
			double b33 = coefficient(pct, 2, 2);
			
			curX *= b30;
			curY *= b30;
			
			ctrl1x *= b31;
			ctrl1y *= b31;
			
			ctrl2x *= b32;
			ctrl2y *= b32;
			
			ctrl3x *= b33;
			ctrl3y *= b33;
			
			// cur + ctrl1 + ctrl2 + ctrl3
			double oX = curX + ctrl1x + ctrl2x + ctrl3x;
			double oY = curY + ctrl1y + ctrl2y + ctrl3y;
			
			return new double[] {oX, oY};
		}
	}
}
