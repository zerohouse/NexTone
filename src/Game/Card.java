package Game;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class Card extends ViewWrap {

	Effect effect;
	boolean hasmonster;
	boolean haseffect;
	Context context;

	ViewBinder cost, attack, vital, name, description, index;

	boolean selected;

	public Card(Context context, String name, String description, int cost,
			int attack, int vital, int index) {
		super(context);
		this.context = context;
		selected = false;
		
		
		
		
		hasmonster = true;
		this.attack = new ViewBinder(context, attack, this);
		this.cost = new ViewBinder(context, cost, this);
		this.vital = new ViewBinder(context, vital, this);
		this.name = new ViewBinder(context, name, this);
		this.description = new ViewBinder(context, description, this);
		this.index = new ViewBinder(context, index, this);

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSelect();
			}
		});
	}



	private void toggleSelect() {
		if (selected == false) {
			this.setBackgroundColor(Color.DKGRAY);
			selected = true;

			alert("선택");
		} else {
			this.setBackgroundColor(Color.WHITE);
			selected = false;

			alert("선택안함");
		}
	}


	public boolean selected(){
		return selected;
	}



	public int index() {
		return index.Int();		
	}



	public int cost() {
		return cost.Int();
	}
}
