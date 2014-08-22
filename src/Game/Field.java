package Game;

import java.util.ArrayList;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Field extends LinearLayout {
	
	ArrayList<Target> items;
	Context context;
	
	public Field(Context context) {
		super(context);
		this.context = context;
		items = new ArrayList<Target>();	
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setOrientation(HORIZONTAL);
		setLayoutParams(params);
	}

	
	
		


	public void remove(Monster monster) {
		items.remove(monster);
	}
	
	

}
