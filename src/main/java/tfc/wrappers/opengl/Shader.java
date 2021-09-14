package tfc.wrappers.opengl;

import org.lwjgl.opengl.GL40;

public class Shader {
	private final int id;
	
	public Shader(int type, String contents) {
		id = GL40.glCreateShader(type);
		GL40.glShaderSource(id, contents);
		GL40.glCompileShader(id);
		String log = GL40.glGetShaderInfoLog(id);
		if (log != "") System.err.println(log);
	}
	
	public int getId() {
		return id;
	}
	
	public void delete() {
		GL40.glDeleteShader(id);
	}
}
