package com.mylikenews.nextoneandroid;

import game.Method;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import components.IconBinder;

import dek.Data;
import dek.Sql;

public class SelectHeroAbility extends Activity {

	Intent intent;
	Sql sql;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_hero);

		ArrayList<IconBinder> heros = new ArrayList<IconBinder>();
		IconBinder hero;
		sql = new Sql(this);

		OnClickListener listener;

		LinearLayout layout = (LinearLayout) findViewById(R.id.selecthero);
		for (int i = 0; i < 9; i++) {
			hero = new IconBinder(this);
			hero.setGravity(Gravity.CENTER);
			hero.setText(heroType(i)[0]);
			hero.setIconResource(Method.resId("heroability" + i));
			hero.setTextAppearance(R.style.myText);
			hero.addDescription(heroType(i)[1]);
			
			heros.add(hero);
			layout.addView(hero);
			hero.setId(i);
			intent = new Intent(this, MakeDek.class);
			listener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Data data = new Data(DekList.LastId() + 1, getIntent()
							.getExtras().getString("resource")
							+ ","
							+ v.getId(), ""); // 히어로 리소스 부분 수정해야댐..
					intent.putExtra("selected", data);
					sql.insert(data);
					finish();
					startActivity(intent);
				}
			};
			hero.setOnClickListener(listener);
		}

	}

	public static String[] heroType(int i) {
		String[] hero = new String[2];
		switch (i) {
		case 0:
			hero[0] = "토템소환";
			hero[1] = "랜덤 토템을 소환합니다.";
			break;
		case 1:
			hero[0] = "불";
			hero[1] = "선택한 대상에게 1의 피해를 줍니다.";
			break;
		case 2:
			hero[0] = "치유";
			hero[1] = "선택한 대상의 체력을 2 회복합니다.";
			break;
		case 3:
			hero[0] = "도깨비방망이";
			hero[1] = "1/2 도깨비방망이를 착용합니다.";
			break;
		case 4:
			hero[0] = "방어력";
			hero[1] = "방어력을 2 얻습니다.";
			
			break;
		case 5:
			hero[0] = "자학";
			hero[1] = "영웅의 생명력을 2 소모하여 카드를 한장 뽑습니다.";
			
			break;
		case 6:
			hero[0] = "박쥐소환";
			hero[1] = "1/1 박쥐를 소환합니다.";
			
			break;
		case 7:
			hero[0] = "저격";
			hero[1] = "상대의 영웅에게 2의 피해를 줍니다.";
			
			break;
		case 8:
			hero[0] = "신체강화";
			hero[1] = "영웅이 방어력과 공격력을 1씩 얻습니다.";
			
			break;
		}
		return hero;
	}
}
