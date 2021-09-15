package tfc.utils.rendering.ui;

import org.lwjgl.glfw.GLFW;
import tfc.utils.rendering.general.Color;
import tfc.utils.vecmath.Matrix4;
import tfc.wrappers.opengl.ShaderProgram;
import tfc.wrappers.opengl.Window;

public class PanelElement extends Element {
	protected Color color;
	protected Runnable drawFunc;
	protected ShaderProgram program;
	
	protected ResizePolicy resizePolicy = new ResizePolicy(false, false, false, false);
	
	public PanelElement setResizePolicy(ResizePolicy resizePolicy) {
		this.resizePolicy = resizePolicy;
		return this;
	}
	
	public PanelElement(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program) {
		super(startX, startY, endX, endY);
		this.color = color;
		this.drawFunc = drawFunc;
		this.program = program;
	}
	
	@Override
	public void draw(Matrix4 thisMatrix, Matrix4 baseMatrix) {
		program.start();
		if (this.isFocused) {
			Color color = this.color.hueshift(-50);
			program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		} else {
			program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		}
		program.uniformMatrix4("modelView", thisMatrix.toArray());
		drawFunc.run();
		program.finish();
		super.draw(thisMatrix, baseMatrix);
	}
	
	private boolean isBeingResizedLeft = false;
	private boolean isBeingResizedRight = false;
	
	@Override
	public boolean onClicked(double mouseX, double mouseY, double posX, double posY, int button) {
		if (super.onClicked(mouseX, mouseY, posX, posY, button)) return true;
		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			if (resizePolicy.canResizeLeft) {
				if (posX - mouseX >= -5 || isBeingResizedLeft) {
					isBeingResizedLeft = true;
					return true;
				}
			}
			if (resizePolicy.canResizeRight) {
				double mx = (posX - mouseX);
				double width = Math.abs((startX - endX) / 2);
				if (mx + width <= 5) {
					isBeingResizedRight = true;
					return true;
				}
			}
		} else {
			isBeingResizedLeft = false;
		}
		return false;
	}
	
	@Override
	public void onTick(double mouseX, double mouseY, double posX, double posY, Window window) {
		if (!window.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			isBeingResizedLeft = false;
			isBeingResizedRight = false;
		}
		if (isBeingResizedLeft) {
			double offX = mouseX - posX;
			if (isRightAligned) {
				if ((startX - offX) < (endX + 30)) {
					offX = (startX - (endX + 30));
				}
				startX -= offX;
			} else {
				if ((startX + offX) > (endX - 30)) {
					offX = (endX - 30) - (startX);
				}
				startX += offX;
			}
			window.setCursor(GLFW.GLFW_HRESIZE_CURSOR);
		} else if (isBeingResizedRight) {
			double offX = mouseX - (posX + (Math.abs(endX - startX) / 2));
			if (isRightAligned) {
				if ((endX - offX) > (startX - 30)) {
					offX = (endX - (startX - 30));
				}
				endX -= offX;
			} else {
				if ((endX + offX) < (startX + 30)) {
					offX = (startX + 30) - (endX);
				}
				endX += offX;
			}
			window.setCursor(GLFW.GLFW_HRESIZE_CURSOR);
		}
		super.onTick(mouseX, mouseY, posX, posY, window);
	}
	
	@Override
	public void onHovered(double mouseX, double mouseY, double posX, double posY, Window window) {
		if (resizePolicy.canResizeLeft) {
			if (posX - mouseX >= -5) {
				window.setCursor(GLFW.GLFW_HRESIZE_CURSOR);
			}
		}
		if (resizePolicy.canResizeRight) {
			double mx = (posX - mouseX);
			double width = Math.abs((startX - endX) / 2);
			if (mx + width <= 5) {
				window.setCursor(GLFW.GLFW_HRESIZE_CURSOR);
			}
		}
		
		super.onHovered(mouseX, mouseY, posX, posY, window);
	}
}
