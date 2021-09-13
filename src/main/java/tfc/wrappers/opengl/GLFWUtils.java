package tfc.wrappers.opengl;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class GLFWUtils {
	private final HashMap<Integer, Long> cursorMap = new HashMap<>();
	
	public long getCursor(int id) {
		if (cursorMap.containsKey(id)) return cursorMap.get(id);
		cursorMap.put(id, glfwCreateStandardCursor(id));
		return cursorMap.get(id);
	}
}
