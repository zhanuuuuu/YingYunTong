package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.Fen_dian_Update;
import DB.GetConnection;

public class Select_Bao_Sun_Bao_Yi_Goods extends HttpServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cStoreNo = request.getParameter("cSheetno");
			String fage = request.getParameter("fage"); // "0是报损 1是褒义"
		
			if (fage.equals("0")) {
				JSONArray array = Fen_dian_Update.Select_Bao_Sun_Goods(
						GetConnection.getStoreConn(), cStoreNo);

				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
				} 
			} else {
				JSONArray array = Fen_dian_Update.Select_Bao_Yi_Goods(
						GetConnection.getStoreConn(), cStoreNo);

				if (array != null && array.length() > 0) {
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
					System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
							+ array.toString() + "}");
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

	

}
