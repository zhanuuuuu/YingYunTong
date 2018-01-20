package operation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DB.GetConnection;
import DB.Operation_update;
import Tool.ResultSet_To_JSON;

public class Testing extends HttpServlet {

	/**
	 *  Ñ¹Á¦²âÊÔµÄdemo
	 */
	private static final long serialVersionUID = 1L;


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn=GetConnection.getStoreConn();
		PreparedStatement past=null;
		ResultSet rs=null;
		try {
		    past=conn.prepareStatement("select top 600000 dSaleDate, cSaleSheetno, iSeed, cStoreNo, cGoodsNo, cGoodsName, cBarCode, cOperatorno, cOperatorName, cVipCardno, bAuditing, cChkOperno, cChkOper, bSettle, fVipScore, fPrice, fNormalSettle, bVipPrice, fVipPrice, bVipRate, fVipRate from dbo.t_SaleSheetDetail");
		    rs=past.executeQuery();
			JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":" + array.toString().replace(" ", "") + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":" + array.toString() + "}");
			}
		} catch (Exception e) {
		   e.printStackTrace();
		}
		finally{
			DB.DB.closeRs_Con(rs, past, conn);
		}
		out.flush();
		out.close();
	
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	
	

}
