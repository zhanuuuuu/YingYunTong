package Head_shop;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;

@WebServlet(description = "做箱码关联", urlPatterns = { "/Box_code_associated" })
public class Box_code_associated extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data").replace("\n", "").replace("\r", "");
		LoggerUtil.info(data);
		Connection conn = GetConnection.getStoreConn();
		try {
			JSONArray array = new JSONArray(data);
			String sql = "update t_Goods set cGoodsNo_minPackage_tmp=?, fQty_minPackage=? where cGoodsNo=? and ISNULL(cGoodsNo_minPackage,'')=''";
			PreparedStatement past = conn.prepareStatement(sql);
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj=array.getJSONObject(i);
				past.setString(1, obj.getString("cGoodsNo_minPackage_tmp"));// 店内小马
				past.setString(2, obj.getString("fQty_minPackage"));// 箱包系数
				past.setString(3, obj.getString("cGoodsNo"));// 箱码店内码
				past.addBatch();
			}
			past.executeBatch();
			out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
