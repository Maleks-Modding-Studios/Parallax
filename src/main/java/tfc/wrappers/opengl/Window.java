package tfc.wrappers.opengl;

import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private final long handle;
	
	private final int[] width = new int[]{100};
	private final int[] height = new int[]{100};
	private final double[] mouseX = new double[]{-1};
	private final double[] mouseY = new double[]{-1};
	
	private final GLFWUtils glfwUtils = new GLFWUtils();
	
	public Window() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		handle = glfwCreateWindow(100, 100, "~∃(x)(H(x) ^ I(x))", NULL, NULL);
		grabContext();
		glfwSwapInterval(0);
		releaseContext();
	}
	
	public void allowVsync(boolean val) {
		glfwSwapInterval(val ? 1 : 0);
	}
	
	public void releaseContext() {
		glfwMakeContextCurrent(NULL);
	}
	
	public void grabContext() {
		glfwMakeContextCurrent(handle);
	}
	
	public void endFrame() {
		glfwSwapBuffers(handle);
		glfwPollEvents();
		
		glfwGetWindowSize(handle, width, height);
		glfwGetCursorPos(handle, mouseX, mouseY);
	}
	
	public void setVisible(boolean value) {
		if (value) glfwShowWindow(handle);
		else glfwHideWindow(handle);
	}
	
	public void setTitle(String text) {
		glfwSetWindowTitle(handle, text);
	}
	
	public void setWidth(int width) {
		glfwSetWindowSize(handle, this.width[0] = width, height[0]);
	}
	
	public void setHeight(int height) {
		glfwSetWindowSize(handle, width[0], this.height[0] = height);
	}
	
	public void setSize(int width, int height) {
		glfwSetWindowSize(handle, this.width[0] = width, this.height[0] = height);
	}
	
	public void addCloseListener(GLFWWindowCloseCallbackI action) {
		glfwSetWindowCloseCallback(handle, action);
	}
	
	public int getWidth() {
		return width[0];
	}
	
	public int getHeight() {
		return height[0];
	}
	
	public double getMouseX() {
		return mouseX[0];
	}
	
	public double getMouseY() {
		return mouseY[0];
	}
	
	public void destroy() {
		glfwDestroyWindow(handle);
	}
	
	public void setCursor(int cursor) {
		glfwSetCursor(handle, glfwUtils.getCursor(cursor));
	}
	
	public boolean isMouseButtonDown(int button) {
		return glfwGetMouseButton(handle, button) == GLFW_PRESS;
	}
}
