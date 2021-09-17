package tfc.utils.misc;

public class Stack<T> {
	private StackElement element;
	
	public void push() {
		element = new StackElement(element);
	}
	
	public void set(T value) {
		this.element.value = value;
	}
	
	public void pop() {
		element = element.previous;
	}
	
	public T get() {
		return element.value;
	}
	
	private class StackElement {
		private final StackElement previous;
		private T value;
		
		public StackElement(StackElement previous) {
			this.previous = previous;
		}
	}
}
