package effects.hero;

import game.Player;

public class DamgeAndDefense implements HeroEffect {
	Player player;
	
	public DamgeAndDefense(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {

		player.hero.getDefense(1, false, manacost);
		player.hero.setDamage(1, false);
		player.hero.heroNewTurn();
	}
}

