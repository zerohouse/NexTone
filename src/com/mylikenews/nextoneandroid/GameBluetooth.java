package com.mylikenews.nextoneandroid;

import game.Game;
import game.Method;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import net.Sender;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import dek.Data;

public class GameBluetooth extends Activity implements
		AdapterView.OnItemClickListener {
	static final int ACTION_ENABLE_BT = 101;
	static final int SELECT_DEK = 100;

	BluetoothAdapter bluetoothAdapter;
	ListView mListDevice;
	ArrayList<String> mArDevice; // 원격 디바이스 목록
	static final String BLUE_NAME = "BluetoothEx"; // 접속시 사용하는 이름
	// 접속시 사용하는 고유 ID
	static final UUID BLUE_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
	ClientThread mCThread = null; // 클라이언트 소켓 접속 스레드
	ServerThread mSThread = null; // 서버 소켓 접속 스레드
	SocketThread mSocketThread = null; // 데이터 송수신 스레드

	Intent selectdek;
	String response = "";
	boolean server, start, done;

	Game game;
	String herostring, dekstring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_bluetooth_game);

		Method.setContext(this);
		selectdek = new Intent(this, SelectDek.class);

		// ListView 초기화
		initListView();

		// 블루투스 사용 가능상태 판단
		boolean isBlue = canUseBluetooth();
		if (isBlue)
			// 페어링된 원격 디바이스 목록 구하기
			getParedDevice();
	}

	// 2.0 and above
	@Override
	public void onBackPressed() {
		if (!Game.isStart()) {
			finish();
			return;
		}
		showAreYouSureDialog();
	}

	// Before 2.0
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!Game.isStart()) {
			finish();
			return super.onKeyDown(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAreYouSureDialog();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showAreYouSureDialog() {
		AlertDialog.Builder areyousure = new AlertDialog.Builder(
				GameBluetooth.this);

		areyousure.setTitle("게임에서 나갑니다.");

		// Set up the buttons
		areyousure.setNegativeButton("포기하고 다음판 할래!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Sender.S("101&"); // 항복
						Sender.close();
						finish();
					}
				});
		areyousure.setPositiveButton("아니 잘못 눌렀어!",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		areyousure.show();
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (response.contains("done"))
					done = true;
				if (start)
					game.Do(response);
				break;

			case 1:
				startActivityForResult(selectdek, SELECT_DEK);
				break;

			case 2:
				startActivityForResult(selectdek, SELECT_DEK);
				break;

			}
		}
	};

	// 블루투스 활성화 요청 결과 수신
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		switch (requestCode) {
		case ACTION_ENABLE_BT:
			// 사용자가 블루투스 활성화 승인했을때
			if (resultCode == RESULT_OK) {
				Method.alert("다른 사용자를 찾습니다.");
				// 페어링된 원격 디바이스 목록 구하기
				getParedDevice();
				return;
			}
			// 사용자가 블루투스 활성화 취소했을때
			Method.alert("블루투스를 사용할 수 없습니다.");
			finish();
			break;

		case SELECT_DEK:
			if (resultCode == RESULT_OK) {

				RelativeLayout animate = (RelativeLayout) findViewById(R.id.blueanimate);
				LinearLayout container = new LinearLayout(this);

				LinearLayout.LayoutParams conparams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				container.setGravity(Gravity.CENTER);
				container.setLayoutParams(conparams);
				container.setOrientation(LinearLayout.VERTICAL);
 
				Data data = (Data) intent.getSerializableExtra("selected");
				dekstring = data.getDekstring();
				herostring = data.getHerostring();
				game = new Game(GameBluetooth.this, container, animate,
						dekstring, herostring);
  
				game.waitForDekSelect();
				start = true;

				Sender.S("done");
				if (!done)
					return;

				startGame();
				return;
			}
			finish();
			break;
		}
	}

	private void startGame() {
		Random r = new Random();
		int order = r.nextInt(2);
		if (order == 0) {
			game.Do("0&1");
			Sender.S("0&0");
			return;
		}
		game.Do("0&0");
		Sender.S("0&1");
	}

	// 블루투스 사용 가능상태 판단
	public boolean canUseBluetooth() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			Method.alert("Device not found");
			finish();
			return false;
		}
		if (bluetoothAdapter.isEnabled()) {
			return true;
		}
		// 사용자에게 블루투스 활성화를 요청한다
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(intent, ACTION_ENABLE_BT);
		return false;
	}

	// 원격 디바이스 검색 시작
	public void startFindDevice() {
		// 원격 디바이스 검색 중지
		stopFindDevice();
		// 디바이스 검색 시작
		bluetoothAdapter.startDiscovery();
		// 원격 디바이스 검색 이벤트 리시버 등록
		registerReceiver(mBlueRecv, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
	}

	// 디바이스 검색 중지
	public void stopFindDevice() {
		// 현재 디바이스 검색 중이라면 취소한다
		if (bluetoothAdapter.isDiscovering()) {
			bluetoothAdapter.cancelDiscovery();
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
		@SuppressWarnings("rawtypes")
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
		if (bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
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
		Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
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
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
		// 클라이언트 소켓 스레드 생성 & 시작
		mCThread = new ClientThread(device);
		mCThread.start();
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
				logMessage("Create Client Socket error");
				return;
			}
		}

		public void run() {
			// 원격 디바이스와 접속 시도
			try {
				mmCSocket.connect();
			} catch (IOException e) {
				logMessage("Connect to server error");
				// 접속이 실패했으면 소켓을 닫는다
				try {
					mmCSocket.close();
				} catch (IOException e2) {
					logMessage("Client Socket close error");
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
				logMessage("Client Socket close error");
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
				mmSSocket = bluetoothAdapter
						.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME,
								BLUE_UUID);
			} catch (IOException e) {
				logMessage("Get Server Socket Error");
			}
		}

		public void run() {
			BluetoothSocket cSocket = null;

			// 원격 디바이스에서 접속을 요청할 때까지 기다린다
			try {
				cSocket = mmSSocket.accept();
			} catch (IOException e) {
				logMessage("Socket Accept Error");
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
				logMessage("Server Socket close error");
			}
		}
	}

	public void logMessage(String strMsg) {
		Log.d("tag1", strMsg);
	}

	// 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
	public void onConnected(BluetoothSocket socket, boolean server) {
		logMessage("Socket connected");

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
		private InputStream mmInStream; // 입력 스트림

		public SocketThread(BluetoothSocket socket) {
			Sender.setBluetoothSocket(socket);

			// 입력 스트림과 출력 스트림을 구한다
			try {
				mmInStream = socket.getInputStream();
			} catch (IOException e) {
				logMessage("Get Stream error");
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
					mHandler.sendEmptyMessage(0);
					SystemClock.sleep(1);
				} catch (IOException e) {
					logMessage("Socket disconneted");
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