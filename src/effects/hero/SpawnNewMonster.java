package effects.hero;

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
		
		String monsterinfo = index + ","+1+","+1+",bat";
		Monster monster = new Monster(player.context(), monsterinfo, player.field, index, false);
		player.field.add(monster);
	}
}
