package game;

public interface Target {

	public abstract int damage();

	public abstract void attacked(int damage);

	public abstract void attack(Target target);
	
	public abstract int index();

	public abstract void attackOrder(Target another);

}