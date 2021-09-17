package tfc.utils.rendering.gl;

import io.github.maleks.modding.studios.parallax.Launch;
import org.lwjgl.opengl.GL11;
import tfc.utils.misc.Rectangle2D;
import tfc.utils.misc.Stack;
import tfc.utils.vecmath.Matrix4;
import tfc.utils.vecmath.Vector4;
import tfc.wrappers.opengl.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Stencil {
	private Stack<Rectangle2D> stencilSteps = new Stack<>();
	
	public void set(Rectangle2D rect) {
		stencilSteps = new Stack<>();
		stencilSteps.push();
		stencilSteps.set(rect);
	}
	
	public void push(Rectangle2D rectangle2D) {
		if (stencilSteps.get().intersects(rectangle2D)) {
			stencilSteps.push();
			stencilSteps.set(rectangle2D.intersection(stencilSteps.get()));
		} else {
			stencilSteps.push();
			stencilSteps.set(null);
		}
	}
	
	public void pop() {
		stencilSteps.pop();
	}
	
	// https://gitlab.com/Spectre0987/TardisMod-1-14/-/blob/1.16/src/main/java/net/tardis/mod/client/renderers/boti/BOTIRenderer.java
	public void setupStencil() {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		
		// Always write to stencil buffer
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		GL11.glStencilMask(0xFF);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		
		ShaderProgram program = Launch.shaderProgram;
		program.start();
		program.uniformVec4f("colMultiplier", 1, 1, 1, 1);
		Matrix4 matrix = Matrix4.identity();
		Rectangle2D rect = stencilSteps.get();
		matrix = matrix.multiply(Matrix4.createTranslationMatrix(new Vector4(rect.minX, rect.minY, 0, 1)));
		matrix = matrix.multiply(Matrix4.createTranslationMatrix(new Vector4(rect.maxX - rect.minX, rect.maxY - rect.minY, 1, 1)));
		program.uniformMatrix4("modelView", matrix.toArray());
		glBindBuffer(GL_ARRAY_BUFFER, 1);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		program.finish();
		
		// Only pass stencil test if equal to 1(So only if rendered before)
		GL11.glStencilMask(0x00);
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
	}
	
	// https://gitlab.com/Spectre0987/TardisMod-1-14/-/blob/1.16/src/main/java/net/tardis/mod/client/renderers/boti/BOTIRenderer.java
	public void clearStencil() {
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
		
		GL11.glColorMask(false, false, false, false);
		ShaderProgram program = Launch.shaderProgram;
		program.start();
		program.uniformVec4f("colMultiplier", 1, 1, 1, 1);
		Matrix4 matrix = Matrix4.identity();
		program.uniformMatrix4("modelView", matrix.toArray());
		glBindBuffer(GL_ARRAY_BUFFER, 1);
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		program.finish();
		
		//Set things back
		GL11.glColorMask(true, true, true, true);
	}
}
