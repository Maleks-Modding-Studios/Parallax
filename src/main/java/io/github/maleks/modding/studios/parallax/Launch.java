package io.github.maleks.modding.studios.parallax;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL20;
import tfc.utils.rendering.general.Color;
import tfc.utils.rendering.general.VertexBuilder;
import tfc.utils.rendering.gl.VertexArrayHelper;
import tfc.utils.rendering.ui.PanelElement;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.*;

public class Launch {
	public static void main(String[] args) {
		GLFW.glfwInit();
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		Window window = new Window();
		window.setVisible(true);
		boolean[] isOpen = new boolean[]{true};
		window.addCloseListener((w) -> isOpen[0] = false);
		
		window.grabContext();
		window.allowVsync(true);
		
		ShaderProgram shaderProgram;
		{
			Shader frag = new Shader(GL20.GL_FRAGMENT_SHADER,
					"#version 330 core\n" +
							"\n" +
							"in vec4 color;\n" +
							"in vec2 texCoord;\n" +
							"\n" +
							"out vec4 FragColor;\n" +
							"\n" +
							"uniform sampler2D texture;\n" +
							"uniform vec4 colMultiplier;\n" +
							"\n" +
							"void main() {\n" +
//							"\tFragColor = color * texture2D(texture, texCoord);\n" +
							"\tFragColor = color * colMultiplier;\n" +
							"}\n"
			);
			Shader vert = new Shader(GL20.GL_VERTEX_SHADER,
					"#version 330 core\n" +
							"in vec4 position;\n" +
							"in vec4 col;\n" +
							"in vec2 tex;\n" +
							"\n" +
							"out vec4 color;\n" +
							"out vec2 texCoord;\n" +
							"\n" +
							"uniform mat4 modelView;\n" +
							"\n" +
							"void main() {\n" +
							"\tgl_Position = position * modelView;\n" +
							"\tcolor = col;\n" +
							"\ttexCoord = tex;\n" +
							"}\n"
			);
			shaderProgram = new ShaderProgram(new Shader[] { frag, vert });
			shaderProgram.link();
			shaderProgram.start();
			shaderProgram.getUniformLocation("modelView");
			shaderProgram.getUniformLocation("texture");
			shaderProgram.getUniformLocation("colMultiplier");
			shaderProgram.uniformVec4f("colMultiplier", 1, 1, 1, 1);
			shaderProgram.finish();
		}
		
		VertexArrayHelper panelVAO;
		{
			VertexBuilder builder = new VertexBuilder(0);
			builder.position(-1, -1, 0, 1).color(1, 1, 1, 1).texture(0, 0).endVertex();
			builder.position(1, -1, 0, 1).color(1, 1, 1, 1).texture(255, 0).endVertex();
			builder.position(1, 1, 0, 1).color(1, 1, 1, 1).texture(255, 255).endVertex();
			
			builder.position(1, 1, 0, 1).color(1, 1, 1, 1).texture(255, 255).endVertex();
			builder.position(-1, 1, 0, 1).color(1, 1, 1, 1).texture(0, 255).endVertex();
			builder.position(-1, -1, 0, 1).color(1, 1, 1, 1).texture(0, 0).endVertex();
			
			VertexArrayObject vao = new VertexArrayObject(builder.countVerticies());
			vao.bind();
			
			VertexBufferObject vbo = new VertexBufferObject(GL20.GL_ARRAY_BUFFER, builder.toArray());
			
			// I intentionally have this like this incase more elements need to be added
			// jit should optimize it
			int total = 4 + 4 + 2;
			int offset = 0;
			
			int posLoc = shaderProgram.getAttribLocation("position");
			GL20.glVertexAttribPointer(posLoc, 4, GL20.GL_FLOAT, false, total * 4, offset);
			GL20.glEnableVertexAttribArray(posLoc);
			
			int colorLoc = shaderProgram.getAttribLocation("col");
			GL20.glVertexAttribPointer(colorLoc, 4, GL20.GL_FLOAT, false, total * 4, (offset += 4) * 4);
			GL20.glEnableVertexAttribArray(colorLoc);
			
			int texLoc = shaderProgram.getAttribLocation("tex");
			GL20.glVertexAttribPointer(texLoc, 2, GL20.GL_FLOAT, false, total * 4, (offset += 4) * 4);
			GL20.glEnableVertexAttribArray(texLoc);
			vao.unbind();
			
			panelVAO = new VertexArrayHelper(vao, vbo);
		}
		window.releaseContext();
		
		PanelElement mainPanel = new PanelElement(0, 0, 255, 255, new Color(255, 255, 255, 255), ()->panelVAO, shaderProgram);
		while (isOpen[0]) {
			window.grabContext();
			Matrix4 matrix4 = Matrix4.identity();
			GL20.glViewport(-1, -1, 1, 1);
			
			GL20.glClearColor(1, 0, 1, 1);
			GL20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
//			matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(
//					Math.min(1f / window.getWidth() * 255, 1f / window.getHeight() * 255),
//					Math.min(1f / window.getWidth() * 255, 1f / window.getHeight() * 255),
//					1, 1
//			)));
			mainPanel.draw(matrix4);
			window.endFrame();
			window.releaseContext();
		}
		
		window.grabContext();
		panelVAO.delete();
		shaderProgram.deleteAttached();
		shaderProgram.delete();
		window.releaseContext();
		window.destroy();
		
		Runtime.getRuntime().exit(0);
	}
}
