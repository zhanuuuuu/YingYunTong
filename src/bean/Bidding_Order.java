package bean;

import java.util.List;

public class Bidding_Order {

	private String Bidding_State;
	private List<cSheetNo> list;

	public List<cSheetNo> getList() {
		return list;
	}

	public void setList(List<cSheetNo> list) {
		this.list = list;
	}

	public String getBidding_State() {
		return Bidding_State;
	}

	public void setBidding_State(String bidding_State) {
		Bidding_State = bidding_State;
	}
	
	
}
