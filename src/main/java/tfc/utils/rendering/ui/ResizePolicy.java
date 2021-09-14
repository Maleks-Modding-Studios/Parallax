package tfc.utils.rendering.ui;

public class ResizePolicy {
	public final boolean canResizeLeft;
	public final boolean canResizeRight;
	public final boolean canResizeTop;
	public final boolean canResizeBottom;
	
	public ResizePolicy(boolean canResizeLeft, boolean canResizeRight, boolean canResizeTop, boolean canResizeBottom) {
		this.canResizeLeft = canResizeLeft;
		this.canResizeRight = canResizeRight;
		this.canResizeTop = canResizeTop;
		this.canResizeBottom = canResizeBottom;
	}
}
