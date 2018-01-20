package Online_Manager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;


@WebServlet(description = "ËÑË÷ÃÅµêÉÌÆ·", urlPatterns = { "/Search_cStore_Goods" })
public class Search_cStore_Goods extends HttpServlet {
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
		Connection conn=GetConnection.getStoreConn();
		PreparedStatement past=null;
		ResultSet rs=null;
		try {
		    String cStoreNo=request.getParameter("cStoreNo");
		    String goods=request.getParameter("goods");
		    past=conn.prepareStatement("select cGoodsNo,cGoodsName, cBarcode,cStoreNo,cStoreName,cUnit,cSpec fNormalPrice,fVipPrice,bOnLine,bOnLine_Price from t_cStoreGoods where (cBarcode=? or cGoodsNo=? or cGoodsName=? ) and cStoreNo=? ");
		    past.setString(1, goods);
		    past.setString(2, goods);
		    past.setString(3, goods);
		    past.setString(4, cStoreNo);
		    rs=past.executeQuery();
		    JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if (array != null && array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":"+ array.toString() + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":"+ "0" + "}");
			}
			System.out.println(array.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	}
}
