package bean;
/**
 * Order_Details                      ���������

cSheetno	varchar(50)	Unchecked              �������
cGoodsNo	varchar(50)	Unchecked          ��Ʒ���
cGoodsName	varchar(50)	Unchecked          ��Ʒ����
Num	 money	Unchecked                      ����
Last_Price	money	Unchecked              ���ռ۸�
Last_Money	nchar(10)	Unchecked              С��
 * @author Administrator
 *
 */
public class Order_Details {
	private String cSheetno;
	private String cGoodsNo;
	private String cGoodsName;
	private String Num;
	private String Last_Price;
	private String Last_Money;
	private String cGoodsImagePath;
	
	
	public String getcGoodsImagePath() {
		return cGoodsImagePath;
	}
	public void setcGoodsImagePath(String cGoodsImagePath) {
		this.cGoodsImagePath = cGoodsImagePath;
	}
	public Order_Details(String cSheetno, String cGoodsNo, String cGoodsName,
			String num, String last_Price, String last_Money) {
		super();
		this.cSheetno = cSheetno;
		this.cGoodsNo = cGoodsNo;
		this.cGoodsName = cGoodsName;
		this.Num = num;
		this.Last_Price = last_Price;
		this.Last_Money = last_Money;
	}
	public Order_Details() {
		super();
	}
	@Override
	public String toString() {
		return "Order_Details [cSheetno=" + cSheetno + ", cGoodsNo=" + cGoodsNo
				+ ", cGoodsName=" + cGoodsName + ", Num=" + Num
				+ ", Last_Price=" + Last_Price + ", Last_Money=" + Last_Money
				+ "]";
	}
	public String getcSheetno() {
		return cSheetno;
	}
	public void setcSheetno(String cSheetno) {
		this.cSheetno = cSheetno;
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
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}
	public String getLast_Price() {
		return Last_Price;
	}
	public void setLast_Price(String last_Price) {
		Last_Price = last_Price;
	}
	public String getLast_Money() {
		return Last_Money;
	}
	public void setLast_Money(String last_Money) {
		Last_Money = last_Money;
	}
	
	
}
