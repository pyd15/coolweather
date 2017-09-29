package db;

import model.Province;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	
	private static final String Province_TABLE="Province";
	private static final String City_TABLE = "City";
	private static final String COUNTY_TABLE = "County";
	
	/*
	 * Province表创建语句
	 */
	public static final String CREATE_PROVINCE = "create table Province(" +
			"id integer primary key autoincrement," +
			"province_name text," +
			"province_code integer)";
	/*
	 * City表创建语句
	 */
	public static final String CREATE_CITY = "create table City(" +
			"id integer primary key autoincrement," +
			"city_name text," +
			"city_code integer," +
			"province_id integer)";
	/*
	 * County表创建语句
	 */
	public static final String CREATE_COUNTY = "create table County(" +
			"id integer primary key autoincrement," +
			"county_name text," +
			"county_code integer," +
			"city_id integer," +
			"weather_id text)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists " + Province_TABLE);// 删除旧版表格
		db.execSQL("drop table if exists " + City_TABLE);
		db.execSQL("drop table if exists " + COUNTY_TABLE);
        onCreate(db);// 创建表格
	}

}
