package Game;

public enum Value {
	START(0), FIRST(1), SECOND(2), DEKSTRING(3), YOURTURN(4);
	
	private int value;
	
	private Value(int val){
		value = val;
	}
	
	public int get(){
		return value;
	}
	
}
