package game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import components.ViewBinder;
import effects.Effect;

public class Hero extends RelativeLayout {

	Player player;
	Effect effect;
	int emptyDummy;
	ViewBinder dummysize;
	HeroCharacter hero;
	ManaStone mana;
	String name;
	Context context;
	RelativeLayout.LayoutParams params;
	boolean getWeapon;
	int maxvital;
	String resource;
	
	RelativeLayout.LayoutParams dummyparam;

	Hero(Context context, Player player, String res) {
		super(context);
		
		String[] tmp = res.split(",");
		resource = tmp[0];
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.height = Method.dpToPx(110);
		
		
		setBackgroundResource(Method.resId(resource+"back"));
		setLayoutParams(params);

		this.context = context;
		this.player = player;
		emptyDummy = 0;

		getWeapon = false;

		mana = new ManaStone(context, this);
		mana.setMana(0);
		RelativeLayout.LayoutParams manaparam = mana.getParams();

		manaparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		manaparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mana.setMaxmana(0);

		hero = new HeroCharacter(context, this, resource);
		


		dummysize = new ViewBinder(context, player.dummy.size(), this, true);
		dummyparam = dummysize.getParams();
		dummysize.setTextSize(15);

		
	}

	public void newTurn() {
		if (mana.maxmana() < 10)
			mana.maxmanaAdd(1);
		mana.setMana(mana.maxmana());
		dummysize.setInt(player.dummy.size());
		dummysize.setText(" Cards: " + player.dummy.size());
		hero.setOnAttackClickListener();
	}



	
	public void endTurn() {
		System.out.println();
		hero.setOnClickListener(null);
	}

	public void setByString(String set) {
		String[] setsplit = set.split(",");
		mana.setMana(Integer.parseInt(setsplit[0]));
		mana.setMaxmana(Integer.parseInt(setsplit[1]));
		hero.damage.setInt(Integer.parseInt(setsplit[3]));
		hero.vital.setInt(Integer.parseInt(setsplit[4]));
		dummysize.setText(" Cards: " + Integer.parseInt(setsplit[5]));

		if (Integer.parseInt(setsplit[2]) != 0) { 
			hero.defense.setInt(Integer.parseInt(setsplit[2]));
			return;
		}
		hero.defense.setText("");
	}

	@Override
	public String toString() {
		String heroState;
		heroState = mana.mana() + "," + mana.maxmana() + "," + hero.defense.Int()
				+ "," + hero.damage.Int() + "," + hero.vital.Int() + ","
				+ dummysize.Int();
		return heroState;
	}


	public void emptyDummy() {
		emptyDummy++;
		hero.vital.add(-emptyDummy);
		hero.defeatCheck();
	}

	public int index() {
		return -1;
	}



	public void deSelect() {
		hero.setBackgroundResource(Method.resId(resource));
	}
	
	public void setAttackBackground() {
		hero.setAttackBackground();
	}
	
	@SuppressLint("NewApi")
	@Override
	public float getX(){
		return hero.getX();
	}

	public void listenerNull() {
		hero.setOnClickListener(null);
	}

	public void setListener() {
		hero.setOnClickListener(Listeners.listener);
	}

	public void attackCheck() {
		hero.attackCheck();
	}
}