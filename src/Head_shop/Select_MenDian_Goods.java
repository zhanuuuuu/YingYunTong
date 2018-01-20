package Head_shop;

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

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "查询门店商品", urlPatterns = { "/Select_MenDian_Goods" })
public class Select_MenDian_Goods extends HttpServlet {
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
		Connection conn= GetConnection.getStoreConn();
		String Number_of_pages=request.getParameter("Number_of_pages");
		String cStoreNo=request.getParameter("cStoreNo");
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			System.out.println(Number_of_pages);
			System.out.println(cStoreNo);
			//--and  isnull(bStorage,0)=1  and ISNUll(bUnStock,0)='0'
			String sql=String_Tool.sql("cGoodsNo", "select a.cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=isnull(b.EndQty,0),a.fPreservationUp,a.fPreservationDown,a.fPreservation_soft,bStorage  from t_cStoreGoods a left join (select cGoodsNo,EndQty from t_goodsKuCurQty_wei where cStoreNo=? ) b on a.cGoodsNo=b.cGoodsNo where a.cStoreNo=? and cBarcode not like '%X%'   and ISNUll(bDeled,0)='0'  ", Integer.parseInt(Number_of_pages));
			System.out.println(sql);
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
			past.setString(2, cStoreNo);
		    rs=  past.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" +0 + "\"}");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(rs, past, conn);
		}
	}
}
