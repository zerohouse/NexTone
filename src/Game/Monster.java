package Game;

public class Monster implements Target {

	Field field;
	int damage, defauldamage;
	int vital, defaulvital;
	int maxvital, defaulmaxvital;
	Effect whendeath;
	Effect whenturn;
	
	Monster(Field field){
		this.field = field;
	}


	@Override
	public int damage() {
		return damage;
	}

	@Override
	public void attacked(int damage) {
		vital -= damage;
		if (vital < 1){
			field.remove(this);
		}
	}

	@Override
	public void attack(Target target) {
		vital -= target.damage();
		target.attacked(damage);
		if (vital < 1){
			field.remove(this);
		}
	}
}
