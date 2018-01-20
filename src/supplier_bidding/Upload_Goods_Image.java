package supplier_bidding;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import Tool.RenamePolicyCos;
@WebServlet(description = "提交商品图片", urlPatterns = { "/Upload_Goods_Image" })
public class Upload_Goods_Image extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
	

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("content-type", "text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		File fileDir = new File(this.getServletContext().getRealPath("/goods_bidding_Img"));
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		try {
			RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();
			MultipartRequest multirequest = new MultipartRequest(request, fileDir.getAbsolutePath(), 10 * 1024 * 1024, "UTF-8",myRenamePolicyCos);
			out.print(" {\"resultStatus\":\"1" + "\"}");
		} catch (Exception e) {
			out.print(" {\"resultStatus\":\"-1" + "\"}");
			e.printStackTrace();
		}
	
		out.flush();
		out.close();
	}
}
