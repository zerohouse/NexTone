package effects.monster.aura;

import game.Monster;


public class AuraEffectFactory {

	public final static int ALL_ATTACK = 1;
	
	public static AuraEffect makeAuraEffect(int type, int amount, Monster monster) {

		
		AuraEffect result;
		switch (type) {
			
		default:
			result = null;
			return result;
		}
	}


}
