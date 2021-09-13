package tfc.utils.rendering.general;

import java.util.ArrayList;
import java.util.List;

public class VertexBuilder {
	List<Double> vertexList;
	int index = 0;
	
	public VertexBuilder(int initialAllocation) {
		// TODO: make this useful
		vertexList = new ArrayList<>(initialAllocation);
	}
	
	public VertexBuilder position(double x, double y, double z, double w) {
		vertexList.add(x);
		vertexList.add(y);
		vertexList.add(z);
		vertexList.add(w);
		return this;
	}
	
	public VertexBuilder color(int r, int g, int b, int a) {
		vertexList.add(r / 255.0);
		vertexList.add(g / 255.0);
		vertexList.add(b / 255.0);
		vertexList.add(a / 255.0);
		return this;
	}
	
	public void endVertex() {
		index++;
	}
	
	public VertexBuilder texture(int u, int v) {
		vertexList.add(u / 255.0);
		vertexList.add(v / 255.0);
		return this;
	}
	
	public int countVerticies() {
		return index;
	}
	
	public float[] toArray() {
		float[] array = new float[vertexList.size()];
		for (int i = 0; i < array.length; i++) array[i] = vertexList.get(i).floatValue();
		return array;
	}
}
