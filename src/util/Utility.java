package util;

import gson.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import model.City;
import model.County;
import model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import db.CoolWeatherDBHelper;

public class Utility {
	//解析并处理服务器返回的省级数据
//	public synchronized static boolean handleProvinceResponeData(CoolWeatherDBHelper coolWeatherDBHelper,
//			String response) {
//		if (!TextUtils.isEmpty(response)) {
//			String[] allProvinces=response.split(",");
//			if (allProvinces!=null&&allProvinces.length>0) {
//				for (String provinces: allProvinces) {
//					String[] provincearray=provinces.split("\\|");
//					Province province = new Province();
//					province.setProvinceCode(provincearray[0]);
//					province.setProvincename(provincearray[1]);
//					// 将解析出来的数据存储到Province表
//					coolWeatherDBHelper.saveProvince(province);
//				}
//				return true;
//			}
//		}
//		return false;
//	}
	public synchronized static boolean handleProvincesRespone(CoolWeatherDBHelper coolWeatherDBHelper,String response) {
		if (!TextUtils.isEmpty(response)) {
			JSONArray allProvinces;
			try {
				allProvinces = new JSONArray(response);
				for (int i = 0; i < allProvinces.length(); i++) {
					JSONObject provinceObject=allProvinces.getJSONObject(i);
					Province province = new Province();
					province.setProvinceCode(provinceObject.getInt("id"));
					province.setProvincename(provinceObject.getString("name"));
					// 将解析出来的数据存储到Province表
					coolWeatherDBHelper.saveProvince(province);
				}
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
//	public static boolean handleCitiesResponse(CoolWeatherDBHelper coolWeatherDBHelper,
//			String response, int provinceId) {
//		if (!TextUtils.isEmpty(response)) {
//			String[] allCities = response.split(",");
//			if (allCities != null && allCities.length > 0) {
//				for (String c : allCities) {
//					String[] array = c.split("\\|");
//					City city = new City();
//					city.setCityCode(array[0]);
//					city.setCityname(array[1]);
//					city.setProvinceId(provinceId);
//					// 将解析出来的数据存储到City表
//					coolWeatherDBHelper.saveCity(city);
//				}
//				return true;
//			}
//		}
//		return false;
//	}
	
	public static boolean handleCitiesResponse(CoolWeatherDBHelper coolWeatherDBHelper,String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			JSONArray allCities;
			try {
				allCities = new JSONArray(response);
				for (int i = 0; i < allCities.length(); i++) {
					JSONObject citiesoObject=allCities.getJSONObject(i);
					City city = new City();
					city.setCityCode(citiesoObject.getInt("id"));
					city.setCityname(citiesoObject.getString("name"));
					city.setProvinceId(provinceId);
					// 将解析出来的数据存储到City表
					coolWeatherDBHelper.saveCity(city);
				}
				return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
//	public static boolean handleCountiesResponse(CoolWeatherDBHelper coolWeatherDB,
//			String response, int cityId) {
//		if (!TextUtils.isEmpty(response)) {
//			String[] allCounties = response.split(",");
//			if (allCounties != null && allCounties.length > 0) {
//				for (String c : allCounties) {
//					String[] array = c.split("\\|");
//					County county = new County();
//					county.setCountyCode(array[0]);
//					county.setCountyname(array[1]);
//					county.setCityId(cityId);
//					// 将解析出来的数据存储到County表
//					coolWeatherDB.saveCounty(county);
//				}
//				return true;
//			}
//		}
//		return false;
//	}
	
	public static boolean handleCountiesResponse(CoolWeatherDBHelper coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			JSONArray allCounties;
			try {
				allCounties = new JSONArray(response);
				for (int i = 0; i < allCounties.length(); i++) {
					JSONObject countyObject=allCounties.getJSONObject(i);
					County county = new County();
					county.setCountyCode(countyObject.getInt("id"));
					county.setCountyname(countyObject.getString("name"));
					county.setWeatherId(countyObject.getString("weather_id"));
					county.setCityId(cityId);
					// 将解析出来的数据存储到County表
					coolWeatherDB.saveCounty(county);
			}
				return true;
			}
				catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		return false;
	}

	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
//			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
//					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static Weather handleWeatherResponse(String response){
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
			String weatherContent=jsonArray.getJSONObject(0).toString();
			return new Gson().fromJson(weatherContent, Weather.class);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
//	public static void saveWeatherInfo(Context context, Weather weather) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
//		SharedPreferences.Editor editor = PreferenceManager
//				.getDefaultSharedPreferences(context).edit();
//		editor.putBoolean("city_selected", true);
//		editor.putString("city_name", cityName);
//		editor.putString("weather_code", weatherCode);
//		editor.putString("temp1", temp1);
//		editor.putString("temp2", temp2);
//		editor.putString("weather_desp", weatherDesp);
//		editor.putString("publish_time", publishTime);
//		editor.putString("current_date", sdf.format(new Date()));
//		editor.commit();
//	}

}