package tfc.wrappers.opengl;

import org.lwjgl.opengl.GL40;

public class VertexArrayObject {
	private final int id;
	public int elementCount;
	
	public VertexArrayObject(int elements) {
		id = GL40.glGenVertexArrays();
		elementCount = elements;
	}
	
	public void bind() {
		GL40.glBindVertexArray(id);
	}
	
	public void unbind() {
		GL40.glBindVertexArray(0);
	}
	
	public void delete() {
		GL40.glDeleteVertexArrays(id);
	}
	
	public void drawArrays(int mode) {
		GL40.glDrawArrays(mode, 0, elementCount);
	}
}
