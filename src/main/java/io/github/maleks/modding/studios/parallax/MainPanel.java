package io.github.maleks.modding.studios.parallax;

import tfc.utils.rendering.general.Color;
import tfc.utils.rendering.ui.PanelElement;
import tfc.utils.vecmath.Matrix4;
import tfc.wrappers.opengl.ShaderProgram;
import tfc.wrappers.opengl.Window;

public class MainPanel extends PanelElement {
	protected Window window;
	
	public MainPanel(double startX, double startY, double endX, double endY, Color color, Runnable drawFunc, ShaderProgram program, Window window) {
		super(startX, startY, endX, endY, color, drawFunc, program);
		this.window = window;
	}
	
	@Override
	public void draw(Matrix4 matrix4) {
		endX = window.getWidth() * 2;
		endY = window.getHeight() * 2;
		super.draw(matrix4);
	}
}
