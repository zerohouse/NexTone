package game;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class Card extends RelativeLayout {

	Effect effect;
	boolean hasmonster;
	boolean haseffect;
	Context context;

	ViewBinder cost, attack, vital;
	String resource, name, description;
	int index;
	RelativeLayout.LayoutParams params;

	boolean selected;

	public Card(Context context, String name, String description, int cost,
			int attack, int vital, String resource, int index) {
		super(context);
		this.context = context;
		 
		params = Method.getParams();
		params.width = Method.dpToPx(70);
		params.height = Method.dpToPx(100);
		setLayoutParams(params);
		
		selected = false;

		hasmonster = true;
		this.resource = resource; 

		setBackgroundResource(Method.resId(resource + "c"));



		this.attack = new ViewBinder(context, attack, this);
		RelativeLayout.LayoutParams attackparam = this.attack.getParams();
		attackparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		attackparam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		

		this.cost = new ViewBinder(context, cost, this);
		RelativeLayout.LayoutParams costparam = this.cost.getParams();
		costparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		
		this.vital = new ViewBinder(context, vital, this);
		RelativeLayout.LayoutParams vitalparam = this.vital.getParams();
		vitalparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		vitalparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		this.name = name;
		this.description = description;
		this.index = index;

		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleSelect();
			}
		});
	}




	private void toggleSelect() {
		if (selected == false) {
			this.setBackgroundResource(Method.resId(resource + "clicked"));
			selected = true;
		} else {
			this.setBackgroundResource(Method.resId(resource + "c"));
			selected = false;
		}
	}

	public boolean selected() {
		return selected;
	}

	public int index() {
		return index;
	}

	public int cost() {
		return cost.Int();
	}

	public String resource() {
		return resource;
	}


	public int attack() {
		return attack.Int();
	}


	public int vital() {
		return vital.Int();
	}
}
