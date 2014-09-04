package game;

import android.view.View;
import android.view.View.OnClickListener;

public class Listeners {

	static OnClickListener listener;
	
	public static void setAttacked() {
		listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Static.attacker != null)
					Static.attacker.attack((Target) v, false);
			}
		};
	}
	
	
	
	public static void setListener(OnClickListener listen){
		listener = listen;
	}



	public static void setAttack() {
		// TODO Auto-generated method stub
		
	}
}
