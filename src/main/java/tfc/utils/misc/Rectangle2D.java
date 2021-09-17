package tfc.utils.misc;

public class Rectangle2D {
	public double minX, minY, maxX, maxY;
	
	public Rectangle2D(double minX, double minY, double maxX, double maxY) {
		this.minX = Math.min(minX, maxX);
		this.minY = Math.min(minY, maxY);
		this.maxX = Math.max(minX, maxX);
		this.maxY = Math.max(minY, maxY);
	}
	
	public Rectangle2D intersection(Rectangle2D other) {
		if (!intersects(other)) return null;
		double minX = Math.max(this.minX, other.minX);
		double minY = Math.max(this.minY, other.minY);
		double maxX = Math.min(this.maxX, other.maxX);
		double maxY = Math.min(this.maxY, other.maxY);
		return new Rectangle2D(minX, minY, maxX, maxY);
	}
	
	public boolean intersects(Rectangle2D other) {
		return (
				other.maxX > minX &&
						other.maxY > minY &&
						other.minX < maxX &&
						other.minY < maxY
		);
	}
}
