package io.github.maleks.modding.studios.parallax;

import tfc.utils.rendering.general.Color;
import tfc.utils.rendering.ui.PanelElement;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.ShaderProgram;
import tfc.wrappers.opengl.Window;

public class MainPanel extends PanelElement {
	protected Window window;
	
	public MainPanel(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program, Window window) {
		super(startX, startY, endX, endY, color, drawFunc, program);
		this.window = window;
	}
	
	@Override
	public void draw(Matrix4 matrix4, Matrix4 baseMatrix) {
		endX = window.getWidth() * 2;
		endY = window.getHeight() * 2;
		matrix4 = matrix4.multiply(Matrix4.createTranslationMatrix(new Vector4(startX, startY, 0, 1)));
		matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(endX - startX, endY - startY, 1, 1)));
//		program.start();
//		program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getGreen() / 255f, color.getAlpha() / 255f);
//		program.uniformMatrix4("modelView", matrix4.toArray());
//		drawFunc.run();
//		program.finish();
		super.draw(matrix4, baseMatrix);
	}
}
