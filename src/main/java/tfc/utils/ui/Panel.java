package tfc.utils.ui;

public class Panel {
	protected double startX;
	protected double startY;
	protected double endX;
	protected double endY;
	
	public Panel(double startX, double startY, double endX, double endY) {
		if (startX > 255 || startX < 0) throw new RuntimeException("StartX is out of bounds, should be between 0 and 255 but is instead " + startX);
		if (startY > 255 || startY < 0) throw new RuntimeException("StartY is out of bounds, should be between 0 and 255 but is instead " + startY);
		if (endX > 255 || endX < 0) throw new RuntimeException("EndX is out of bounds, should be between 0 and 255 but is instead " + endX);
		if (endY > 255 || endY < 0) throw new RuntimeException("EndY is out of bounds, should be between 0 and 255 but is instead " + endY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
	
	public boolean isInside(double mouseX, double mouseY) {
		return
				mouseX >= startX && mouseX <= endX &&
						mouseY >= startY && mouseY <= endY;
	}
}
