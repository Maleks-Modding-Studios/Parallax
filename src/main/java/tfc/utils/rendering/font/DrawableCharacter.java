package tfc.utils.rendering.font;

import earcut4j.Earcut;
import org.apache.fontbox.ttf.GlyphData;
import org.apache.fontbox.ttf.TrueTypeFont;
import tfc.utils.rendering.general.VertexBuilder;

import java.awt.geom.PathIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class DrawableCharacter {
	public final int vboId;
	public final int startCount;
	public final int vertexCount;
	private final float[] vertices;
	
	public DrawableCharacter(int vboId, int startCount, int vertexCount, float[] vertices) {
		this.vboId = vboId;
		this.startCount = startCount;
		this.vertexCount = vertexCount;
		this.vertices = vertices;
	}
	
	public DrawableCharacter(TrueTypeFont font, char character) throws IOException {
		startCount = 0;
		
		int id = font.getUnicodeCmap().getGlyphId((int) character);
		GlyphData data = font.getGlyph().getGlyph(id);
		PathIterator iterator = data.getPath().getPathIterator(null);
		double[] coord = new double[6];
		ArrayList<Double> coords = new ArrayList<>();
		double segMoveX = 0, segMoveY = 0;
		while (!iterator.isDone()) {
			int type = -1;
			try {
				type = iterator.currentSegment(coord);
			} catch (Throwable ignored) {
				ignored.printStackTrace();
			}
			if (type == PathIterator.SEG_QUADTO) {
				coord[0] -= 61.0;
				coord[1] += 99;
				coord[2] -= 61.0;
				coord[3] += 99;
				coord[0] /= 480;
				coord[1] /= 480 * 2;
				coord[2] /= 480;
				coord[3] /= 480 * 2;
				
				for (int i = 0; i < 8; i++) {
					double[] out = PointInterpolator.QuadSegment.interp(
							i / 8d,
							coords.get(coords.size() - 2), coords.get(coords.size() - 1),
							coord[0],
							coord[1],
							coord[2],
							coord[3]
					);
					
					coords.add(out[0]);
					coords.add(out[1]);
				}
			} else if (type == PathIterator.SEG_CUBICTO) {
				coord[0] -= 61.0;
				coord[1] += 99;
				coord[2] -= 61.0;
				coord[3] += 99;
				coord[4] -= 61.0;
				coord[5] += 99;
				
				coord[0] /= 480;
				coord[1] /= 480 * 2;
				coord[2] /= 480;
				coord[3] /= 480 * 2;
				coord[4] /= 480;
				coord[5] /= 480 * 2;
				
				for (int i = 0; i < 8; i++) {
					double[] out = PointInterpolator.CubicSegment.interp(
							i / 8d,
							coords.get(coords.size() - 2), coords.get(coords.size() - 1),
							coord[0],
							coord[1],
							coord[2],
							coord[3],
							coord[4],
							coord[5]
					);
					
					coords.add(out[0]);
					coords.add(out[1]);
				}
			} else {
				if (type != PathIterator.SEG_CLOSE) {
					coord[0] -= 61.0;
					coord[1] += 99;
					coord[0] /= 480;
					coord[1] /= 480 * 2;
					coords.add(coord[0]);
					coords.add(coord[1]);
					if (type == PathIterator.SEG_MOVETO) {
						segMoveX = coord[0];
						segMoveY = coord[1];
					}
				} else {
					coord[0] -= 61.0;
					coord[1] += 99;
					coord[0] /= 480;
					coord[1] /= 480 * 2;
					coords.add(segMoveX);
					coords.add(segMoveY);
					// TODO
				}
			}
			iterator.next();
		}
		double[] array1 = new double[coords.size()];
		for (int i = 0; i < coords.size(); i++) array1[i] = coords.get(i);
		List<Integer> array = Earcut.earcut(array1, null, 2);
		VertexBuilder builder1 = new VertexBuilder(0);
		for (int integer : array) {
			builder1.position(array1[integer * 2], array1[integer * 2 + 1], 0, 1);
			builder1.endVertex();
		}
		vertices = builder1.toArray();
		vertexCount = builder1.countVerticies();
		
		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void delete() {
		glDeleteBuffers(vboId);
	}
}
