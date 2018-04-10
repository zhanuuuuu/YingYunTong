package Price_Policy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB_Price_Policy;
import DB.GetConnection;

public class Upload_discount extends HttpServlet {

	
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
		String data=request.getParameter("data");
		LoggerUtil.info(data);
		JSONArray array;
		try {
			array = new JSONArray(data);
			boolean a=DB_Price_Policy.Insert_Into_discount(GetConnection.getStoreConn(), array);
			if(a){
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}	
		} catch (JSONException e) {

			e.printStackTrace();
		}
	
	}

}
