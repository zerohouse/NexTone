package game;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.Sender;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;

public class NetGame extends AsyncTask<Void, Integer, Void> {

	Player player1, player2;
	Context context;
	LinearLayout container;
	String player1dek, player2dek, player2hero;
	boolean first;
	String ip;
	int port;

	public NetGame(Context context, LinearLayout container, String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.context = context;
		this.container = container;
		player1dek = "1x2,2x2,3x26";
	}

	public void Start(boolean first) {

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
		Sender.S("7 1@" + player1.hero.getString());
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

		container.addView(player1.field(), 0);
		container.addView(player2.field(), 0);
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
				player1.newTurn();
				break;

			case 5: // 맨처음 시작할때. 선공 플레이어가 세팅을 마쳤으면,
					// 게임을 시작하고, 아니면 카드 바꾸기 아이템에
					// 게임 시작 이벤트도 걸어준다.
				if (player1.done()) {
					player1.newTurn();
					return;
				}
				player1.ChangeToStartTurn();
				break;

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
					player2.field.add(mon[1]);
					return;
				}
				player1.field.add(mon[1]);

				break;
 
			case 9: // 9번은 공격정보를 가져온다.
					// 인덱스의 몬스터들끼리 공격을 주고 받는다.
				String[] attack = response[1].split(",");
				Target one,
				another;
				if (Integer.parseInt(attack[0]) != -1) {
					one = player1.field.get(Integer.parseInt(attack[0]));
				} else {
					one = player1.field.hero;
				}
				another = player2.field.get(Integer.parseInt(attack[1]));
				one.attackOrder(another);
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

}
