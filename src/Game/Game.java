package Game;

import android.content.Context;
import android.widget.LinearLayout;

public class Game {

	Player player1, player2;
	Context context;
	LinearLayout container;

	public Game(Context context, LinearLayout container) {
		this.context = context;
		this.container = container;
	}
	
	public void Start(){
		String dekstring = "1x2 2x2 3x26";
		player1 = new Player(context, dekstring);
		player1.firstSetting(5);
	}
	
	

}

