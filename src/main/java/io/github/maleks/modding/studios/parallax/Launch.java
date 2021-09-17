package io.github.maleks.modding.studios.parallax;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL20;
import tfc.utils.misc.Rectangle2D;
import tfc.utils.rendering.font.Font;
import tfc.utils.rendering.general.Color;
import tfc.utils.rendering.gl.Stencil;
import tfc.utils.rendering.ui.PanelElement;
import tfc.utils.rendering.ui.TextElement;
import tfc.utils.rendering.ui.VerticallyDistributedPanelElement;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.Shader;
import tfc.wrappers.opengl.ShaderProgram;
import tfc.wrappers.opengl.Window;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Launch {
	public static Window window;
	public static ShaderProgram shaderProgram;
	public static int buffer;
	public static PanelElement panel;
	
	public static Stencil stencil = new Stencil();
	
	protected static boolean[] lastFrameMouseStates = new boolean[]{false, false, false};
	
	public static void main(String[] args) {
		GLFW.glfwInit();
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		window = new Window();
		window.setVisible(true);
		boolean[] isOpen = new boolean[]{true};
		window.setCloseListener((w) -> isOpen[0] = false);
		
		window.grabContext();
		window.allowVsync(false);
		
		{
			Shader frag = new Shader(GL20.GL_FRAGMENT_SHADER,
					"#version 330 core\n" +
							"\n" +
							"out vec4 FragColor;\n" +
							"\n" +
							"uniform vec4 colMultiplier;\n" +
							"\n" +
							"void main() {\n" +
							"\tFragColor = vec4(1) * colMultiplier;\n" +
							"}\n"
			);
			Shader vert = new Shader(GL20.GL_VERTEX_SHADER,
					"#version 330 core\n" +
							"in vec4 position;\n" +
							"\n" +
							"uniform mat4 modelView;\n" +
							"\n" +
							"void main() {\n" +
							"\tgl_Position = position * modelView;\n" +
							"}\n"
			);
			shaderProgram = new ShaderProgram(new Shader[]{frag, vert});
			shaderProgram.link();
			shaderProgram.start();
			shaderProgram.getUniformLocation("modelView");
			shaderProgram.getUniformLocation("texture");
			shaderProgram.getUniformLocation("colMultiplier");
			shaderProgram.uniformVec4f("colMultiplier", 255, 255, 255, 255);
			shaderProgram.finish();
		}
		
		buffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, buffer);
		glBufferData(GL_ARRAY_BUFFER, new float[]{
				0.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
				1.0f, 0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f, 1.0f,
		}, GL_STATIC_DRAW);
//		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
//		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		Font font = null;
		try {
			font = new Font("what/JetBrainsMonoNL-Regular.ttf");
		} catch (Throwable ignored) {
			ignored.printStackTrace();
		}
		
		window.releaseContext();
		
		panel = new MainPanel(0, 0, 255, 255, new Color(225, 225, 255, 255).darker(0.3f, 16f), () -> {
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
			glEnableVertexAttribArray(0);
			glDrawArrays(GL_TRIANGLES, 0, 6);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}, shaderProgram, window);
		
		window.setScrollListener((window1, xoffset, yoffset) -> {
			panel.onScroll(window.getMouseX(), window.getMouseY(), 0, 0, xoffset, yoffset, window);
		});
		
		window.setKeyListener((window1, key, scan, action, mods) -> {
			if (action == 2 || action == 1) {
				panel.onTyped((char) key, key);
			}
		});
		
		VerticallyDistributedPanelElement panel1 = (VerticallyDistributedPanelElement) new VerticallyDistributedPanelElement(
				0, 0, 340, 255,
				new Color(225, 225, 255).darker(0.25f, 16f),
				() -> {
					glBindBuffer(GL_ARRAY_BUFFER, buffer);
					glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
					glEnableVertexAttribArray(0);
					glDrawArrays(GL_TRIANGLES, 0, 6);
					glBindBuffer(GL_ARRAY_BUFFER, 0);
				}, shaderProgram, 40
		).setFillY(true);
		for (int i = 0; i < 51; i++) {
			PanelElement element = new PanelElement(
					8, 0, 300 - 7, 255,
					new Color(225, 225, 255).darker(0.5f, 16f),
					() -> {
						glBindBuffer(GL_ARRAY_BUFFER, buffer);
						glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
						glEnableVertexAttribArray(0);
						glDrawArrays(GL_TRIANGLES, 0, 6);
						glBindBuffer(GL_ARRAY_BUFFER, 0);
					}, shaderProgram
			);
			panel1.addChild(element);
		}
		panel.addChild(panel1);
		
		TextElement textElement = new TextElement(0, 0, 255, 255, font, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		panel1.addChild(textElement);
		
		while (isOpen[0]) {
			mainLoop();
			try {
				// this is really just here to prevent java from causing the fans to spin up
				Thread.sleep(0, 1);
			} catch (Throwable ignored) {
			}
		}
		
		window.grabContext();
		shaderProgram.delete();
		shaderProgram.deleteAttached();
		GL20.glDeleteBuffers(buffer);
		window.releaseContext();
		
		font.delete();
		
		Runtime.getRuntime().exit(0);
	}
	
	private static double endLastFrame = System.nanoTime();
	
	private static void mainLoop() {
//		long startTime = System.nanoTime();
		stencil.set(new Rectangle2D(0, 0, window.getWidth(), window.getHeight()));
		
		window.grabContext();
		window.startFrame();
		Matrix4 matrix4 = Matrix4.identity();
		glViewport(-1, -1, window.getWidth() + 1, window.getHeight() + 1);
		
		GL20.glClearColor(0, 0, 0, 1);
		GL20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		matrix4 = matrix4.multiply(Matrix4.createTranslationMatrix(new Vector4(-1, -1, 0, 1)));
		matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(
				1f / window.getWidth(),
				1f / window.getHeight(),
				1, 1
		)));
		
		window.setCursor(GLFW.GLFW_ARROW_CURSOR);
		panel.update(24 / ((System.nanoTime() - endLastFrame) * 0.001));
		panel.onHovered(window.getMouseX(), window.getMouseY(), 0, 0, window);
		if (window.isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			if (!lastFrameMouseStates[0]) {
				panel.onClicked(window.getMouseX(), window.getMouseY(), 0, 0, GLFW.GLFW_MOUSE_BUTTON_LEFT);
				lastFrameMouseStates[0] = true;
			}
		} else {
			lastFrameMouseStates[0] = false;
		}
		panel.onTick(window.getMouseX(), window.getMouseY(), 0, 0, window);
		
		panel.draw(matrix4, matrix4);
//		System.out.println(1.0 / ((System.nanoTime() - startTime) / 1000000000d));
		
//		shaderProgram.start();
//		shaderProgram.uniformMatrix4("modelView", matrix4.toArray());
//		shaderProgram.uniformVec4f("colMultiplier", 1, 0.5f, 1, 1);
//		glBindBuffer(GL_ARRAY_BUFFER, buffer);
//		glDrawArrays(GL_TRIANGLES, 0, 6);
//		glBindBuffer(GL_ARRAY_BUFFER, 0);
//		shaderProgram.finish();
//		mainPanel.draw(matrix4);
		window.endFrame();
		window.releaseContext();
		
		endLastFrame = System.nanoTime();
	}
}
