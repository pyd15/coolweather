package db;

import java.util.ArrayList;
import java.util.List;

import model.City;
import model.County;
import model.Province;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDBHelper {
	//数据库名
	public static final String DB_NAME = "cool_weather";
	//数据库版本
	public static final int VERSION = 2;
	
	private static CoolWeatherDBHelper coolWeatherDBHelper;
	
	private SQLiteDatabase db;
	
	//构造方法私有化
	public CoolWeatherDBHelper(Context context) {
		// TODO Auto-generated constructor stub
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}

	//获取CoolWeatherDBHelper的实例
	public synchronized static CoolWeatherDBHelper getInstance(Context context) {
		if (coolWeatherDBHelper==null) {
			coolWeatherDBHelper=new CoolWeatherDBHelper(context);
		}
		return coolWeatherDBHelper;
	}
	
	//将Province实例存到数据库
	public void saveProvince(Province province) {
		if (province!=null) {
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvincename());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	//从数据库读取全国各省份信息
	public List<Province> loadProvinces() {
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getInt(cursor.getColumnIndex("province_code")));
				province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	//将City实例存到数据库
		public void saveCity(City city) {
			if (city!=null) {
				ContentValues values=new ContentValues();
				values.put("city_name", city.getCityname());
				values.put("city_code", city.getCityCode());
				values.put("province_id", city.getProvinceId());
				db.insert("City", null, values);
			}
		}
		
		//从数据库读取某省下各市信息
		public List<City> loadCities(int provinceId) {
			List<City> list=new ArrayList<City>();
			Cursor cursor=db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					City city=new City();
					city.setId(cursor.getInt(cursor.getColumnIndex("id")));
					city.setCityCode(cursor.getInt(cursor.getColumnIndex("city_code")));
					city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
					city.setProvinceId(provinceId);
					list.add(city);
				} while (cursor.moveToNext());
			}
			return list;
		}
		
		//将County实例存到数据库
		public void saveCounty(County county) {
			if (county!=null) {
				ContentValues values=new ContentValues();
				values.put("county_name", county.getCountyname());
				values.put("county_code", county.getCountyCode());
				values.put("city_id", county.getCityId());
				values.put("weather_id", county.getWeatherId());
				db.insert("County", null, values);
			}
		}
		
		//从数据库读取某城市下所有县的信息
		public List<County> loadCounties(int cityId) {
			List<County> list=new ArrayList<County>();
			Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					County county=new County();
					county.setId(cursor.getInt(cursor.getColumnIndex("id")));
					county.setCountyCode(cursor.getInt(cursor.getColumnIndex("county_code")));
					county.setCountyname(cursor.getString(cursor.getColumnIndex("county_name")));
					county.setCityId(cityId);
					county.setWeatherId(cursor.getString(cursor.getColumnIndex("weather_id")));
					list.add(county);
				} while (cursor.moveToNext());
			}
			return list;
		}
}
