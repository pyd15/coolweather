package gson;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Weather {
	public String status;
	public Basic basic;
	public Aqi aqi;
	public Now now;
	public Suggestion suggestion;
	
	//ͨ��@SerializedNameע�ⷽʽʹJson�ֶ���Java�ֶν���ӳ����ϵ
	@SerializedName("daily_forecast")
	public List<Forecast> forecastsList;
}
