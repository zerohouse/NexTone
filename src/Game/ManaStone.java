package game;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mylikenews.nextoneandroid.R;

public class ManaStone extends LinearLayout {

	int mana;
	int maxmana;
	Hero hero;
	ArrayList<ImageView> manaimage;

	public ManaStone(Context context, Hero hero) {
		super(context);
		this.hero = hero;
		hero.addView(this);

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
		setLayoutParams(params);
		return params;
	}

}
