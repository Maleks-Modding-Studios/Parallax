package tfc.utils.rendering.font;

import earcut4j.Earcut;
import org.apache.fontbox.ttf.*;
import org.lwjgl.opengl.GL20;
import tfc.utils.rendering.general.VertexBuilder;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;

public class Font {
	private final TrueTypeFont font;
	private final KerningSubtable kerning;
	private final CmapSubtable cmap;
	private final GlyphTable glyphs;
	
	private final HashMap<Character, DrawableCharacter> characterMap = new HashMap<>();
	
	public Font(String path) throws IOException {
		font = new TTFParser().parse(new File(path));
		cmap = font.getUnicodeCmap();
		glyphs = font.getGlyph();
		KerningTable table = font.getKerning();
		if (table != null) kerning = table.getHorizontalKerningSubtable();
		else kerning = null;

//		GlyphData data = font.getGlyph().getGlyph(id);
		int vboId = GL20.glGenBuffers();
		int sindx = 0;
		ArrayList<Double> vertexCoords = new ArrayList<>();
		for (int i = font.getOS2Windows().getFirstCharIndex(); i < font.getOS2Windows().getLastCharIndex(); i++) {
			int id = cmap.getGlyphId(i);
			if (id != 0) {
				if (i < ' ') continue;
				if (i > '~') continue;
				ArrayList<Double> coords = new ArrayList<>();
				int startIndex = coords.size();
				generateChar(coords, (char) i, cmap, glyphs);
				int endIndex = coords.size() - startIndex;
				if (endIndex == 0) continue;
				System.out.print((char) i);
//				if ((char) i == ',') {
//					System.out.println(floats.length);
//					System.out.println(coords.size());
//				}
				startIndex /= 4;
				endIndex /= 4;
				
				float[] floats = new float[coords.size()];
				for (int i1 = 0; i1 < floats.length; i1++) {
					floats[i1] = coords.get(i1).floatValue();
					vertexCoords.add(coords.get(i1).doubleValue());
				}
//				{
//					double[] array1 = new double[coords.size() * 2];
//					for (int i1 = 0; i1 < coords.size(); i1++) array1[i1] = coords.get(i1);
//					List<Integer> array = Earcut.earcut(array1, null, 2);
//					int indx = 0;
//					for (int integer : array) {
//						vertexCoords.add(array1[integer * 2]);
//						vertexCoords.add(array1[integer * 2 + 1]);
//						floats[indx * 4] = (float) array1[integer * 2];
//						floats[indx * 4 + 1] = (float) array1[integer * 2 + 1];
//						floats[indx * 4 + 2] = 0;
//						floats[indx * 4 + 3] = 1;
//					}
//					sindx += array.size();
//					endIndex = array.size();
//				}
				characterMap.put((char) i, new DrawableCharacter(vboId, sindx, endIndex, floats));
				sindx += endIndex;
			}
		}
		
		VertexBuilder builder1 = new VertexBuilder(0);
		for (int i = 0; i < vertexCoords.size(); i += 4) {
			builder1.position(vertexCoords.get(i), vertexCoords.get(i + 1), 0, 1);
			builder1.endVertex();
		}
		float[] vertices = builder1.toArray();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void generateChar(ArrayList<Double> coordsOut, char c, CmapSubtable cmap, GlyphTable glyphs) throws IOException {
		int id = cmap.getGlyphId((int) c);
		if (id == 0) return;
		GlyphData data = glyphs.getGlyph(id);
		if (data == null) return;
		Path2D path = data.getPath();
		if (path == null) return;
		PathIterator iterator = path.getPathIterator(null);
		double[] coord = new double[6];
		double segMoveX = 0, segMoveY = 0;
		ArrayList<Double> coords = new ArrayList<>();
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
				if (type == PathIterator.SEG_CLOSE) {
					coord[0] -= 61.0;
					coord[1] += 99;
					coord[0] /= 480;
					coord[1] /= 480 * 2;
					coords.add(segMoveX);
					coords.add(segMoveY);
					// TODO
				} else if (type == PathIterator.SEG_MOVETO || type == PathIterator.SEG_LINETO) {
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
					System.out.println("Unknown segment type: " + type);
				}
			}
			iterator.next();
		}
		double[] array1 = new double[coords.size()];
		for (int i = 0; i < coords.size(); i++) array1[i] = coords.get(i);
		List<Integer> array = Earcut.earcut(array1, null, 2);
		VertexBuilder builder1 = new VertexBuilder(0);
		float[] out = new float[array.size() * 2];
		int i = 0;
		for (int integer : array) {
			builder1.position(array1[integer * 2], array1[integer * 2 + 1], 0, 1);
			builder1.endVertex();
		}
		for (float v : builder1.toArray()) {
			coordsOut.add((double) v);
		}
	}
	
	public DrawableCharacter get(char c) throws IOException {
		if (!characterMap.containsKey(c)) {
			DrawableCharacter character = new DrawableCharacter(font, c);
			characterMap.put(c, character);
		}
		return characterMap.get(c);
	}
	
	public int getSpacing(char left, char right) {
		if (kerning == null) return 0;
		return kerning.getKerning(left, right);
	}
	
	public void delete() {
		for (DrawableCharacter value : characterMap.values()) {
			value.delete();
		}
	}
	
	public double getWidth(String text) {
		return getWidth(text.toCharArray());
	}
	
	public double getWidth(char[] text) {
		double width = 0;
		try {
			for (char c : text) {
				int id = cmap.getGlyphId((int) c);
				if (id == 0) width += 10;
				GlyphData data = glyphs.getGlyph(id);
				if (data != null) {
					width += (data.getXMaximum() - data.getXMinimum());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return width;
	}
	
	public double getWidth(char c) throws IOException {
		int id = cmap.getGlyphId((int) c);
		if (id == 0) return 600;
		GlyphData data = glyphs.getGlyph(id);
		return font.getWidth(String.valueOf(c));
	}
}
