package effects.monster.aura;

import game.Monster;


public class AuraEffectFactory {

	
	public static AuraEffect makeAuraEffect(int type, Monster monster) {

		
		AuraEffect result;
		switch (type) {
		
		case 1:
			result = new SpellPower(monster, 1); // 주문토템
			return result;
			
		default:
			result = null;
			return result;
		}
	}


}
