package net;

import game.Method;
import game.Player;
import game.Static;
import game.Target;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import animation.Attack;

import com.mylikenews.nextoneandroid.R;

public class NetGame extends AsyncTask<Void, Integer, Void> {

	Player player1, player2;
	Context context;
	LinearLayout container;
	String player1dek, player2dek, player2hero;
	boolean first;
	String ip;
	int port;
	RelativeLayout animate;

	public NetGame(Context context, LinearLayout container,
			RelativeLayout animate, String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.context = context;
		this.container = container;
		this.animate = animate;
		player1dek = "1x2,2x2,3x26";
	}

	public void Start(boolean first) {

		Attack.set(animate);

		container.removeAllViews();
		player1 = new Player(context, player1dek, 1, this);
		if (first) {
			Method.alert("게임을 시작합니다.");
			addPlayerCard(3);
			player1.first();
		} else {
			Method.alert("게임을 시작합니다.");
			addPlayerCard(4);
			player1.second();
		}
		this.first = first;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Sender.S("7 1@" + player1.heroString());
	}

	public void initSetting() {

		player2 = new Player(context, player2dek, 2, this);
		if (first) {
			player2.second();
		} else {
			player2.first();
		}

		player1.setEnemy(player2);
		player2.setEnemy(player1);

	}

	public void initView() {
		if (container.getChildCount() > 3) {
			return;
		}
		FrameLayout fieldarea = new FrameLayout(context);
		LinearLayout.LayoutParams fieldparam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);
		fieldarea.setLayoutParams(fieldparam);

		ImageView fieldback = new ImageView(context);
		fieldback.setLayoutParams(fieldparam);
		fieldback.setScaleType(ScaleType.FIT_XY);
		fieldback.setBackgroundResource(R.drawable.field);

		LinearLayout innerfieldarea = new LinearLayout(context);
		fieldarea.setLayoutParams(fieldparam);
		innerfieldarea.setOrientation(LinearLayout.VERTICAL);

		container.addView(fieldarea, 0);

		fieldarea.addView(fieldback);
		fieldarea.addView(innerfieldarea);

		innerfieldarea.addView(player2.field());
		innerfieldarea.addView(player1.field());

		player2.addHero();
		player1.addHero();
	}

	private void addPlayerCard(int size) {
		player1.firstSetting(size);
		container.addView(player1.hand());
	}

	String response;
	Sender sender;

	@Override
	protected Void doInBackground(Void... params) {
		try {
			Socket socket = new Socket(ip, port);
			sender = new Sender(socket);
			InputStream in = socket.getInputStream();
			DataInputStream datain = new DataInputStream(in);

			response = "";
			while (!response.equals("end")) {
				publishProgress(1);
				response = datain.readUTF();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Integer... values) {
		if (values[0] == 1)
			Do(response);
	}

	private void Do(String resString) {
		String[] response = resString.split(" ");
		try {
			int type = Integer.parseInt(response[0]);
			switch (type) {

			case 0: // 0번이 넘어오면 시작한다. (카드 바꾸기 화면 실행)
				if (Integer.parseInt(response[1]) == 1) {
					Start(true);
					return;
				}
				Start(false);
				break;

			case 3: // 3번은 상데의 덱정보를 가지고 온다.
					// 상대의 덱을 세팅.
				player2dek = response[1];
				initSetting();
				break;

			case 4: // 4번이 넘어오면 턴을 넘긴다.
				player2.endTurnByNet();
				player1.newTurn();
				break;

			case 5: // 맨처음 시작할때. 선공 플레이어가 세팅을 마쳤으면,
					// 게임을 시작하고, 아니면 카드 바꾸기 아이템에
					// 게임 시작 이벤트도 걸어준다.
				if (player1.done()) {
					initView();
					player1.newTurn();
					Sender.S("6 ");
					return;
				}

				player1.ChangeToStartTurn();
				break;

			case 6: // 처음 화면에 필드와 히어로를 추가한다.
				initView();

			case 7: // 7번은 영웅정보를 가지고 온다.
					// (이미 생성된) 영웅 인스턴스의 정보를 바꾼다.
				String res[] = response[1].split("@");
				if (res[0].equals("1")) {
					player2.hero.setByString(res[1]);
					return;
				}
				player1.hero.setByString(res[1]);
				break;

			case 8: // 8번은 생성할 몬스터의 정보를 가지고 온다.
					// 8번을 통해 필드에 상대의 몬스터를 생성한다.
				String[] mon = response[1].split("@");
				if (mon[0].equals("1")) {
					player2.field.addByString(mon[1], true);
					return;
				}
				player1.field.addByString(mon[1], true);

				break;

			case 9: // 9번은 공격정보를 가져온다.
					// 인덱스의 몬스터들끼리 공격을 주고 받는다.
				String[] attack = response[1].split(",");
				Target one,
				another;
				if (Integer.parseInt(attack[0]) == -1)
					one = player1.field.hero.hero();
				else
					one = player1.field.getByIndex(Integer.parseInt(attack[0]));

				if (Integer.parseInt(attack[1]) == -1)
					another = player2.field.hero.hero();
				else
					another = player2.field.getByIndex(Integer
							.parseInt(attack[1]));

				another.attack(one, true);
				break;

			case 10:
				Method.alert("상대방의 턴입니다."); // 턴알림
				break;

			case 11:
				Target attacker;
				int resint = Integer.parseInt(response[1]);

				player2.hero.deSelect();
				player2.field.othersNotAttack(); // 상대방의 필드에서 선택되면 attack이미지로 변경

				if (resint == -1)
					attacker = player2.hero.hero();
				else
					attacker = player2.field.getByIndex(resint);

				attacker.setAttackBackground();

				break;

			case 12:
				Static.Cancel(player2, false);

				break;

			case 13:
				String mana[] = response[1].split(",");
				if (Integer.parseInt(mana[0]) == 1) {
					player2.hero.mana.Add(Integer.parseInt(mana[1]), true);
					return;
				}
				player1.hero.mana.Add(Integer.parseInt(mana[1]), true);

				break;

			case 16:
				String heal[] = response[1].split(",");

				int player = Integer.parseInt(heal[0]);
				int index = Integer.parseInt(heal[1]);
				int healamount = Integer.parseInt(heal[2]);

				Target healtarget;

				if (player == 1) {
					if (index != -1)
						healtarget = player2.field.getByIndex(index);
					else
						healtarget = player2.hero.hero();
					healtarget.heal(healamount, true);
					return;
				}
				if (index != -1)
					healtarget = player1.field.getByIndex(index);
				else
					healtarget = player1.hero.hero();
				healtarget.heal(healamount, true);

				break;

			case 14:
				String weapon[] = response[1].split(",");
				int damage = Integer.parseInt(weapon[0]);
				int vital = Integer.parseInt(weapon[1]);
				String resource = weapon[2];
				player2.hero.getWeapon(damage, vital, resource, true, 0);
				break;

			case 15:
				String defense[] = response[1].split(",");
				if (Integer.parseInt(defense[0]) == 1) {
					player2.hero.getDefense(Integer.parseInt(defense[1]), true,
							0);
					return;
				}
				player1.hero.getDefense(Integer.parseInt(defense[1]), true, 0);
				break;
				
			case 18:
				String dam[] = response[1].split(",");
				if (Integer.parseInt(dam[0]) == 1) {
					player2.hero.setDamage(Integer.parseInt(dam[1]), true);
					return;
				}
				player1.hero.setDamage(Integer.parseInt(dam[1]), true);
				break;
				
			}
		} catch (Exception e) {
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		endGame();
	}

	private void endGame() {
		// TODO Auto-generated method stub

	}

	public ViewGroup container() {
		return container;
	}

}
