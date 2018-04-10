package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Select_Online_Manager;

@WebServlet(description = "��ѯ�ŵ궩��", urlPatterns = { "/Select_Men_Dian_Order" })
public class Select_Men_Dian_Order extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String cStoreNo = request.getParameter("cStoreNo");
			String start = request.getParameter("starttime");
			String end = request.getParameter("endtime");
			String Pay_state = request.getParameter("Pay_state"); // ����״̬��0�Ǵ����1���Ѹ����������2�Ǵ��ջ���3�����
																	//4�ѱ���
			LoggerUtil.info(cStoreNo);
			LoggerUtil.info(start);
			LoggerUtil.info(Pay_state);
			String str = Select_Online_Manager.select_Order(GetConnection.getStoreConn(), Pay_state, cStoreNo, start,
					end);
			out.print(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
