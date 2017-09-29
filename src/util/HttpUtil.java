package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.Province;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.coolweather.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpUtil {
	
	public static final int SHOW_RESPONE = 0;
	public static final int SHOW_LOCATION = 1;
	public static final int SHOW_JSONDATA = 2;
	public static final int SHOW_GSONDATA = 3;

	List<String> dataListForGSON = new ArrayList<String>();
	List<String> dataListForJSON = new ArrayList<String>();
	
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONE: {
				String respone = (String) msg.obj;
				break;
			}
			case SHOW_LOCATION: {
				String currentPosition = (String) msg.obj;
				break;
			}
			case SHOW_JSONDATA: {
				String dataString = "";
//				for (int i = 0; i < dataListForJSON.size(); i++) {
//					dataString += dataListForJSON.get(i);
//				}
//				Toast.makeText(MainActivity.this, "This is from Json!", Toast.LENGTH_SHORT).show();
				break;
			}
			case SHOW_GSONDATA: {
				String dataString = "";
//				for (int i = 0; i < dataListForGSON.size(); i++) {
//					dataString += dataListForGSON.get(i);
//				}
//				Toast.makeText(MainActivity.this, "This is from Gson!", Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		}
	};
	
	
	public static void sendHttpRequest(final String address,final HttpCallBackListener listener)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				URL url;
				try {
					//不知为何用http开头不行？？
					url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(80000);
					connection.setReadTimeout(80000);
					// 下面对输入流进行读取
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder respone = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						respone.append(line);
					}
					if (listener!=null) {
						//回调onFinish方法
						listener.onFinish(respone.toString());
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if (listener!=null) {
						//回调onError方法
						listener.onError(e);
					}
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
//				Toast.makeText(, "!!!", Toast.LENGTH_SHORT).show();
			}
		}).start();
	}
	
	public static void sendRequestWithHttpClient(final String address,final HttpCallBackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient httpClient = new DefaultHttpClient();
				// 指定访问的服务器地址为本机
				HttpGet httpGet = new HttpGet(
						address);
				
				//http://www.guolin.tech/api/china
				// 172.16.28.245
				// 10.0.2.2 //模拟器上用这个ip或后者
				// 192.168.137.1
				try {
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 取出服务返回的具体内容
						HttpEntity entity = httpResponse.getEntity();
						// 因为返回的数据可能包含中文，为防止乱码出现故指定字符集为"utf-8"
						String respone = EntityUtils.toString(entity, "utf-8");
//						parseJSONWithJSONObject(respone);
//						parseJSONWithGSON(respone);
						if (listener!=null) {
							//回调onFinish方法
							listener.onFinish(respone.toString());
						}
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					if (listener!=null) {
						//回调onError方法
						listener.onError(e);
					}
				}
			}
		}).start();
	}
	
	private static void parseJSONWithJSONObject(String jsonData) {
		JSONArray jsonArray;
		try {
			String id;
			String name;
			String version;
			String string;
			jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				id = jsonObject.getString("id");
				name = jsonObject.getString("name");
//				version = jsonObject.getString("version");
//				string = "id is " + id + " " + "name is " + name + " "
//						+ "version is " + version + "\n";
				string = "id is " + id + " " + "name is " + name +"\n";
				
//				dataListForJSON.add(string);
				Log.d("JSON", "id is " + id);
				Log.d("JSON", "name is " + name);
//				Log.d("JSON", "version is " + version);
			}
			Message message = new Message();
			message.what = SHOW_JSONDATA;
			handler.sendMessage(message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void parseJSONWithGSON(String jsonData) {
		Gson gson = new Gson();
//		List<App> appList = gson.fromJson(jsonData, new TypeToken<List<App>>() {
//		}.getType());
		List<Province> weatherList = gson.fromJson(jsonData, new TypeToken<List<Province>>() {
		}.getType());
		
		String data = null;
		int j = 0;
//		for (App app : appList) {
		for (Province weather : weatherList) {
//			Log.d("GSON", "id is " + weather.getId());
//			Log.d("GSON", "name is " + weather.getname());
//			data = "id is " + weather.getId() + " " + "name is " + weather.getname()+ "\n";
//			dataListForGSON.add(data);
//			Log.d("list", dataListForGSON.get(j));
			j++;
		}
//		for (int i = 0; i < dataListForGSON.size(); i++) {
//			Log.d("all data", dataListForGSON.get(i));
//		}
		Message message = new Message();
		message.what = SHOW_GSONDATA;
		handler.sendMessage(message);
	}
	
}
