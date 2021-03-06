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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;

@WebServlet(description = "查询总部商品", urlPatterns = { "/Select_Zong_Bu_Goods" })
public class Select_Zong_Bu_Goods extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
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
			LoggerUtil.info(Number_of_pages);
			LoggerUtil.info(cStoreNo);
			String sql=String_Tool.sql("cGoodsNo", "select a.cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=isnull(b.EndQty,0),a.fPreservationUp,a.fPreservationDown,a.fPreservation_soft,bStorage  from t_Goods a left join (select cGoodsNo,EndQty from t_goodsKuCurQty_wei where cStoreNo=? ) b on a.cGoodsNo=b.cGoodsNo where cBarcode not like '%X%'", Integer.parseInt(Number_of_pages));
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
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
