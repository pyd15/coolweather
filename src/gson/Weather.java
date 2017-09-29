package gson;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Weather {
	public String status;
	public Basic basic;
	public Aqi aqi;
	public Now now;
	public Suggestion suggestion;
	
	//通过@SerializedName注解方式使Json字段与Java字段建立映射联系
	@SerializedName("daily_forecast")
	public List<Forecast> forecastsList;
}
