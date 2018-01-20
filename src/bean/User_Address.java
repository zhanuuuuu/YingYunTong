package bean;
/**
 * User_Address                      �û���ַ��
AddressID  varchar(20)                         ��ַID
UserNo	varchar(20)	Checked                 �û����
UserName	varchar(50)	Checked             �û�����
Tel	varchar(20)	Checked                     �û��ֻ���
Provincial	 nchar(10)	Checked                 ʡ
City	nchar(10)	Checked                         ��
District	nchar(10)	Checked                     ��
Detailaddress	varchar(250)	Checked             ��ϸ��ַ
Default_fage   nchar(10)                      �Ƿ�����Ĭ��1 ��Ĭ�ϵ�ַ��0�Ƿ�Ĭ��
Available  varchar(10) 1�ǿ��ã�0�ǲ����ã�ɾ���û���ַ��ʱ��ֱ������Ϊ0��������ɾ����ַ
 * @author Administrator
 *
 */
public class User_Address {
	private String AddressID;
	private String UserNo;
	private String UserName;
	private String Tel;
	private String Provincial;
	private String City;
	private String District;
	private String Detailaddress;
	private String Default_fage;
	private String Available;
	public String getAddressID() {
		return AddressID;
	}
	public void setAddressID(String addressID) {
		AddressID = addressID;
	}
	public String getUserNo() {
		return UserNo;
	}
	public void setUserNo(String userNo) {
		UserNo = userNo;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getTel() {
		return Tel;
	}
	public void setTel(String tel) {
		Tel = tel;
	}
	public String getProvincial() {
		return Provincial;
	}
	public void setProvincial(String provincial) {
		Provincial = provincial;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public String getDetailaddress() {
		return Detailaddress;
	}
	public void setDetailaddress(String detailaddress) {
		Detailaddress = detailaddress;
	}
	public String getDefault_fage() {
		return Default_fage;
	}
	public void setDefault_fage(String default_fage) {
		Default_fage = default_fage;
	}
	public String getAvailable() {
		return Available;
	}
	public void setAvailable(String available) {
		Available = available;
	}
	public User_Address(String addressID, String userNo, String userName,
			String tel, String provincial, String city, String district,
			String detailaddress, String default_fage, String available) {
		super();
		this.AddressID = addressID;
		this.UserNo = userNo;
		this.UserName = userName;
		this.Tel = tel;
		this.Provincial = provincial;
		this.City = city;
		this.District = district;
		this.Detailaddress = detailaddress;
		this.Default_fage = default_fage;
		this.Available = available;
	}
	public User_Address() {
		super();
	}
	@Override
	public String toString() {
		return "User_Address [AddressID=" + AddressID + ", UserNo=" + UserNo
				+ ", UserName=" + UserName + ", Tel=" + Tel + ", Provincial="
				+ Provincial + ", City=" + City + ", District=" + District
				+ ", Detailaddress=" + Detailaddress + ", Default_fage="
				+ Default_fage + ", Available=" + Available + "]";
	}
	
	
}
