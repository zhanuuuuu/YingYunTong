package qianxi.zmy;

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

/**
 * Servlet implementation class driverSelectRTSheetno
 */
@WebServlet(description = "˾����ѯ�ŵ��Ѿ��ύ�ĵ��ӣ��ŵ���Աȷ�ϵĵ��ݣ�", urlPatterns = { "/driverSelectRTSheetno" })
public class driverSelectRTSheetno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public driverSelectRTSheetno() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String sheetno=request.getParameter("cSheetno");
		
		String cStoreNo=request.getParameter("cStoreNo");
		Connection conn=null;
		PreparedStatement past=null;
		ResultSet rs=null;
		
		try {
			conn=GetConnection.getStoreConn();
			if (sheetno!=null || cStoreNo!=null) {
				
				String sql = " { call selectDriverGetRGS (?,?)}"; // //
				past = conn.prepareStatement(sql);
				
				if(sheetno==null){       //������Ϊ��ʱ   ��ѯ�����ŵ��ȫ��
					past.setString(1, cStoreNo);
					past.setString(2, "");
				}else{                   //�����Ų�Ϊ��ʱ   ��ѯ���Ǿ���ĵ���
					past.setString(1, "");
					past.setString(2, sheetno);
				}
				
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				DB.closePreparedStatement(past);
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				
			} 
			else {
				out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
			}
		
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + -2 + "\"," + "\"dDate\":" + null + "}");  //����ϵͳ�쳣
			e.printStackTrace();
		}finally {
			DB.closeRs_Con(rs, null, conn);
			out.flush();
			out.close();
		}
		
	}

}
