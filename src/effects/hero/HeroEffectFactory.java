package effects.hero;

import game.Player;

public class HeroEffectFactory {

	public static HeroEffect makeHeroEffect(int type, Player player) {

		
		HeroEffect result;
		switch (type) {
		
		case 0:
			result = new SpawnRandomTotem(player); // 주술
			return result;
		
		case 1:
			result = new FireBall(player); // 법사
			return result;
		case 2:
			result = new Heal(player); // 사제
			return result;
		case 3:
			result = new GetWeapon(player); // 도적
			return result;
		case 4:
			result = new GetDefense(player); // 전사
			return result;
		case 5:
			result = new DrawCard(player); // 흑마
			return result;
		case 6:
			result = new SpawnNewMonster(player); // 성기사
			return result;
		case 7:
			result = new AttackEmenyHero(player); // 냥꾼
			return result;
		case 8:
			result = new DamgeAndDefense(player); // 드루
			return result;

			
		default:
			result = null;
			return result;
		}
	}
}
