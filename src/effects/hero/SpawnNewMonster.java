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

		String monsterinfo = index + "," + 1 + "," + 1 + ",bat,0";
		Card card = new Card(player.context(), "박쥐;;bat;0;0;0;1;1", player.hand, index);
		Monster monster = new Monster(player.context(), card, monsterinfo,
				player.field, index, false);
		player.field.add(monster);
	}
}
