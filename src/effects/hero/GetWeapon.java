package effects.hero;

import game.Player;

public class GetWeapon implements HeroEffect {

	Player player;

	public GetWeapon(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {
		player.hero.getWeapon(1, 2, "heroability3", false, manacost);
	}
}
