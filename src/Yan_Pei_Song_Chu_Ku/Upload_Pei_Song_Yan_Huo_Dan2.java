package Yan_Pei_Song_Chu_Ku;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBYan_Huo_update;
import DB.GetConnection;

@WebServlet(description = "ÅäËÍ³ö¿âÑé»õ", urlPatterns = { "/Upload_Pei_Song_Yan_Huo_Dan2" })
public class Upload_Pei_Song_Yan_Huo_Dan2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String goods_array_str = request.getParameter("goods_array");
		String pallet_array_str = request.getParameter("pallet_array");
		String cha_yi_array_str=request.getParameter("cha_yi_array");
		String chu_ku_no_array_str=request.getParameter("chu_ku_no_array");
		LoggerUtil.info(goods_array_str);
		LoggerUtil.info(pallet_array_str);
		LoggerUtil.info(cha_yi_array_str);
		LoggerUtil.info(chu_ku_no_array_str);
		try {
			LoggerUtil.info("½²¹þ");
			JSONArray goods_array = new JSONArray(goods_array_str);
			JSONArray pallet_array =  new JSONArray(pallet_array_str);
			JSONArray cha_yi_array=new JSONArray(cha_yi_array_str);
			JSONArray chu_ku_no_array=new JSONArray(chu_ku_no_array_str);
			boolean a = DBYan_Huo_update.insert_into_yan_huo_dan(GetConnection.getStoreConn(), goods_array, pallet_array,cha_yi_array,chu_ku_no_array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"" + "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
