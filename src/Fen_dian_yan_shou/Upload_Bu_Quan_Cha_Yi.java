package Fen_dian_yan_shou;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.String_Tool;

@WebServlet(description = "商品信息补全", urlPatterns = { "/Upload_Bu_Quan_Cha_Yi" })
public class Upload_Bu_Quan_Cha_Yi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String cStoreNo = request.getParameter("cStoreNo");
		String cGoodsNo = request.getParameter("cGoodsNo");
		String data=request.getParameter("data");
		Connection conn = null;
		CallableStatement c = null;
		try {
			conn = GetConnection.getStoreConn();
			if(!String_Tool.isEmpty(data)){
				JSONArray array=new JSONArray(data);
				for(int i=0;i<array.length();i++){
					c = conn.prepareCall("{call  p_cStoreInGoods (?,?)}");
					c.setString(1, cStoreNo);
					c.setString(2, cGoodsNo);
					c.execute();
				}
			}
			else{
				c = conn.prepareCall("{call  p_cStoreInGoods (?,?)}");
				c.setString(1, cStoreNo);
				c.setString(2, cGoodsNo);
				c.execute();
			}
			out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"" + "}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			Logger.getLogger(Upload_Bu_Quan_Cha_Yi.class).error(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			DB.closeRs_Con(null, c, conn);
		}
		out.flush();
		out.close();
	}
}
