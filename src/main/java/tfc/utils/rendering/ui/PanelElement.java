package tfc.utils.rendering.ui;

import tfc.utils.rendering.general.Color;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.ShaderProgram;

public class PanelElement extends Element {
	protected Color color;
	protected Runnable drawFunc;
	protected ShaderProgram program;
	
	public PanelElement(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program) {
		super(startX, startY, endX, endY);
		this.color = color;
		this.drawFunc = drawFunc;
		this.program = program;
	}
	
	@Override
	public void draw(Matrix4 matrix4) {
		Matrix4 oldMatrix = matrix4;
		matrix4 = matrix4.multiply(Matrix4.createTranslationMatrix(new Vector4(startX, startY, 0, 1)));
		matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(endX - startX, endY - startY, 1, 1)));
		program.start();
		program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getGreen() / 255f, color.getAlpha() / 255f);
		program.uniformMatrix4("modelView", matrix4.toArray());
		drawFunc.run();
		program.finish();
		super.draw(oldMatrix);
	}
}
