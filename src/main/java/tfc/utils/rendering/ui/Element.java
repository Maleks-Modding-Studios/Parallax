package tfc.utils.rendering.ui;

import tfc.utils.vecmath.Matrix4;

import java.util.ArrayList;

public class Element {
	protected double startX;
	protected double startY;
	protected double endX;
	protected double endY;
	protected boolean isRightAligned;
	
	public void setRightAligned(boolean val) {
		isRightAligned = val;
	}
	
	protected final ArrayList<Element> children = new ArrayList<>();
	
	public Element(double startX, double startY, double endX, double endY) {
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
	
	public double getStartX() {
		return startX;
	}
	
	public double getStartY() {
		return startY;
	}
	
	public double getEndX() {
		return endX;
	}
	
	public double getEndY() {
		return endY;
	}
	
	public void addChild(Element other) {
//		double x = other.startX;
//		double y = other.startY;
//		double w = other.endX;
//		double h = other.endY;
//
//		if (w > startX && h > startY && startX < x && y < endY) {
			children.add(other);
//		}
	}
	
	public void draw(Matrix4 matrix4) {
		for (Element child : children) {
			// TODO: setup matrix
			// TODO: setup matrix for right aligned elements
			// TODO: setup matrix for top aligned elements
			child.draw(matrix4);
		}
	}
	
	public boolean onClicked(double mouseX, double mouseY, int button) {
		for (Element child : children)
			if (child.isInside(mouseX, mouseY))
				if (child.onClicked(mouseX, mouseY, button)) return true;
		return false;
	}
}
