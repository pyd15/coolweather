package model;

public class County {
	private int id;
	private String countyname;
	private int countyCode;
	private int cityId;
	private String weatherId;
	
	
	public String getWeatherId() {
		return weatherId;
	}
	public void setWeatherId(String weatherId) {
		this.weatherId = weatherId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountyname() {
		return countyname;
	}
	public void setCountyname(String countyname) {
		this.countyname = countyname;
	}
//	public String getCountyCode() {
//		return countyCode;
//	}
//	public void setCountyCode(String countyCode) {
//		this.countyCode = countyCode;
//	}
	public int getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(int countyCode) {
		this.countyCode = countyCode;
	}
}
