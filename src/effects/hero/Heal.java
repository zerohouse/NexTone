package effects.hero;

import android.view.View;
import android.view.View.OnClickListener;
import game.Listeners;
import game.Method;
import game.Player;
import game.Static;
import game.Target;

public class Heal implements HeroEffect {


	Player player;

	int cost;

	public Heal(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {

		cost = manacost;
		Method.alert("치료할 대상을 선택하세요.");

		OnClickListener heal = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Target selected = (Target) v;
				player.hero.mana.Add(-cost, false);
				selected.heal(2, false, player.hero.hero());
				Static.Cancel(player, false);
			}

		};

		Listeners.setListener(heal);

		player.endturn.setText("    취소");
		player.endturn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				player.hero.heroAbilityUseable();
				Static.Cancel(player, true);
			}

		});

		player.field.setListener();
		player.hero.setListener();
		player.enemy.field.setListener();
		player.enemy.hero.setListener();

	}
}
