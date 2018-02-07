package qianxi.zmy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class ShenHe_YanhuoDan
 */
@WebServlet(description = "根据单号审核验货单（手机审核功能）", urlPatterns = { "/ShenHe_YanhuoDan" })
public class ShenHe_YanhuoDan extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		
		PrintWriter out = response.getWriter();
		
		String cSheetno=request.getParameter("cSheetno");
		String cOperatorNo = request.getParameter("cOperatorNo");
		String cOperator = request.getParameter("cOperator");
		String data = request.getParameter("data");
		String cStoreNO = request.getParameter("cStoreNO");
		
		System.out.println(cSheetno);
		System.out.println(cStoreNO);
		Double inMoney=0.0;
		
		if(cSheetno!=null || cOperatorNo!=null || cOperator!=null || data!=null || cStoreNO!=null){
			
			Connection conn=GetConnection.getStoreConn();
			PreparedStatement past=null;
			try{
				conn.setAutoCommit(false);
				JSONArray array=new JSONArray(data);
				
				PreparedStatement ps = conn.prepareStatement("UPDATE wh_StockVerifyDetail "+
						" SET fQuantity=?,fInPrice=?,fInMoney=?,fTaxrate=?,bTax=?,fTaxPrice=?, "+
						" fTaxMoney=?,fNoTaxPrice=?,fNoTaxMoney=?,fPacks=? "+
						" WHERE cSheetno=? AND cGoodsNo=?   ");
				
				for(int i=0;i<array.length();i++){
					
					JSONObject obj=array.getJSONObject(i);
					System.out.println(obj.getBoolean("isModefy"));
					inMoney=inMoney+Double.parseDouble(obj.getString("fInMoney"));
					String fNormalPrice="0.0";
					if(obj.getBoolean("isModefy")){
					
						PreparedStatement past2 = conn.prepareStatement("select fNormalPrice,cStoreNo,cGoodsNo from t_cStoreGoods where cGoodsNo=? AND cStoreNo=?  ");
						past2.setString(1, obj.getString("cGoodsNo"));
						past2.setString(2, cStoreNO);
						ResultSet rs2 = past2.executeQuery();
						if (rs2.next()) {
							fNormalPrice = rs2.getString("fNormalPrice");
						}
						DB.closeResultSet(rs2);
						DB.closePreparedStatement(past2);
						
						ps.setDouble(1,Double.parseDouble(obj.getString("fQuantity")));
						ps.setDouble(2,Double.parseDouble(obj.getString("fInPrice")));
						ps.setDouble(3,Double.parseDouble(obj.getString("fInMoney")));
						double fTaxrate = (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj.getString("fInPrice")))
								/ Double.parseDouble(obj.getString("fInPrice"));
						ps.setString(4, "" + fTaxrate);
						ps.setString(5, "1");
						String fTaxPrice = ""
								+ (Double.parseDouble(fNormalPrice) - Double.parseDouble(obj.getString("fInPrice")));
						ps.setString(6, "" + fTaxPrice);
						String fTaxMoney = ""
								+ (Double.parseDouble(fTaxPrice) * Double.parseDouble(obj.getString("fQuantity")));
						ps.setString(7, fTaxMoney);
						
						ps.setString(8, "" + fNormalPrice);// obj1.getString("fNoTaxPrice")
						ps.setString(9,
								"" + (Double.parseDouble(fNormalPrice) * Double.parseDouble(obj.getString("fQuantity"))));
						
						ps.setString(10, "1");
						ps.setString(11,cSheetno);
						ps.setString(12,obj.getString("cGoodsNo"));
						
						ps.addBatch();
						if(i%200==0){
							ps.executeBatch();
						}
						
						ps.executeBatch();
						DB.closePreparedStatement(ps);
					}
					
					
					
				}
			
				past=conn.prepareStatement("UPDATE WH_StockVerify "+
										"	SET bExamin=1,cExaminerNo=?,cExaminer=?,fMoney=?     "+
										"	WHERE cSheetno=? ");
				past.setString(1, cOperatorNo);
				past.setString(2, cOperator);
				past.setString(3, ""+inMoney);
				past.setString(4, cSheetno);
				
				int updateCount=past.executeUpdate();
				
				if(updateCount>0){
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + cSheetno + "}");  
				}
				else{
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + cSheetno + "}");  
				}
				conn.commit();
				conn.setAutoCommit(true);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				
				DB.closePreparedStatement(past);
				DB.closeConn(conn);
			}
			
		}else{
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
		}
		
		out.flush();
		out.close();
		
	}

}
