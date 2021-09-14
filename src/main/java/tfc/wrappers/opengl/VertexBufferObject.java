package tfc.wrappers.opengl;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class VertexBufferObject {
	private final int id;
	private final int target;
	
	private FloatBuffer buffer = null;
	
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
		if (buffer != null) MemoryUtil.memFree(buffer);
		FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(data.length);
		verticesBuffer.put(data).flip();
		GL20.glBufferData(target, verticesBuffer, GL20.GL_STATIC_DRAW);
		buffer = verticesBuffer;
	}
	
	public void delete() {
		GL20.glDeleteBuffers(id);
	}
}
