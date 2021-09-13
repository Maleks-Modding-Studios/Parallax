import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import tfc.wrappers.opengl.Window;

public class Test1 {
	public static void main(String[] args) {
		GLFW.glfwInit();
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		Window w = new Window();
		w.setVisible(true);
		boolean[] isOpen = new boolean[]{true};
		w.addCloseListener((window) -> isOpen[0] = false);
		
		w.grabContext();
		w.setCursor(GLFW.GLFW_HRESIZE_CURSOR);
		w.allowVsync(true);
		w.releaseContext();
		
		while (isOpen[0]) {
			w.grabContext();
			w.endFrame();
			w.releaseContext();
		}
		
		w.destroy();
	}
}
