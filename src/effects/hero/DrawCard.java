package effects.hero;

import game.Player;

public class DrawCard implements HeroEffect {

	Player player; 
 
	public DrawCard(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {
		player.hero.mana.Add(-manacost, false);
		player.hero.hero().heal(-2, false, player.hero.hero());
		player.newCard();
		player.sendHeroState();
	}

}
