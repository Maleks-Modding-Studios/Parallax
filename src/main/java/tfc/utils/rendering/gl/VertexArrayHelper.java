package tfc.utils.rendering.gl;

import tfc.wrappers.opengl.VertexArrayObject;
import tfc.wrappers.opengl.VertexBufferObject;

public class VertexArrayHelper {
	VertexArrayObject vao;
	VertexBufferObject vbo;
	
	public VertexArrayHelper(VertexArrayObject vao, VertexBufferObject vbo) {
		this.vao = vao;
		this.vbo = vbo;
		vao.bind();
		vbo.bind();
		vao.unbind();
	}
	
	public void draw(int type) {
		vao.bind();
		vao.drawArrays(type);
		vao.unbind();
	}
	
	public void delete() {
		vao.delete();
		vbo.delete();
	}
}
