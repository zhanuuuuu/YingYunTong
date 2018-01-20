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

public class Diao_bo_chu_ku extends HttpServlet {
	
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("data");
		
		String cBeizhu1 = request.getParameter("cBeizhu1");
		System.out.println(cBeizhu1);
		System.out.println(""+data);
		boolean a = false;
		try {
			JSONArray array = new JSONArray(data);
			if(cBeizhu1!=null){
				a =Fen_dian_Update.Insert_into_Diao_bo_chu_ku(GetConnection.getStoreConn(), array,cBeizhu1);	
			}else{
				a =Fen_dian_Update.Insert_into_Diao_bo_chu_ku(GetConnection.getStoreConn(), array);
			}
			
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
				System.out.println("提交成功");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
				System.out.println("提交失败");
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
