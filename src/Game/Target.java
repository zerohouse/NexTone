package Game;

public interface Target {

	public abstract int damage();

	public abstract void attacked(int damage);

	public abstract void attack(Target target);

}