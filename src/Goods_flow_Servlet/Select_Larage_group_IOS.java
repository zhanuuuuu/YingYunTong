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
import org.json.JSONObject;

import DB.GetConnection;
import Tool.ResultSet_To_JSON;

@WebServlet(description = "查询所有的部租", urlPatterns = { "/Select_Larage_group_IOS" })
public class Select_Larage_group_IOS extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getStoreConn();
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			past = conn.prepareStatement("select cPath,cHomeAlias,cParentNo,cCategoryAlias,cMarket,bFresh,bOnLine,cGroupTypeName,ImagePath,cGroupTypeNo,cIMG from T_GroupType where cParentno='--' ");
			rs = past.executeQuery();
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			while (rs.next()) {
			    obj = new JSONObject();
				String cGroupTypeNo = rs.getString("cGroupTypeNo");
				String cGroupTypeName = rs.getString("cGroupTypeName");
				String bFresh = rs.getString("bFresh");
				obj.put("cGroupTypeNo", cGroupTypeNo);
				obj.put("cGroupTypeName", cGroupTypeName);
				obj.put("bFresh", bFresh);
				PreparedStatement past1 = conn.prepareStatement("select * from T_GroupType where cParentno=? ");
				past1.setString(1, cGroupTypeNo);
				ResultSet rs1 = past1.executeQuery();
				obj.put("list", ResultSet_To_JSON.resultSetToJsonArray(rs1));
				array.put(obj);
			}
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.DB.closeRs_Con(rs, past, conn);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
