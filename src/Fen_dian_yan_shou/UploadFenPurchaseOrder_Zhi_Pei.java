package Fen_dian_yan_shou;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DBYan_Huo_update;
import DB.GetConnection;

@WebServlet(description = "提交采购验货单", urlPatterns = { "/UploadFenPurchaseOrder_Zhi_Pei" })

public class UploadFenPurchaseOrder_Zhi_Pei extends HttpServlet {
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
		String data = request.getParameter("data");
		String cSheetno=request.getParameter("cSheetno");
		LoggerUtil.info(data);
		try {
			JSONArray array = new JSONArray(data);
			boolean a = DBYan_Huo_update.uploadFenPurchaseOrder_Zhi_Pei(GetConnection.getStoreConn(), array,cSheetno);
			if (a) {
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 1 + "\""+ "}");
			} 
			else {
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\""+ "}");
				out.print("{\"resultStatus\":\"" + 0 + "\""+ "}");
			}
		} catch (Exception e) {
			LoggerUtil.info("{\"resultStatus\":\"" + "服务器异常" + "\""+ "}");
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
