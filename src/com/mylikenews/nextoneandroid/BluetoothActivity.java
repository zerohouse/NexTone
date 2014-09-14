package com.mylikenews.nextoneandroid;

import game.Card;
import game.Game;
import game.Method;
import game.Player;
import game.Static;
import game.Target;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.Sender;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import animation.Ani;
import animation.Attack;
import animation.Helper;

public class BluetoothActivity extends Activity implements
		AdapterView.OnItemClickListener, Game {
	static final int ACTION_ENABLE_BT = 101;
	EditText mEditData;
	BluetoothAdapter mBA;
	ListView mListDevice;
	ArrayList<String> mArDevice; // 원격 디바이스 목록
	static final String BLUE_NAME = "BluetoothEx"; // 접속시 사용하는 이름
	// 접속시 사용하는 고유 ID
	static final UUID BLUE_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	ClientThread mCThread = null; // 클라이언트 소켓 접속 스레드
	ServerThread mSThread = null; // 서버 소켓 접속 스레드
	SocketThread mSocketThread = null; // 데이터 송수신 스레드

	Player player1, player2;
	Context context;
	LinearLayout container;
	String player1dek, player1hero, player2dek, player2hero;
	boolean first, server;
	String ip;
	int port;
	static boolean isgameStart = false;
	RelativeLayout animate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_game);

		context = this;
		Method.setContext(context);

		animate = (RelativeLayout) findViewById(R.id.blueanimate);
		container = new LinearLayout(this);
		LinearLayout.LayoutParams conparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		container.setLayoutParams(conparams);
		container.setOrientation(LinearLayout.VERTICAL);

		mEditData = (EditText) findViewById(R.id.editData);
		// ListView 초기화
		initListView();

		// 블루투스 사용 가능상태 판단
		boolean isBlue = canUseBluetooth();
		if (isBlue)
			// 페어링된 원격 디바이스 목록 구하기
			getParedDevice();
	}

	// 블루투스 사용 가능상태 판단
	public boolean canUseBluetooth() {
		// 블루투스 어댑터를 구한다
		mBA = BluetoothAdapter.getDefaultAdapter();
		// 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
		if (mBA == null) {
			Method.alert("Device not found");
			return false;
		}

		Method.alert("Device is exist");
		// 블루투스 활성화 상태라면 함수 탈출
		if (mBA.isEnabled()) {
			Method.alert("\nDevice can use");
			return true;
		}

		// 사용자에게 블루투스 활성화를 요청한다
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, ACTION_ENABLE_BT);
		return false;
	}

	// 블루투스 활성화 요청 결과 수신
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTION_ENABLE_BT) {
			// 사용자가 블루투스 활성화 승인했을때
			if (resultCode == RESULT_OK) {
				Method.alert("\nDevice can use");
				// 페어링된 원격 디바이스 목록 구하기
				getParedDevice();
			}
			// 사용자가 블루투스 활성화 취소했을때
			else {
				Method.alert("\nDevice can not use");
			}
		}
	}

	// 원격 디바이스 검색 시작
	public void startFindDevice() {
		// 원격 디바이스 검색 중지
		stopFindDevice();
		// 디바이스 검색 시작
		mBA.startDiscovery();
		// 원격 디바이스 검색 이벤트 리시버 등록
		registerReceiver(mBlueRecv, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
	}

	// 디바이스 검색 중지
	public void stopFindDevice() {
		// 현재 디바이스 검색 중이라면 취소한다
		if (mBA.isDiscovering()) {
			mBA.cancelDiscovery();
			// 브로드캐스트 리시버를 등록 해제한다
			unregisterReceiver(mBlueRecv);
		}
	}

	// 원격 디바이스 검색 이벤트 수신
	BroadcastReceiver mBlueRecv = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction() == BluetoothDevice.ACTION_FOUND) {
				// 인텐트에서 디바이스 정보 추출
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 페어링된 디바이스가 아니라면
				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
					// 디바이스를 목록에 추가
					addDeviceToList(device.getName(), device.getAddress());
			}
		}
	};

	// 디바이스를 ListView 에 추가
	public void addDeviceToList(String name, String address) {
		// ListView 와 연결된 ArrayList 에 새로운 항목을 추가
		String deviceInfo = name + " - " + address;
		Log.d("tag1", "Device Find: " + deviceInfo);
		mArDevice.add(deviceInfo);
		// 화면을 갱신한다
		ArrayAdapter adapter = (ArrayAdapter) mListDevice.getAdapter();
		adapter.notifyDataSetChanged();
	}

	// ListView 초기화
	public void initListView() {
		// 어댑터 생성
		mArDevice = new ArrayList<String>();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mArDevice);
		// ListView 에 어댑터와 이벤트 리스너를 지정
		mListDevice = (ListView) findViewById(R.id.listDevice);
		mListDevice.setAdapter(adapter);
		mListDevice.setOnItemClickListener(this);
	}

	// 다른 디바이스에게 자신을 검색 허용
	public void setDiscoverable() {
		// 현재 검색 허용 상태라면 함수 탈출
		if (mBA.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
			return;
		// 다른 디바이스에게 자신을 검색 허용 지정
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
		startActivity(intent);
	}

	// 페어링된 원격 디바이스 목록 구하기
	public void getParedDevice() {
		if (mSThread != null)
			return;
		// 서버 소켓 접속을 위한 스레드 생성 & 시작
		mSThread = new ServerThread();
		mSThread.start();

		// 블루투스 어댑터에서 페어링된 원격 디바이스 목록을 구한다
		Set<BluetoothDevice> devices = mBA.getBondedDevices();
		// 디바이스 목록에서 하나씩 추출
		for (BluetoothDevice device : devices) {
			// 디바이스를 목록에 추가
			addDeviceToList(device.getName(), device.getAddress());
		}

		// 원격 디바이스 검색 시작
		startFindDevice();

		// 다른 디바이스에 자신을 노출
		setDiscoverable();
	}

	// ListView 항목 선택 이벤트 함수
	@SuppressWarnings("rawtypes")
	@SuppressLint("NewApi")
	public void onItemClick(AdapterView parent, View view, int position, long id) {
		// 사용자가 선택한 항목의 내용을 구한다
		String strItem = mArDevice.get(position);

		// 사용자가 선택한 디바이스의 주소를 구한다
		int pos = strItem.indexOf(" - ");
		if (pos <= 0)
			return;
		String address = strItem.substring(pos + 3);
		Method.alert("Sel Device: " + address);

		// 디바이스 검색 중지
		stopFindDevice();
		// 서버 소켓 스레드 중지
		mSThread.cancel();
		mSThread = null;

		if (mCThread != null)
			return;
		// 상대방 디바이스를 구한다
		BluetoothDevice device = mBA.getRemoteDevice(address);
		// 클라이언트 소켓 스레드 생성 & 시작
		mCThread = new ClientThread(device);
		mCThread.start();
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String strMsg = (String) msg.obj;
				Do(response);
				Method.alert(strMsg);
				break;
			case 1:
				animate.removeAllViews();
				animate.addView(container);
				setGame("2x1,3x2,4x3", "heroblue,1");
				Random r = new Random();
				int order = r.nextInt(2);
				if (order == 0) {
					Do("0&1");
					Sender.S("0&0");
					return;
				}
				Do("0&0");
				Sender.S("0&1");				
				break;
			case 2:
				animate.removeAllViews();
				animate.addView(container);
				setGame("2x1,3x2,4x3", "heroblue,1");
				break;
			}
		}
	};

	public void setGame(String dekstring, String herostring) {
		player1dek = dekstring;
		player1hero = herostring;
	}

	public void player1Setting(boolean first) {

		isgameStart = true;
		Attack.setAnimate(animate);
		Helper.setHelper(context);

		container.removeAllViews();
		player1 = new Player(context, player1dek, player1hero, 1, this, first);
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

		Sender.S("7&1@" + player1.heroString());
	}

	public void player2Setting() {

		player2 = new Player(context, player2dek, player2hero, 2, this,
				!player1.getFirst());
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

	private void Do(String resString) {
		
		try {
			String[] response = resString.split("&");
			int type = Integer.parseInt(response[0]);
			switch (type) {

			case 0: // 0번이 넘어오면 시작한다. (카드 바꾸기 화면 실행)
				Card.stateChange();
				if (Integer.parseInt(response[1]) == 1) {
					player1Setting(true);
					return;
				}
				player1Setting(false);
				break;

			case 3: // 3번은 상데의 덱과 히어로정보를 가지고 온다.
					// 상대의 덱을 세팅.
				String[] dekhero = response[1].split(";");
				player2dek = dekhero[0];
				player2hero = dekhero[1];
				player2Setting();
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
					Sender.S("6&");
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

			case 8: // 8번은 사용할 카드의 정보를 가지고 온다.
					// 플레이어의 덱에있는 카드를 index를 통해 사용.

				String[] mon = response[1].split("@");

				Card card;
				String cardstring;
				if (mon[0].equals("1")) {
					cardstring = player2.getCardStringById(Integer
							.parseInt(mon[2]));
					card = new Card(context, cardstring, player2.hand,
							Integer.parseInt(mon[1]), Integer.parseInt(mon[2]));
					player2.field.addByCard(card, true);
					return;
				}
				cardstring = player1
						.getCardStringById(Integer.parseInt(mon[2]));
				card = new Card(context, cardstring, player1.hand,
						Integer.parseInt(mon[1]), Integer.parseInt(mon[2]));
				player1.field.addByCard(card, true);
				break;

			case 80:
				Helper.showInfo(new Ani(getCardStringById(response[1])));
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

				int healamount = Integer.parseInt(heal[1]);

				Target healtarget = getByIndex(heal[0]);
				Target from = getByIndex(heal[2]);

				if (heal[3].equals("null")) {
					healtarget.heal(healamount, true, from, null);
					break;
				}
				healtarget.heal(healamount, true, from, heal[3]);
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

			case 100:
				player1.gameEnd(1); // 패배
				Sender.S("550&");
				break;

			case 101:
				player1.gameEnd(2); // 상대가 나감.
				break;

			case 103:
				player1.gameEnd(3); // 상대가 나감.
				break;

			case 550: // 게임 엔드.
				Sender.close();
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getCardStringById(String string) {
		String[] tmp = string.split("@");
		if (tmp[0].equals("1")) {
			return player2.getCardStringById(Integer.parseInt(tmp[2]));
		}
		return player1.getCardStringById(Integer.parseInt(tmp[2]));

	}

	private Target getByIndex(String index) {
		String[] tmp = index.split("#");
		if (Integer.parseInt(tmp[0]) == 1) {
			if (Integer.parseInt(tmp[1]) == -1)
				return player2.hero.hero();
			return player2.field.getByIndex(Integer.parseInt(tmp[1]));
		}
		if (Integer.parseInt(tmp[1]) == -1)
			return player1.hero.hero();
		return player1.field.getByIndex(Integer.parseInt(tmp[1]));
	}

	@Override
	public ViewGroup container() {
		return container;
	}

	public static boolean isStart() {
		return isgameStart;
	}

	public static void resetStart() {
		isgameStart = false;
	}

	// 클라이언트 소켓 생성을 위한 스레드
	@SuppressLint("NewApi")
	private class ClientThread extends Thread {
		private BluetoothSocket mmCSocket;

		// 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
		public ClientThread(BluetoothDevice device) {
			try {
				mmCSocket = device
						.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
			} catch (IOException e) {
				showMessage("Create Client Socket error");
				return;
			}
		}

		public void run() {
			// 원격 디바이스와 접속 시도
			try {
				mmCSocket.connect();
			} catch (IOException e) {
				showMessage("Connect to server error");
				// 접속이 실패했으면 소켓을 닫는다
				try {
					mmCSocket.close();
				} catch (IOException e2) {
					showMessage("Client Socket close error");
				}
				return;
			}

			// 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
			onConnected(mmCSocket, false);
		}

		// 클라이언트 소켓 중지
		public void cancel() {
			try {
				mmCSocket.close();
			} catch (IOException e) {
				showMessage("Client Socket close error");
			}
		}
	}

	// 서버 소켓을 생성해서 접속이 들어오면 클라이언트 소켓을 생성하는 스레드
	@SuppressLint("NewApi")
	private class ServerThread extends Thread {
		private BluetoothServerSocket mmSSocket;

		// 서버 소켓 생성
		public ServerThread() {
			try {
				mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(
						BLUE_NAME, BLUE_UUID);
			} catch (IOException e) {
				showMessage("Get Server Socket Error");
			}
		}

		public void run() {
			BluetoothSocket cSocket = null;

			// 원격 디바이스에서 접속을 요청할 때까지 기다린다
			try {
				cSocket = mmSSocket.accept();
			} catch (IOException e) {
				showMessage("Socket Accept Error");
				return;
			}

			// 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
			onConnected(cSocket, true);
		}

		// 서버 소켓 중지
		public void cancel() {
			try {
				mmSSocket.close();
			} catch (IOException e) {
				showMessage("Server Socket close error");
			}
		}
	}

	// 메시지를 화면에 표시
	public void showMessage(String strMsg) {
		// 메시지 텍스트를 핸들러에 전달
		Message msg = Message.obtain(mHandler, 0, strMsg);
		mHandler.sendMessage(msg);
		Log.d("tag1", strMsg);
	}

	// 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
	public void onConnected(BluetoothSocket socket, boolean server) {
		showMessage("Socket connected");

		this.server = server;

		// 데이터 송수신 스레드가 생성되어 있다면 삭제한다
		if (mSocketThread != null)
			mSocketThread = null;
		// 데이터 송수신 스레드를 시작
		mSocketThread = new SocketThread(socket);
		mSocketThread.start();
		if (server) {
			mHandler.sendEmptyMessage(1);
			return;
		}
		mHandler.sendEmptyMessage(2);
	}

	// 데이터 송수신 스레드
	private class SocketThread extends Thread {
		private final BluetoothSocket mmSocket; // 클라이언트 소켓
		private InputStream mmInStream; // 입력 스트림

		public SocketThread(BluetoothSocket socket) {
			mmSocket = socket;
			Sender.setBluetoothSocket(socket);

			// 입력 스트림과 출력 스트림을 구한다
			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e) {
				showMessage("Get Stream error");
			}
		}

		// 소켓에서 수신된 데이터를 화면에 표시한다
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;

			while (true) {
				try {
					// 입력 스트림에서 데이터를 읽는다
					bytes = mmInStream.read(buffer);
					response = new String(buffer, 0, bytes);
					showMessage("Receive: " + response);
					SystemClock.sleep(1);
				} catch (IOException e) {
					showMessage("Socket disconneted");
					break;
				}
			}
		}

	}

	// 앱이 종료될 때 디바이스 검색 중지
	public void onDestroy() {
		super.onDestroy();
		// 디바이스 검색 중지
		stopFindDevice();

		// 스레드를 종료
		if (mCThread != null) {
			mCThread.cancel();
			mCThread = null;
		}
		if (mSThread != null) {
			mSThread.cancel();
			mSThread = null;
		}
		if (mSocketThread != null)
			mSocketThread = null;
	}

}