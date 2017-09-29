package activity;

import java.util.ArrayList;
import java.util.List;

import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utility;

import model.City;
import model.County;
import model.Province;

import com.example.coolweather.R;

import db.CoolWeatherDBHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY= 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDBHelper coolWeatherDBHelper;
	private List<String> dataList=new ArrayList<String>();
	
	/**
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<County> countyList;
	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ѡ�еĳ���
	 */
	private City selectedCity;
	/**
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	/**
	 * �Ƿ��WeatherActivity����ת������
	 */
	private boolean isFromWeatherActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
//		if (prefs.getString("weather", null)!=null) {
//			Intent intent=new Intent(this,WeatherActivity.class);
//			startActivity(intent);
////			finish();
//		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDBHelper = CoolWeatherDBHelper.getInstance(ChooseAreaActivity.this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {
					String weatherId=countyList.get(position).getWeatherId();
					Log.d("weatherId", weatherId);
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
//					intent.putExtra("county_code", countyCode);
					intent.putExtra("weather_id", weatherId);
					startActivity(intent);
//					finish();
				}
			}
		});
		queryProvinces();  // ����ʡ������
	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryProvinces() {
		titleText.setText("�й�");
		provinceList = coolWeatherDBHelper.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvincename());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
//			Toast.makeText(this, "From Province", Toast.LENGTH_SHORT).show();
		} else {
			String address="http://guolin.tech/api/china";
			queryFromServer(address, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCities() {
		titleText.setText(selectedProvince.getProvincename());
		cityList = coolWeatherDBHelper.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityname());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvincename());
			currentLevel = LEVEL_CITY;
//			Toast.makeText(this, "From City", Toast.LENGTH_SHORT).show();
		} else {
			int provinceCode=selectedProvince.getProvinceCode();
			String address="http://guolin.tech/api/china/"+provinceCode;
			queryFromServer(address, "city");
		}
	}
	
	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCounties() {
		titleText.setText(selectedCity.getCityname());
		countyList = coolWeatherDBHelper.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyname());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityname());
			currentLevel = LEVEL_COUNTY;
//			Toast.makeText(this, "From County", Toast.LENGTH_SHORT).show();
		} else {
			int cityCode=selectedCity.getCityCode();
			int provinceCode=selectedProvince.getProvinceCode();
			String address="http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
			queryFromServer(address, "county");
		}
	}
	
	/*
	 * ���ݴ���ĵ�ַ�����ʹӷ������ϲ�ѯʡ��������
	 */
	private void queryFromServer(String address,final String type) {
		Toast.makeText(this, "From Server", Toast.LENGTH_SHORT).show();
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesRespone(coolWeatherDBHelper,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDBHelper,
							response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDBHelper,
							response, selectedCity.getId());
				}
				if (result) {
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this,
										"����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	private void showProgressDialog() {
		if (progressDialog==null) {
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		if (progressDialog!=null) {
			progressDialog.dismiss();
		}
	}
	/*
	 *����Back�������ݵ�ǰ�����жϴ�ʱӦ�������б���ʡ�б���ֱ���˳� 
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentLevel==LEVEL_COUNTY) {
			queryCities();
		}else if (currentLevel==LEVEL_CITY) {
			queryProvinces();
		}else {
			finish();
		}
	}
}
