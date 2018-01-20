package bean;

import java.util.List;

public class Head_Customer_Order {
	
	private String cStoreNo;
	private String cStoreName;
	private String cSpuer_No;
	
	public String getcSpuer_No() {
		return cSpuer_No;
	}
	public void setcSpuer_No(String cSpuer_No) {
		this.cSpuer_No = cSpuer_No;
	}
	private List<Sheet> list;
	
	
	public List<Sheet> getList() {
		return list;
	}
	public void setList(List<Sheet> list) {
		this.list = list;
	}
	public String getcStoreNo() {
		return cStoreNo;
	}
	public void setcStoreNo(String cStoreNo) {
		this.cStoreNo = cStoreNo;
	}
	public String getcStoreName() {
		return cStoreName;
	}
	public void setcStoreName(String cStoreName) {
		this.cStoreName = cStoreName;
	}
	

}
