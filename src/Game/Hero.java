package Game;

public class Hero implements Target {

	Player player;
	int vital;
	int defense;
	int damage;
	
	Hero(Player player){
		this.player = player;
	}
	
	@Override
	public void attacked(int damage) {
		vital -= damage;
		if (vital < 1){
			player.defeat();
		}
	}

	@Override
	public void attack(Target target) {
		vital -= target.damage();
		target.attacked(damage);
		if (vital < 1){
			player.defeat();
		}
	}

	@Override
	public int damage() {
		return 0;
	}

}
