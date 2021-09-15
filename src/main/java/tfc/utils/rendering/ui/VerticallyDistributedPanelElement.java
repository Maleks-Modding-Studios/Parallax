package tfc.utils.rendering.ui;

import tfc.utils.rendering.general.Color;
import tfc.utils.vecmath.Matrix4;
import tfc.wrappers.opengl.ShaderProgram;
import tfc.wrappers.opengl.Window;

public class VerticallyDistributedPanelElement extends PanelElement {
	protected double childHeight;
	
	public double scrollValue;
	public double targetScrollValue;
	
	public VerticallyDistributedPanelElement(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program, double childHeight) {
		super(startX, startY, endX, endY, color, drawFunc, program);
		this.childHeight = childHeight;
	}
	
	@Override
	public void update(double pct) {
//		scrollValue += targetScrollValue;
//		scrollValue /= 2;
		scrollValue = ((1 -pct) * scrollValue) + ((pct) * targetScrollValue);
		for (int indx = 0; indx < children.size(); indx++) {
			Element child = children.get(indx);

			// fillY would create an infinite scroll plane for one element if my implementation were setup for it, which it is not, but even if it was, allowing for it would be stupid
			child.fillY = false;
			// resize the child element and reposition it to account for scroll value, removed children, added children, etc
			child.endY = endY - (indx * childHeight) + scrollValue;
			child.startY = child.endY - childHeight;

			// calling this while iterating instead of iterating twice manages to increase the framerate by 10 with 1 million child elements
			child.update(pct);
		}
	}
	
	@Override
	public void onScroll(double mouseX, double mouseY, double posX, double posY, double dx, double dy, Window window) {
//		super.onScroll(mouseX, mouseY, posX, posY, dx, dy, window);
		targetScrollValue += (dy * -80);
		if (targetScrollValue > getMaxScroll() - (endY - startY)) targetScrollValue = getMaxScroll() - (endY - startY);
		if (targetScrollValue < 0) targetScrollValue = 0;
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
