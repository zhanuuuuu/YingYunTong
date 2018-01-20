package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import DB.Fen_dian_Update;
import DB.GetConnection;

/**
 * Servlet implementation class select_Supper_Goods_SheetNo
 * 供应商   单号   商品
 */
@WebServlet("/select_Supper_Goods_SheetNo")
public class select_Supper_Goods_SheetNo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String dBeginDate = request.getParameter("dBeginDate");
			String dEndDate = request.getParameter("dEndDate"); //
			String cStoreNo = request.getParameter("cStoreNo"); //
			
			System.out.println(dBeginDate);
			System.out.println(dEndDate);
			System.out.println(cStoreNo);

			JSONArray array = Fen_dian_Update.select_Supper_Goods_SheetNo_function(GetConnection.getStoreConn(), cStoreNo,
					dBeginDate, dEndDate);

			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" + "" + "\"}");

			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
		
	}

}
