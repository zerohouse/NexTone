package effects.monster.excute;

import game.Monster;

public class CureTotem implements ExcuteEffect {

	Monster monster;
	public CureTotem(Monster monster) {
		this.monster = monster;

	}

	@Override
	public void run() {
		for (Monster monster : monster.field().monsters())
			monster.heal(1, false);
	}

}
