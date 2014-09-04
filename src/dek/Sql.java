package dek;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Sql extends SQLiteOpenHelper {

	public Sql(Context context) {
		super(context, "mydeklist.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists mydeklist"
				+ " (id integer primary key autoincrement not null,"
				+ " hero text, dek text, summary text, sum integer);";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists mydeklist";
		db.execSQL(sql);

		onCreate(db);
	}

	public void insert(Data data) {
		SQLiteDatabase db = getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능

		ContentValues values = new ContentValues();
		// db.insert의 매개변수인 values가 ContentValues 변수이므로 그에 맞춤
		// 데이터의 삽입은 put을 이용한다.
		values.put("hero", data.getHerostring());
		values.put("dek", data.getDekstring());
		values.put("summary", data.getSummary());
		db.insert("mydeklist", null, values); // 테이블/널컬럼핵/데이터(널컬럼핵=디폴트)
		// tip : 마우스를 db.insert에 올려보면 매개변수가 어떤 것이 와야 하는지 알 수 있다.
	}

	// update
	public void update(Data data) {
		SQLiteDatabase db = getWritableDatabase(); // db 객체를 얻어온다. 쓰기가능

		ContentValues values = new ContentValues();
		values.put("hero", data.getHerostring());
		values.put("dek", data.getDekstring());
		values.put("summary", data.getSummary());
		values.put("sum", data.getSum() + "");
		db.update("mydeklist", values, "id=?",
				new String[] { data.getId() + "" });
		Log.i("db",
				data.getId() + data.getHerostring() + "\n"
						+ data.getDekstring() + data.getSummary());
	}

	// delete
	public void delete(Data data) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("mydeklist", "id=?", new String[] { data.getId() + "" });
		Log.i("db", data.getId() + "정상적으로 삭제 되었습니다.");
	}

	public ArrayList<Data> dekList() {
		ArrayList<Data> result = new ArrayList<Data>();

		SQLiteDatabase db = getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
		Cursor c = db.query("mydeklist", new String[] { "id", "dek", "hero",
				"summary", "sum" }, null, null, null, null, null);
		while (c.moveToNext()) {
			result.add(new Data(c.getInt(0), c.getInt(4), c.getString(3), c
					.getString(2), c.getString(1)));
		}

		return result;
	}

	public void delete(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("mydeklist", "id=?", new String[] { (id) + "" });
		Log.i("db", id + "정상적으로 삭제 되었습니다.");
	}
}