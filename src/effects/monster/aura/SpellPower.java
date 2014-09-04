package effects.monster.aura;

import effects.card.State;
import game.Method;
import game.Monster;

public class SpellPower implements AuraEffect {

	Monster monster;
	int amount;

	public SpellPower(Monster monster, int amount) {
		this.monster = monster;
		this.amount = amount;
	}

	@Override
	public void effectStart() {
		State.spellpower += 1;
		Method.alert("주문력+1");
	}

	@Override
	public void effectEnd() {
		State.spellpower -= 1;
		Method.alert("주문력-1");
	}

}
