package tfc.utils.rendering.ui;

import io.github.maleks.modding.studios.parallax.Launch;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;
import tfc.utils.rendering.font.DrawableCharacter;
import tfc.utils.rendering.font.Font;
import tfc.utils.rendering.general.Color;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.ShaderProgram;

public class TextElement extends Element {
	private static final char empty;
	
	static {
		// this is redundant, it could just be `empty = 0;`
		char[] emptyChar = new char[1];
		empty = emptyChar[0];
		// if this gets thrown, I would be concerned
		if (empty != (char) 0) throw new RuntimeException("sir, your jvm be broken");
	}
	
	Font font;
	char[] text;
	
	public TextElement(double startX, double startY, double endX, double endY, Font font, String text) {
		super(startX, startY, endX, endY);
		this.font = font;
		this.text = text.toCharArray();
	}
	
	public TextElement(double startX, double startY, double endX, double endY, Font font, char[] text) {
		super(startX, startY, endX, endY);
		this.font = font;
		this.text = text;
	}
	
	@Override
	public void update(double pct) {
//		System.out.println();
		endX = startX + ((font.getWidth(text) / 600) * (getEndY() - getStartY()) * 1.25);
	}
	
	Color color = new Color(255, 255, 255, 255);
	ShaderProgram program = Launch.shaderProgram;
	
	@Override
	public void draw(Matrix4 thisMatrix, Matrix4 baseMatrix) {
		endY = 50;
		program.start();
		if (this.isFocused) {
			Color color = this.color.hueshift(-50);
			program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		} else {
			program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
		}
		{
			try {
				int currentVBOId = -1;
				thisMatrix = thisMatrix.multiply(
						Matrix4.createScaleMatrix(
								new Vector4(
										1f / Math.abs(startX - endX),
										1.75, 1, 1
								)
						)
				);
				thisMatrix = thisMatrix.multiply(
						Matrix4.createScaleMatrix(
								new Vector4(
										Math.abs(endY - startY),
										Math.abs(endY - startY) / 40f, 1, 1
								)
						)
				);
				char last = 0;
				for (char c : text) {
					DrawableCharacter character = font.get(c);
					if (last != 0) {
						thisMatrix = thisMatrix.multiply(
								Matrix4.createTranslationMatrix(
										new Vector4(font.getSpacing(last, c) + ((font.getWidth(c)) / 600), 0, 0, 1)
								)
						);
					}
					program.uniformMatrix4("modelView", thisMatrix.toArray());
					if (character.vboId != currentVBOId) {
						currentVBOId = character.vboId;
						GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, character.vboId);
						GL20.glVertexAttribPointer(0, 4, GL20.GL_FLOAT, false, 0, 0);
						GL20.glEnableVertexAttribArray(0);
						GL20.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
					}
					last = c;
					GL20.glDrawArrays(GL20.GL_TRIANGLES, character.startCount, character.vertexCount);
				}
			} catch (Throwable ignored) {
			}
		}
		program.finish();
		super.draw(thisMatrix, baseMatrix);
	}
	
	@Override
	public void onTyped(char c, int key) {
		super.onTyped(c, key);
		if (
				!(Launch.window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) ||
						Launch.window.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))
		) {
			c = Character.toLowerCase(c);
		}
		if (c < 256) {
			char[] newArray = new char[text.length + 1];
			System.arraycopy(text, 0, newArray, 0, text.length);
			newArray[newArray.length - 1] = c;
			text = newArray;
		} else if (c == GLFW.GLFW_KEY_BACKSPACE) {
			if (Launch.window.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
				System.out.println("bulk delete");
				// TODO
			}
			if (text.length >= 1) {
				char[] newArray = new char[text.length - 1];
				System.arraycopy(text, 0, newArray, 0, text.length - 1);
				text = newArray;
			}
		}
	}
}
