package effects.hero;

import game.Player;

public class AttackEmenyHero implements HeroEffect {
	Player player;
	
	public AttackEmenyHero(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {

		player.hero.mana.Add(-manacost, false);
		player.enemy.hero.hero().heal(-2, false, player.hero.hero());
	}
}
