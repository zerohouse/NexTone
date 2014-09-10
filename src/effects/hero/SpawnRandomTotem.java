package effects.hero;

import effects.monster.aura.AuraEffectFactory;
import effects.monster.excute.ExcuteEffectFactory;
import game.Monster;
import game.Player;
import game.Static;

public class SpawnRandomTotem implements HeroEffect {

	Player player;

	public SpawnRandomTotem(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {
		player.hero.mana.Add(-manacost, false);

		int totem = Static.ramdom.nextInt(4);
		int index = Static.index();
		String monsterinfo;
		Monster monster;

		switch (totem) {
		case 0:
			monsterinfo = index + "," + 1 + "," + 1 + ",totem,0";
			monster = new Monster(player.context(), monsterinfo, player.field,
					index, false);
			player.field.add(monster);
			break;

		case 1:
			monsterinfo = index + "," + 0 + "," + 2 + ",totemheal,0";
			monster = new Monster(player.context(), monsterinfo, player.field,
					index, false);
			player.field.add(monster);
			monster.setEndTurnEffect(ExcuteEffectFactory.makeExcuteEffect(1, monster)); // 힐능력 부여
			
			break;
			
		case 2:
			monsterinfo = index + "," + 0 + "," + 2 + ",totemspell,0";
			monster = new Monster(player.context(), monsterinfo, player.field,
					index, false);
			monster.setAuraEffect(AuraEffectFactory.makeAuraEffect(1, monster)); // 주문능력부여
			player.field.add(monster);
			break;
			
		case 3:
			monsterinfo = index + "," + 0 + "," + 2 + ",totemshield,0#1";
			monster = new Monster(player.context(), monsterinfo, player.field,
					index, false);
			player.field.add(monster);
			break;
		}
	}
}
