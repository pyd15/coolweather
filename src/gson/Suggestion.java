package gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
	@SerializedName("comf")
	public Comfort comfort;
	
	@SerializedName("cw")
	public CarWash carwash;
	
	@SerializedName("trav")
	public Travel travel;
	 
	@SerializedName("drsg")
	public Dress dress;
	 
	@SerializedName("sport")
	public Sport sport;
	
	public class Comfort{
		@SerializedName("txt")
		public String comfortinfo;
	}
	
	public class Travel{
		@SerializedName("txt")
		public String travelinfo;
	}
	
	public class Dress{
		@SerializedName("txt")
		public String dressinfo;
	}
	
	public class CarWash{
		@SerializedName("txt")
		public String cwinfo;
	}
	
	public class Sport{
		@SerializedName("txt")
		public String sportinfo;
	}
}
