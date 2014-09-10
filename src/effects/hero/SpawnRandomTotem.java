package effects.hero;

import effects.monster.aura.AuraEffectFactory;
import effects.monster.excute.ExcuteEffectFactory;
import game.Card;
import game.Method;
import game.Monster;
import game.Player;
import game.Static;

import java.util.ArrayList;

public class SpawnRandomTotem implements HeroEffect {

	Player player;
	static ArrayList<Integer> totems = new ArrayList<Integer>();

	public SpawnRandomTotem(Player player) {
		this.player = player;
		totems.add(0);
		totems.add(1);
		totems.add(2);
		totems.add(3);
	}

	@Override
	public void run(int manacost) {
		if (totems.size() == 0) {
			Method.alert("이미 모든 종류의 토템을 사용중입니다.");
			return;
		}
		player.hero.mana.Add(-manacost, false);

		int totem = totems.get(Static.ramdom.nextInt(totems.size()));
		int index = Static.index();
		String monsterinfo;
		Monster monster;
		Card card;
		switch (totem) {
		case 0:
			totems.remove((Integer) 0);

			monsterinfo = index + "," + 1 + "," + 1 + ",totem,0";
			card = new Card(player.context(), "토템;;totem;0;0;0;1;1",
					player.hand, index);
			monster = new Monster(player.context(), card, monsterinfo,
					player.field, index, false);
			player.field.add(monster);
			monster.setDeathEffect(ExcuteEffectFactory.totemNumber(0));
			break;

		case 1:
			totems.remove((Integer) 1);

			monsterinfo = index + "," + 0 + "," + 2 + ",totemheal,0";
			card = new Card(player.context(), "치유토템;;totemheal;0;0;0;0;2",
					player.hand, index);

			monster = new Monster(player.context(), card, monsterinfo,
					player.field, index, false);
			player.field.add(monster);
			monster.setEndTurnEffect(ExcuteEffectFactory.makeExcuteEffect(1,
					monster)); // 힐능력 부여
			monster.setDeathEffect(ExcuteEffectFactory.totemNumber(1));
			break;

		case 2:
			totems.remove((Integer) 2);

			monsterinfo = index + "," + 0 + "," + 2 + ",totemspell,0";
			card = new Card(player.context(), "주문토템;;totemspell;0;0;0;0;2",
					player.hand, index);

			monster = new Monster(player.context(), card, monsterinfo,
					player.field, index, false);
			monster.setAuraEffect(AuraEffectFactory.makeAuraEffect(1, monster)); // 주문능력부여
			player.field.add(monster);
			monster.setDeathEffect(ExcuteEffectFactory.totemNumber(2));
			break;

		case 3:
			totems.remove((Integer) 3);

			monsterinfo = index + "," + 0 + "," + 2 + ",totemshield,0#1";
			card = new Card(player.context(), "수호토템;;totemshield;0;0#1;0;0;2",
					player.hand, index);

			monster = new Monster(player.context(), card, monsterinfo,
					player.field, index, false);
			player.field.add(monster);
			monster.setDeathEffect(ExcuteEffectFactory.totemNumber(3));
			break;
		}
	}

	public static void retoreTotem(int num) {
		totems.add(num);
	}
}
