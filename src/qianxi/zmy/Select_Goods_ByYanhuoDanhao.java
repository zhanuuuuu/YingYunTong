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

import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class Select_Goods_ByYanhuoDanhao
 */
@WebServlet(description = "根据验货单号查询出来商品", urlPatterns = { "/Select_Goods_ByYanhuoDanhao" })
public class Select_Goods_ByYanhuoDanhao extends HttpServlet {
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
		
		if(cSheetno!=null){
			
			PreparedStatement past=null;
			ResultSet rs=null;
			Connection conn=GetConnection.getStoreConn();
			try{
				past=conn.prepareStatement("SELECT A.cSheetno,A.cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fQuantity,fInPrice,fInMoney,B.cStoreNo,C.EndQty FROM WH_StockVerifyDetail  A  "+
				"	INNER JOIN WH_StockVerify B ON A.cSheetno=B.cSheetno AND A.cSheetno= ?  "+
				"	INNER JOIN t_goodsKuCurQty_wei C ON C.cStoreNo=B.cStoreNo AND C.cGoodsNo=A.cGoodsNo ");
				past.setString(1, cSheetno);
				rs=past.executeQuery();
				JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);	
				if(array!=null&&array.length()>0){
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				}
				else{
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				DB.DB.closeResultSet(rs);
				DB.DB.closePreparedStatement(past);
				DB.DB.closeConn(conn);
			}
			
		}else{
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
		}
		
		out.flush();
		out.close();
	}

}
