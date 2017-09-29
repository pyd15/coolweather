package activity;

import model.City;
import model.Province;
import gson.Forecast;
import gson.Weather;
import util.HttpCallBackListener;
import util.HttpUtil;
import util.Utility;
import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coolweather.R;

public class WeatherActivity extends Activity {

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

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

	public DrawerLayout drawerLayout;

	public SwipeRefreshLayout swipeRefresh;

	private ScrollView weatherLayout;

	private Button navButton;
	
	private Button switchButton;

	private TextView titleCity;

	private TextView titleUpdateTime;

	private TextView degreeText;

	private TextView weatherInfoText;

	private LinearLayout forecastLayout;

	private TextView aqiText;

	private TextView pm25Text;

	private TextView comfortText;

	private TextView carWashText;

	private TextView sportText;

	private TextView travelText;

	private TextView dressText;

	private ImageView bingPicImg;

	private ProgressDialog progressDialog;

	private String mWeatherId;
	private String WeatherId;

	// private int mWeatherId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (Build.VERSION.SDK_INT >= 21) {
		// View decorView = getWindow().getDecorView();
		// decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
		// getWindow().setStatusBarColor(Color.TRANSPARENT);
		// }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);

		// ��ʼ�����ؼ�
		// bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
		weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
		titleCity = (TextView) findViewById(R.id.title_city);
		titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
		degreeText = (TextView) findViewById(R.id.degree_text);
		weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
		forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
		aqiText = (TextView) findViewById(R.id.api_text);
		pm25Text = (TextView) findViewById(R.id.pm25_text);
		comfortText = (TextView) findViewById(R.id.comfort_text);
		carWashText = (TextView) findViewById(R.id.carwash_text);
		sportText = (TextView) findViewById(R.id.sport_text);
		travelText = (TextView) findViewById(R.id.travel_text);
		dressText = (TextView) findViewById(R.id.dress_text);
		swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
		switchButton=(Button)findViewById(R.id.change_city);
//		swipeRefresh.setColorSchemeResources(R.color.holo_blue_bright,
//				0, 0, 0);
		swipeRefresh.setColorSchemeResources(android.R.color.black, android.R.color.holo_blue_bright,
				android.R.color.holo_green_dark, android.R.color.holo_orange_light);
		// drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// navButton = (Button) findViewById(R.id.nav_button);
//		String weatherId = getIntent().getStringExtra("weather_id");
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String weatherString = prefs.getString("weather", null);
		boolean isFromChooseActivity=getIntent().getBooleanExtra("from ChooseActivity", false);
		if (weatherString != null&&isFromChooseActivity) {
			// �л���ʱֱ�ӽ�����������
			Weather weather = Utility.handleWeatherResponse(weatherString);
			mWeatherId = weather.basic.weatherId;
			showWeatherInfo(weather);
		} else {
			// �޻���ʱȥ��������ѯ����
			mWeatherId = getIntent().getStringExtra("weather_id");
			weatherLayout.setVisibility(View.INVISIBLE);// ��ѯ������ǰ������scrollview
			requestWeather(mWeatherId);
		}
		swipeRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						requestWeather(mWeatherId);
					}
				});
		// navButton.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// drawerLayout.openDrawer(GravityCompat.START);
		// }
		// });
		// String bingPic = prefs.getString("bing_pic", null);
		// if (bingPic != null) {
		// Glide.with(this).load(bingPic).into(bingPicImg);
		// } else {
		// loadBingPic();
		// }
		
		switchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.change_city:
					Intent intent=new Intent(WeatherActivity.this,ChooseAreaActivity.class);
					intent.putExtra("from WeatherActivity", true);
					startActivity(intent);
					finish();
					break;
				
				default:
					break;
				}
			}
		});
	}

	/**
	 * ��������id�������������Ϣ��
	 */
	public void requestWeather(final String weatherId) {
		showProgressDialog();
		String weatherUrl = "http://guolin.tech/api/weather?cityid="
				+ weatherId + "&key=cc59b04db26046a39fe55e93150b8ad1";
		HttpUtil.sendHttpRequest(weatherUrl, new HttpCallBackListener() {
			// HttpUtil.sendRequestWithHttpClient(weatherUrl, new
			// HttpCallBackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				final String responseText = response;
				final Weather weather = Utility
						.handleWeatherResponse(responseText);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						// status=ok��˵�������������ݳɹ�
						if (weather != null && "ok".equals(weather.status)) {
							SharedPreferences.Editor editor = PreferenceManager
									.getDefaultSharedPreferences(
											WeatherActivity.this).edit();
							editor.putString("weather", responseText);
							editor.putString("weatherId", weatherId);
							editor.apply();
							showWeatherInfo(weather);
						} else {
							Toast.makeText(WeatherActivity.this, "��ȡ������Ϣʧ��",
									Toast.LENGTH_SHORT).show();
						}
						 swipeRefresh.setRefreshing(false);
					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(WeatherActivity.this, "��ȡ������Ϣʧ��",
								Toast.LENGTH_SHORT).show();
						swipeRefresh.setRefreshing(false);
					}
				});
			}
		});

		// loadBingPic();
	}

	/**
	 * ���ر�Ӧÿ��һͼ
	 */
	// private void loadBingPic() {
	// String requestBingPic = "http://guolin.tech/api/bing_pic";
	// HttpUtil.sendOkHttpRequest(requestBingPic, new HttpCallBackListener() {
	//
	// @Override
	// public void onFinish(String respone) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onError(Exception e) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	// }

	/**
	 * ����չʾWeatherʵ�����е����ݡ�
	 */
	private void showWeatherInfo(Weather weather) {

		String cityName = weather.basic.cityName;
		String updateTime = weather.basic.update.updateTime.split(" ")[1];
		String degree = weather.now.temperature + "��";
		String weatherInfo = weather.now.more.info;
		titleCity.setText(cityName);
		titleUpdateTime.setText(updateTime);
		degreeText.setText(degree);
		weatherInfoText.setText(weatherInfo);
		forecastLayout.removeAllViews();
		for (Forecast forecast : weather.forecastsList) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.forecast_item, forecastLayout, false);
			TextView dateText = (TextView) view.findViewById(R.id.date_text);
			TextView infoText = (TextView) view.findViewById(R.id.info_text);
			TextView maxText = (TextView) view.findViewById(R.id.max_text);
			TextView minText = (TextView) view.findViewById(R.id.min_text);
			dateText.setText(forecast.date);
			infoText.setText(forecast.more.info);
			maxText.setText(forecast.temperature.max);
			minText.setText(forecast.temperature.min);
			forecastLayout.addView(view);
		}
		if (weather.aqi != null) {
			aqiText.setText(weather.aqi.city.aqi);
			pm25Text.setText(weather.aqi.city.pm25);
		} else {
			aqiText.setText("��");
			pm25Text.setText("��");
		}
		String comfort = "���ʶȣ�\n" + weather.suggestion.comfort.comfortinfo;
		String carWash = "ϴ��ָ����\n" + weather.suggestion.carwash.cwinfo;
		String sport = "�˶����飺\n" + weather.suggestion.sport.sportinfo;
		String travel = "���н��飺\n" + weather.suggestion.travel.travelinfo;
		String dress = "���Ž��飺\n" + weather.suggestion.dress.dressinfo;
		comfortText.setText(comfort);
		carWashText.setText(carWash);
		sportText.setText(sport);
		travelText.setText(travel);
		dressText.setText(dress);
		weatherLayout.setVisibility(View.VISIBLE);
		// Intent intent = new Intent(this, AutoUpdateService.class);
		// startService(intent);
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	// @Override
	// public void onBackPressed() {
	// // TODO Auto-generated method stub
	// finish();
	// Intent intent = new Intent(this, ChooseAreaActivity.class);
	// // if (currentLevel==LEVEL_COUNTY) {
	// // .queryCities();
	// // }else if (currentLevel==LEVEL_CITY) {
	// // queryProvinces();
	// // }else {
	// // finish();
	// // }
	// startActivity(intent);
	// }
}
