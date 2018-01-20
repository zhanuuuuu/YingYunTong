package Fen_dian_yan_shou;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import DB.DB;
import DB.GetConnection;
import Tool.GetcSheetno;
import Tool.ReadConfig;
import Tool.String_Tool;

@WebServlet(description = "批量修改价格", urlPatterns = { "/Men_Dian_Batch_Update_price" })
public class Men_Dian_Batch_Update_price extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = GetConnection.getStoreConn();
		String data = request.getParameter("data");
		System.out.println(data);
		String strrandom = "";
		try {
			JSONArray array = new JSONArray(data);
			JSONObject obj = array.getJSONObject(0);
			conn.setAutoCommit(false);
			/* 取单号正常验货单单号 */

			strrandom = GetcSheetno.get_commonality_cSheetNo(conn,
					"select SheetNo=" + new ReadConfig().getprop().getProperty("DataSource")
							+ ".dbo.f_GetStoreUpdatefPriceSheetno('" + String_Tool.DataBaseYear() + "','"
							+ obj.getString("cSupplierNo") + "')");

			PreparedStatement past_detail = conn.prepareStatement(
					"insert into t_StoreGoodsUpdateDetail(cSheetNo,iLineNo,cGoodsNo,cGoodsName,cBarcode,fNormalPrice,fCKPrice,fPfPrice,fPsprice,fDbPrice,fNormalPrice_old,fPfPrice_old,cUnit,cSpec)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				past_detail.setString(1,"" + strrandom);
				past_detail.setString(2,"" + (i + 1));
				past_detail.setString(3,obj1.getString("cGoodsNo"));
				past_detail.setString(4,obj1.getString("cGoodsName"));
				past_detail.setString(5,obj1.getString("cBarcode"));
				past_detail.setString(6,obj1.getString("fNormalPrice"));
				past_detail.setString(7,"");//fCKPrice
				past_detail.setString(8,"");//fPfPrice
				past_detail.setString(9,"");//fPsprice
				past_detail.setString(10,"");//fDbPrice
				past_detail.setString(11,"");//fNormalPrice_old
				past_detail.setString(12,"");//fPfPrice_old
				past_detail.setString(13,obj.getString("cUnit"));
				past_detail.setString(14,obj.getString("cSpec"));
				past_detail.addBatch();
				if (i % 200 == 0) {
					past_detail.executeBatch();
				}
			}
			past_detail.executeBatch();
			DB.closePreparedStatement(past_detail);

			PreparedStatement past = conn.prepareStatement("insert into t_StoreGoodsUpdate(dDate,cSheetNo,cStoreNo ,cStoreName  ,cOperatorNo ,cOperatorName,bExamin ,dDate_exe ,cExaminerNo,cExaminerName ,bStore ,dExaminDate)values(?,?,?,?,?,?,?,?,?,?,?,?)");
			past.setString(1, String_Tool.DataBaseYear_Month_Day());
			past.setString(2, strrandom);
			past.setString(3, obj.getString("cStoreNo"));
			past.setString(4, obj.getString("cStoreName"));
			past.setString(5, obj.getString("cOperatorNo"));
			past.setString(6, obj.getString("cOperatorName"));
			past.setString(7, obj.getString("bExamin"));
			past.setString(8, String_Tool.DataBaseYear_Month_Day());//obj.getString("dDate_exe")
			past.setString(9, "");
			past.setString(10, "");
			past.setString(11, "0");//obj.getString("bStore")
			past.setString(12, "");
			past.execute();
			DB.closePreparedStatement(past);
			out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			conn.commit();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DB.closeConn(conn);
			out.flush();
			out.close();

		}
	}

}
