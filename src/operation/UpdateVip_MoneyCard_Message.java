package operation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Operation_update;


@WebServlet(description = "修改储值卡信息", urlPatterns = { "/UpdateVip_MoneyCard_Message" })
public class UpdateVip_MoneyCard_Message extends HttpServlet {
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
		String fage=request.getParameter("fage");
		String data=request.getParameter("data");
		try {
			int a=0;
			JSONObject obj=new JSONObject(data);
			if(fage.equals("1")){
				a = Operation_update.Update_Vip_message(GetConnection.getStoreConn(),obj);
			}
			else{
				a = Operation_update.Update_Money_card_message(GetConnection.getStoreConn(),obj);
			}
			out.print("{\"resultStatus\":\"" + a + "\"}");
			LoggerUtil.info("{\"resultStatus\":\"" + a + "\"}");
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
