package tfc.wrappers.opengl;

import org.lwjgl.opengl.GL20;

public class VertexBufferObject {
	private final int id;
	private final int target;
	
	public VertexBufferObject(int target, float[] data) {
		this.target = target;
		id = GL20.glGenBuffers();
		bind();
		upload(data);
	}
	
	public void bind() {
		GL20.glBindBuffer(target, id);
	}
	
	public void unbind() {
		GL20.glBindBuffer(target, 0);
	}
	
	public void upload(float[] data) {
		GL20.glBufferData(target, data, GL20.GL_STATIC_DRAW);
	}
	
	public void delete() {
		GL20.glDeleteBuffers(id);
	}
}
