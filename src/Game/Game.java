package Game;

import android.content.Context;
import android.widget.RelativeLayout;

public class Game {

	Player player1, player2;
	Context context;
	RelativeLayout container;

	public Game(Context context, RelativeLayout container) {
		this.context = context;
		this.container = container;
	}
	
	public void Start(){
		String dekstring = "1x2 2x2 3x26";
		player1 = new Player(context, container, dekstring);
		player1.firstSetting(5);
	}
	
	

}

