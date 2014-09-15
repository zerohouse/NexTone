package effects.hero;

import game.Card;
import game.Monster;
import game.Player;
import game.Static;

public class SpawnNewMonster implements HeroEffect {

	Player player;

	public SpawnNewMonster(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {
		player.hero.mana.Add(-manacost, false);

		int index = Static.index();
		Card card = new Card(player.context(), player.getCardStringById(-1),
				index, -1);
		Monster monster = new Monster(player.context(), card, player.field,
				false);
		player.field.add(monster);
	}
}
