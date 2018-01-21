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


@WebServlet(description = "��Ʒ�Ƽ�", urlPatterns = { "/Upload_New_Products" })
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
		// �����ϴ��ļ��Ĵ�С�����������С ���׳�IOException�쳣��Ĭ�ϴ�С��10M��
		int inmaxPostSize = 10 * 1024 * 1024;
		MultipartRequest multirequest = null;

		// �ϴ��ļ�����������
		RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();
		try {
			multirequest = new MultipartRequest(request, fileDir.getAbsolutePath(), inmaxPostSize, "UTF-8",myRenamePolicyCos); // GBK���ı���ģʽ�ϴ��ļ� //���е��������
		
			String data = multirequest.getParameter("data");// ��ȡ��ͨ��Ϣ
			System.out.println(""+data);
			JSONArray array=new JSONArray(data);
			Enumeration<String> filedFileNames = multirequest.getFileNames();
			String filedName = null;
			if (null != filedFileNames) {
				while (filedFileNames.hasMoreElements()) {
					filedName = filedFileNames.nextElement();// �ļ��ı��������
					// ��ȡ���ļ������ϴ����ļ�������Ӧ���ϴ����������е��ļ�
					File uploadFile = multirequest.getFile(filedName);
					// ��ȡδ���������ļ�����
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
