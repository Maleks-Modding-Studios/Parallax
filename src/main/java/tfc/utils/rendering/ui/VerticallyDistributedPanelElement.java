package tfc.utils.rendering.ui;

import tfc.utils.rendering.general.Color;
import tfc.utils.vecmath.Matrix4;
import tfc.wrappers.opengl.ShaderProgram;

public class VerticallyDistributedPanelElement extends PanelElement {
	protected double childHeight;
	
	public double scrollValue;
	
	public VerticallyDistributedPanelElement(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program, double childHeight) {
		super(startX, startY, endX, endY, color, drawFunc, program);
		this.childHeight = childHeight;
	}
	
	// onHovered is called before any other methods
	@Override
	public void update() {
		int indx = 0;
		for (Element child : children) {
			// fillY would create an infinite scroll plane for one element if my implementation were setup for it, which it is not, but even if it was, allowing for it would be stupid
			child.fillY = false;
			// resize the child element and reposition it to account for scroll value, removed children, added children, etc
			child.endY = endY - (indx++ * childHeight) + scrollValue;
//			child.endY = scrollValue - endY - (indx++ * childHeight);
			child.startY = child.endY - childHeight;
		}
		super.update();
	}
	
	@Override
	public void draw(Matrix4 thisMatrix, Matrix4 baseMatrix) {
		super.draw(thisMatrix, baseMatrix);
	}
	
	public double getMaxScroll() {
		return childHeight * children.size();
	}
	
	public double getScrollValue() {
		return scrollValue;
	}
	
	public void setScrollValue(double scrollValue) {
		this.scrollValue = scrollValue;
	}
}
