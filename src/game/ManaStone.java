package game;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylikenews.nextoneandroid.R;

public class ManaStone extends LinearLayout {

	
	int mana;
	int maxmana;
	Hero hero;
	ArrayList<ImageView> manaimage;
	LinearLayout wrap;
	TextView textMana;
	String text;
	
	public ManaStone(Context context, Hero herocontainer) {
		super(context);
		this.hero = herocontainer;
		
		wrap = new LinearLayout(context);
		wrap.setOrientation(LinearLayout.VERTICAL);
		
		text = "";
		textMana = new TextView(context);
		textMana.setGravity(Gravity.RIGHT);
		textMana.setTextAppearance(context, R.style.myText);
		textMana.setTextSize(15);
		herocontainer.addView(wrap);
		wrap.addView(textMana);
		wrap.addView(this);
		setGravity(Gravity.RIGHT);
		manaimage = new ArrayList<ImageView>();

		for (int i = 0; i < 20; i++) { // 0~20은 마나이미지
			ImageView mana = new ImageView(context);
			mana.setBackgroundResource(R.drawable.mana1);
			manaimage.add(mana);
		}
		for (int i = 0; i < 10; i++) { // 21~30은 엠티 마나 이미지
			ImageView mana = new ImageView(context);
			mana.setBackgroundResource(R.drawable.mana2);
			manaimage.add(mana);
		}
	}
	
	private void textUpdate(){
		textMana.setText("Mana("+mana+"/"+maxmana+")");
	}

	public void setMana(int mana) {
		this.mana = mana;
		drawMana();
	}

	public void setMaxmana(int maxmana) {
		this.maxmana = maxmana;
		drawMana();
	}

	public int maxmana() {
		return maxmana;
	}

	public int mana() {
		return mana;
	}

	public void manaAdd(int amount) {
		mana += amount;
		drawMana();
	}

	public void maxmanaAdd(int amount) {
		maxmana += amount;
		drawMana();
	}

	private void drawMana() {
		removeAllViews();
		textUpdate();
		if (mana >= maxmana) {
			for (int i = 0; i < mana; i++) {
				addView(manaimage.get(i));
			}
			return;
		}

		for (int i = 0; i < maxmana - mana; i++) {
			addView(manaimage.get(20 + i));
		}
		if (mana == 0)
			return;
		for (int i = 0; i < mana; i++) {
			addView(manaimage.get(i), 0);
		}
		
	}

	public RelativeLayout.LayoutParams getParams() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		wrap.setLayoutParams(params);
		return params;
	}

}
