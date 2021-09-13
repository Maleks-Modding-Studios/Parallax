package tfc.utils.rendering.ui;

import tfc.utils.rendering.general.Color;
import tfc.utils.rendering.gl.VertexArrayHelper;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.ShaderProgram;

import java.util.function.Supplier;

public class PanelElement extends Element {
	protected Color color;
	protected Supplier<VertexArrayHelper> quadSupplier;
	protected ShaderProgram program;
	
	public PanelElement(double startX, double startY, double endX, double endY, Color color, Supplier<VertexArrayHelper> quadSupplier, ShaderProgram program) {
		super(startX, startY, endX, endY);
		this.color = color;
		this.quadSupplier = quadSupplier;
		this.program = program;
	}
	
	@Override
	public void draw(Matrix4 matrix4) {
		Matrix4 oldMatrix = matrix4;
//		matrix4 = matrix4.multiply(Matrix4.createTranslationMatrix(new Vector4(startX, startY, 0, 0)));
//		matrix4 = matrix4.multiply(Matrix4.createScaleMatrix(new Vector4(endX - startX, endY - startY, 1, 1)));
		program.start();
		program.uniformVec4f("colMultiplier", color.getRed() / 255f, color.getGreen() / 255f, color.getGreen() / 255f, color.getAlpha() / 255f);
		program.uniformMatrix4("modelView", matrix4.toArray());
		quadSupplier.get().draw(3);
		program.finish();
		super.draw(oldMatrix);
	}
}
