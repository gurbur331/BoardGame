package board;

public class Blank <T>{
	private final int index;
	private T data;
	
	public Blank(int index, T data) {
		this.index = index;
		this.data = data;
	}
	
	public T getData() { return data; }
	public int getIndex() { return index; }
	
	public T popData() {
		T tmp = data;
		data = null;
		return tmp;
	}
	
	public void setData(T data) { this.data = data; }
	
	public boolean isEmpty() {
		try {
			if(data.equals(null)) return true;
			else return false;
		} catch(NullPointerException ex) {
			return true;
		}
	}
	
	@Override
	public Blank<T> clone() {
		Blank<T> temp = new Blank<T>(this.getIndex(), this.getData());
		return temp;
	}
}
