package Goods_flow_Servlet;

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

@WebServlet(description = "查询门店商品基本信息", urlPatterns = { "/Select_Men_Dian_Goods" })
public class Select_Men_Dian_Goods extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getStoreConn();
		String Number_of_pages = request.getParameter("Number_of_pages");
		String cStoreNo = request.getParameter("cStoreNo");
		PreparedStatement past = null;
		ResultSet rs = null;
		String sql="";
		try {
			LoggerUtil.info(Number_of_pages);
			if (String_Tool.isEmpty(Number_of_pages)) {
				 sql = "select cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=0,fPreservationUp,fPreservationDown,fPreservation_soft,cSupNo, cSupName , bStorage from t_cStoreGoods where  cStoreNo=? and ISNull(bStorage,0)='0'  and bDeled='0'  ";
                LoggerUtil.info("查询所有的商品");
			} else {
				sql = String_Tool.sql("cGoodsNo",
						"select cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=0,fPreservationUp,fPreservationDown,fPreservation_soft,cSupNo, cSupName ,bStorage from t_cStoreGoods where  cStoreNo=? and ISNull(bStorage,0)='0' and bDeled='0'  ",
						Integer.parseInt(Number_of_pages));
				LoggerUtil.info("分页查询商品");
			}
			LoggerUtil.info(sql);
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
			rs = past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + 0 + "\"}");
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(rs, past, conn);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
