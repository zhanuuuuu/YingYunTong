package bean;

import java.util.List;

public class Head_Request_Order {
	
	
	private String cStoreName;
	private String fQty;
	private String cUnit;
	private String cGoodsNo;
	private String cGoodsName;
	private String cSheetno;
	private String cSpec;
	
	public String getcSpec() {
		return cSpec;
	}

	public void setcSpec(String cSpec) {
		this.cSpec = cSpec;
	}

	public String getcSheetno() {
		return cSheetno;
	}

	public void setcSheetno(String cSheetno) {
		this.cSheetno = cSheetno;
	}

	private List<Store_Request> Store_Request_list;

	public String getcStoreName() {
		return cStoreName;
	}

	public void setcStoreName(String cStoreName) {
		this.cStoreName = cStoreName;
	}

	public String getfQty() {
		return fQty;
	}

	public void setfQty(String fQty) {
		this.fQty = fQty;
	}

	public String getcUnit() {
		return cUnit;
	}

	public void setcUnit(String cUnit) {
		this.cUnit = cUnit;
	}

	public String getcGoodsNo() {
		return cGoodsNo;
	}

	public void setcGoodsNo(String cGoodsNo) {
		this.cGoodsNo = cGoodsNo;
	}

	public String getcGoodsName() {
		return cGoodsName;
	}

	public void setcGoodsName(String cGoodsName) {
		this.cGoodsName = cGoodsName;
	}

	public List<Store_Request> getStore_Request_list() {
		return Store_Request_list;
	}

	public void setStore_Request_list(List<Store_Request> store_Request_list) {
		Store_Request_list = store_Request_list;
	}


	

}
