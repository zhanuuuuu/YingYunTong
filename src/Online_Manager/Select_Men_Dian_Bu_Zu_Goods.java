package Online_Manager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.GetConnection;
import DB.Select_Online_Manager;


@WebServlet("/Select_Men_Dian_Bu_Zu_Goods")
public class Select_Men_Dian_Bu_Zu_Goods extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public Select_Men_Dian_Bu_Zu_Goods() {
        super();
   
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		try {
		    String cGoodsTypeNo=request.getParameter("cGoodsTypeNo");
		    String cStoreNo=request.getParameter("cStoreNo");
			JSONArray array = Select_Online_Manager.getGoods(GetConnection.getStoreConn(),cGoodsTypeNo,cStoreNo);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ "0" + "}");
			}
			LoggerUtil.info(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
