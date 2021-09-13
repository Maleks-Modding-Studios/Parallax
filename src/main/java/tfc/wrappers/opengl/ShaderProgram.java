package tfc.wrappers.opengl;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL40;

import java.util.HashMap;

public class ShaderProgram {
	private final int id;
	private final Shader[] shaders;
	
	private final HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	
	public ShaderProgram(Shader[] shaders) {
		id = GL40.glCreateProgram();
		for (Shader shader : shaders) GL40.glAttachShader(id, shader.getId());
		this.shaders = shaders;
	}
	
	public int getUniformLocation(String name) {
		if (uniforms.containsKey(name)) return uniforms.get(name);
		int number = GL40.glGetUniformLocation(id, name);
		uniforms.put(name, number);
		return number;
	}
	
	public void link() {
		GL40.glLinkProgram(id);
	}
	
	public void delete() {
		for (Shader shader : shaders) GL40.glDetachShader(id, shader.getId());
		GL40.glDeleteProgram(id);
	}
	
	public void start() {
		GL40.glUseProgram(id);
	}
	
	public void finish() {
		GL40.glUseProgram(0);
	}
	
	public void deleteAttached() {
		for (Shader shader : shaders) shader.delete();
	}
	
	public int getAttribLocation(String name) {
		return GL40.glGetAttribLocation(id, name);
	}
	
	public void uniformMatrix4(String name, double[] matrix) {
//		Matrix4 matrix1 = new Matrix4();
//
//		matrix1.M11 = (float) matrix[0];
//		matrix1.M12 = (float) matrix[1];
//		matrix1.M13 = (float) matrix[2];
//		matrix1.M14 = (float) matrix[3];
//
//		matrix1.M21 = (float) matrix[4];
//		matrix1.M22 = (float) matrix[5];
//		matrix1.M23 = (float) matrix[6];
//		matrix1.M24 = (float) matrix[7];
//
//		matrix1.M31 = (float) matrix[8];
//		matrix1.M32 = (float) matrix[9];
//		matrix1.M33 = (float) matrix[10];
//		matrix1.M34 = (float) matrix[11];
//
//		matrix1.M41 = (float) matrix[12];
//		matrix1.M42 = (float) matrix[13];
//		matrix1.M43 = (float) matrix[14];
//		matrix1.M44 = (float) matrix[15];
//
//		GL.UniformMatrix4(getUniformLocation(name), true, ref matrix1);
		GL40.glUniformMatrix4dv(getUniformLocation(name), true, matrix);
	}
	
	public void uniformVec4f(String name, float x, float y, float z, float w) {
		GL40.glUniformMatrix4fv(getUniformLocation(name), true, new float[]{x, y, z, w});
	}
}
