package Game;

import java.util.ArrayList;

import android.content.Context;
import android.widget.RelativeLayout;

public class Field extends RelativeLayout {
	
	ArrayList<Target> items;
	Context context;
	
	public Field(Context context) {
		super(context);
		this.context = context;
		items = new ArrayList<Target>();	
	}

	
	
		


	public void remove(Monster monster) {
		items.remove(monster);
	}
	
	

}
