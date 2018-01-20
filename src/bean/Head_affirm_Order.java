package bean;

import java.util.List;

public class Head_affirm_Order {
	
	
	private String cStoreName;
	private String fQuantity;
	private String cUnit;
	private String cGoodsNo;
	private String cGoodsName;
	private String Head_affirm_cSheetno;
	private String cSpec;
	private String price;
	private String Image_Path;
	private List<Head_affirm_Store_Goods_Detail> StoreList;
	public String getImage_Path() {
		return Image_Path;
	}
	public void setImage_Path(String image_Path) {
		Image_Path = image_Path;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	
	public String getcStoreName() {
		return cStoreName;
	}
	public void setcStoreName(String cStoreName) {
		this.cStoreName = cStoreName;
	}
	public String getfQuantity() {
		return fQuantity;
	}
	public void setfQuantity(String fQuantity) {
		this.fQuantity = fQuantity;
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
	public String getHead_affirm_cSheetno() {
		return Head_affirm_cSheetno;
	}
	public void setHead_affirm_cSheetno(String head_affirm_cSheetno) {
		Head_affirm_cSheetno = head_affirm_cSheetno;
	}
	public String getcSpec() {
		return cSpec;
	}
	public void setcSpec(String cSpec) {
		this.cSpec = cSpec;
	}
	public List<Head_affirm_Store_Goods_Detail> getStoreList() {
		return StoreList;
	}
	public void setStoreList(List<Head_affirm_Store_Goods_Detail> storeList) {
		StoreList = storeList;
	}
	

	

}
