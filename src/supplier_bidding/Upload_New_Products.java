package supplier_bidding;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import com.oreilly.servlet.MultipartRequest;
import DB.DB_Supplier_Bidding;
import DB.GetConnection;
import Tool.RenamePolicyCos;


@WebServlet(description = "新品推荐", urlPatterns = { "/Upload_New_Products" })
public class Upload_New_Products extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		File fileDir = new File(this.getServletContext().getRealPath("/New_goods_Img"));
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		// 设置上传文件的大小，超过这个大小 将抛出IOException异常，默认大小是10M。
		int inmaxPostSize = 10 * 1024 * 1024;
		MultipartRequest multirequest = null;

		// 上传文件重命名策略
		RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();
		try {
			multirequest = new MultipartRequest(request, fileDir.getAbsolutePath(), inmaxPostSize, "UTF-8",myRenamePolicyCos); // GBK中文编码模式上传文件 //所有的请求参数
		
			String data = multirequest.getParameter("data");// 获取普通信息
			System.out.println(""+data);
			JSONArray array=new JSONArray(data);
			Enumeration<String> filedFileNames = multirequest.getFileNames();
			String filedName = null;
			if (null != filedFileNames) {
				while (filedFileNames.hasMoreElements()) {
					filedName = filedFileNames.nextElement();// 文件文本框的名称
					// 获取该文件框中上传的文件，即对应到上传到服务器中的文件
					File uploadFile = multirequest.getFile(filedName);
					// 获取未重命名的文件名称
					String Originalname = multirequest.getOriginalFileName(filedName);
					System.out.println(Originalname);
					
					
					boolean a=DB_Supplier_Bidding.upload_New_Products(GetConnection.getBiddingConn(),array);
					if (a) {
						out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
								+ 1+ "}");
						System.out.println("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"
								+ 1+ "}");
					}
					else{
						out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
								+ 0 + "}");
						System.out.println("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"
								+ 0 + "}");
					}
				}
			}
		} catch (Exception e) {
			out.print(" {\"resultStatus\":\"-1" + "\"}");
			e.printStackTrace();
		} finally {
			out.print(" {\"resultStatus\":\"1" + "\"}");
			out.flush();
			out.close();
		}
	}
	

}
