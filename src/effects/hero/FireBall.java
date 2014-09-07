package effects.hero;

import game.Listeners;
import game.Method;
import game.Player;
import game.Static;
import game.Target;
import android.view.View;
import android.view.View.OnClickListener;

public class FireBall implements HeroEffect {


	Player player;

	int cost;

	public FireBall(Player player) {
		this.player = player;
	}

	@Override
	public void run(int manacost) {

		cost = manacost;
		Method.alert("선택한 대상에게 피해를 1 줍니다.");

		OnClickListener heal = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Target selected = (Target) v;
				player.hero.mana.Add(-cost, false);
				selected.heal(-1, false, player.hero.hero());
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
