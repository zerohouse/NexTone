package effects.hero;

import game.Player;

public class GetDefense implements HeroEffect {
	Player player;
	
	public GetDefense(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {

		player.hero.getDefense(2, false, manacost);


	}
}
