package model;

public class City {
	private int id;
	private String cityname;
	private int cityCode;
	private int provinceId;
	
	
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
//	public String getCityCode() {
//		return cityCode;
//	}
//	public void setCityCode(String cityCode) {
//		this.cityCode = cityCode;
//	}
	public int getCityCode() {
		return cityCode;
	}
	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}
}
