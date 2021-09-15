package tfc.utils.rendering.ui;

import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.Window;

import java.util.ArrayList;

public class Element {
	protected double startX;
	protected double startY;
	protected double endX;
	protected double endY;
	protected boolean isRightAligned;
	protected boolean fillY;
	
	public Element setRightAligned(boolean val) {
		isRightAligned = val;
		return this;
	}
	
	public Element setFillY(boolean val) {
		fillY = val;
		return this;
	}
	
	protected final ArrayList<Element> children = new ArrayList<>();
	
	public Element(double startX, double startY, double endX, double endY) {
//		if (startX > 255 || startX < 0) throw new RuntimeException("StartX is out of bounds, should be between 0 and 255 but is instead " + startX);
//		if (startY > 255 || startY < 0) throw new RuntimeException("StartY is out of bounds, should be between 0 and 255 but is instead " + startY);
//		if (endX > 255 || endX < 0) throw new RuntimeException("EndX is out of bounds, should be between 0 and 255 but is instead " + endX);
//		if (endY > 255 || endY < 0) throw new RuntimeException("EndY is out of bounds, should be between 0 and 255 but is instead " + endY);
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
	
	public void update() {
		for (Element child : children) {
			child.update();
		}
	}
	
	public boolean isInside(double mouseX, double mouseY, double posX, double posY) {
		mouseX -= posX;
		mouseY -= posY;
		return
				mouseX >= 0 && mouseX <= ((isRightAligned ? Math.abs(startX - endX) : (Math.abs((startX - endX)))) / 2) &&
						mouseY >= 0 && mouseY <= (endY / 2);
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
	
	public Element addChild(Element other) {
//		double x = other.startX;
//		double y = other.startY;
//		double w = other.endX;
//		double h = other.endY;
//
//		if (w > startX && h > startY && startX < x && y < endY) {
		children.add(other);
//		}
		return other;
	}
	
	public void onTick(double mouseX, double mouseY, double posX, double posY, Window window) {
		for (Element child : children) {
			if (child.fillY) {
				child.endY = endY;
			}
			double cPosX = child.startX + (posX * 2);
			if (child.isRightAligned) {
				cPosX = Math.abs(startX - endX) - child.startX + (posX * 2);
			}
			cPosX /= 2;
			double cPosY = child.startY - posY;
			child.onTick(mouseX, mouseY, cPosX, cPosY, window);
		}
	}
	
	public void onHovered(double mouseX, double mouseY, double posX, double posY, Window window) {
		for (Element child : children) {
			if (child.fillY) {
				child.endY = endY;
			}
			double cPosX = child.startX + (posX * 2);
			if (child.isRightAligned) {
				cPosX = Math.abs(startX - endX) - child.startX + (posX * 2);
			}
			cPosX /= 2;
			double cPosY = child.startY - posY;
			if (child.isInside(mouseX, mouseY, cPosX, cPosY)) {
				child.onHovered(mouseX, mouseY, cPosX, cPosY, window);
			}
		}
	}
	
	public void draw(Matrix4 matrix4, Matrix4 baseMatrix) {
		Matrix4 oldMatrix = baseMatrix;
		Matrix4 oldBase = baseMatrix;
		for (Element child : children) {
			if (!child.isRightAligned) {
//				matrix4 = oldMatrix.multiply(Matrix4.createTranslationMatrix(new Vector4(child.startX, child.startY, 0, 1)));
				matrix4 = oldMatrix.multiply(Matrix4.createTranslationMatrix(new Vector4(child.startX, child.startY, 0, 1)));
				oldBase = matrix4;
				matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(child.endX - child.startX, child.fillY ? (endY - child.startY) : (child.endY - child.startY), 1, 1)));
			} else {
				double cPosX = Math.abs(startX - endX) - child.startX ;
				matrix4 = oldMatrix.multiply(Matrix4.createTranslationMatrix(new Vector4(cPosX, child.startY, 0, 1)));
				oldBase = matrix4;
				matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(Math.abs(child.startX - child.endX), child.fillY ? (endY - child.startY) : (child.endY - child.startY), 1, 1)));
			}
			// TODO: setup matrix for top aligned elements
			child.draw(matrix4, child.isRightAligned ? oldBase : oldBase);
		}
	}
	
	public boolean onClicked(double mouseX, double mouseY, double posX, double posY, int button) {
		for (Element child : children) {
			if (child.fillY) {
				child.endY = endY;
			}
			double cPosX = child.startX + (posX * 2);
			if (child.isRightAligned) {
				cPosX = Math.abs(startX - endX) - child.startX + (posX * 2);
			}
			cPosX /= 2;
			double cPosY = child.startY - posY;
			if (child.isInside(mouseX, mouseY, cPosX, cPosY)) {
				if (child.onClicked(mouseX, mouseY, cPosX, cPosY, button)) return true;
			}
		}
		return false;
	}
}
