package effects.monster.excute;

import game.Monster;


public class ExcuteEffectFactory {

	
	public static ExcuteEffect makeExcuteEffect(int type, Monster monster) {

		
		ExcuteEffect result;
		switch (type) {
		
		case 1:
			result = new CureTotem(monster); // 힐링 템
			return result;
			
		default:
			result = null;
			return result;
		}
	}
	

}
