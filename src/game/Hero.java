package game;

import net.Sender;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import components.ImageButton;
import components.ViewBinder;
import effects.hero.HeroEffect;
import effects.hero.HeroEffectFactory;

public class Hero extends RelativeLayout {

	Player player;
	HeroEffect effect;
	int emptyDummy, herotype;
	ViewBinder dummysize;
	HeroCharacter hero;
	public ManaStone mana;
	String name;
	Context context;
	RelativeLayout.LayoutParams params;
	boolean getWeapon;
	int heroabilityuseable;
	String heroresource;

	OnClickListener herosability;
	ImageButton ability;

	RelativeLayout.LayoutParams dummyparam;

	Hero(Context context, Player player, String res) {
		super(context);

		String[] resourcesplit = res.split(",");
		heroresource = resourcesplit[0];
		herotype = Integer.parseInt(resourcesplit[1]);
		
		params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.height = Method.dpToPx(110);

		setBackgroundResource(Method.resId(heroresource + "back"));
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

		hero = new HeroCharacter(context, this, heroresource);
		addView(hero);

		dummysize = new ViewBinder(context, player.dummy.size(), this, true);
		dummyparam = dummysize.getParams();
		dummysize.setTextSize(15);

		heroabilityuseable = 0;

		ability = new ImageButton(context, Method.resId("heroability"+herotype),
				Method.resId("heroability"+herotype+"pressed"), "2");
		ability.getParams().width = Method.dpToPx(40);
		ability.getParams().height = Method.dpToPx(40);
		ability.getParams().leftMargin = Method.dpToPx(210);
		ability.getParams().topMargin = Method.dpToPx(50);
		addView(ability);

		effect = HeroEffectFactory.makeHeroEffect(herotype,
				this.player);

		herosability = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (heroabilityuseable == 0) {
					Method.alert("영웅능력은 한턴에 한번만 사용할 수 있습니다.");
					return;
				}

				if (!manaCheck(2)) {
					Method.alert("마나가 부족합니다.");
					return;
				}
				heroabilityuseable--;

				effect.run(2);
			}

		};
		ability.setOnClickListener(herosability);

	}

	public void newTurn() {

		heroabilityuseable = 1;

		if (mana.maxmana() < 10)
			mana.maxmanaAdd(1);
		mana.setMana(mana.maxmana());
		hero.newTurn();

		ability.setOnClickListener(herosability);
	}

	public void endTurn() {
		hero.endTurn();
		ability.setOnClickListener(null);
	}

	public void setByString(String set) {
		String[] setsplit = set.split(",");
		mana.setMana(Integer.parseInt(setsplit[0]));
		mana.setMaxmana(Integer.parseInt(setsplit[1]));
		dummysize.setText(" Cards: " + Integer.parseInt(setsplit[6]) + "/"
				+ Integer.parseInt(setsplit[5]));

		hero.setByString(Integer.parseInt(setsplit[2]) + ","
				+ Integer.parseInt(setsplit[3]) + ","
				+ Integer.parseInt(setsplit[4]));
		hero.vitalCheck();

	}

	@Override
	public String toString() {
		String heroState;
		heroState = mana.mana() + "," + mana.maxmana() + ","
				+ hero.toString() + "," + dummysize.Int() + ","
				+ player.hand.size();
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
		hero.setBackgroundResource(Method.resId(heroresource));
	}

	public void setAttackBackground() {
		hero.setAttackBackground();
	}

	@SuppressLint("NewApi")
	@Override
	public float getX() {
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

	public boolean manaCheck(int amount) {
		if (mana.mana() >= amount)
			return true;
		return false;
	}

	public void heroAbilityUseable() {
		heroabilityuseable++;
	}

	public Target hero() {
		return hero;
	}

	public void getWeapon(int damage, int vital, String resource,
			boolean sended, int manacost) {
		Weapon weapon = new Weapon(context, this, damage, vital, resource);
		hero.getWepon(weapon);
		mana.Add(-manacost, sended);
		this.addView(weapon);
		if (!sended)
			Sender.S("14 " + damage + "," + vital + "," + resource);
	}

	public void getDefense(int defense, boolean Sended, int manacost) {
		mana.Add(-manacost, Sended);
		hero.getDefense(defense);
		if (!Sended)
			Sender.S("15 " + player.me + "," + defense);
	}

	public void setDamage(int damage, boolean sended) {
		hero.damage.setInt(damage);
		if (!sended)
			Sender.S("18 " + player.me + "," + damage);
	}

	public void heroNewTurn() {
		hero.newTurn();
	}

}