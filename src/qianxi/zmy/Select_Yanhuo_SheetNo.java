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

import DB.GetConnection;
import Tool.ResultSet_To_JSON;

/**
 * Servlet implementation class Select_Yanhuo_SheetNo
 * 
 * 此处是根据仓库编号  找到未审核的验货单
 */
@WebServlet(description="此处是根据仓库编号  找到未审核的验货单",urlPatterns={"/Select_Yanhuo_SheetNo"})
public class Select_Yanhuo_SheetNo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		
		PrintWriter out = response.getWriter();
		
		String cWhNo=request.getParameter("cWhNo");
		String BGtime=request.getParameter("BGtime");
		String EDtime=request.getParameter("EDtime");
		
		if(cWhNo!=null && BGtime!=null && EDtime!=null){
			
			PreparedStatement past=null;
			ResultSet rs=null;
			Connection conn=GetConnection.getStoreConn();
			try{
				past=conn.prepareStatement("SELECT dDate,cSheetno,cSheetno_stock,cSupplierNo,cSupplier,cOperatorNo,cOperator,cFillEmpNo,cFillEmp,dFillin, "+
	"cFillinTime,iDays,cStockDptno,cStockDpt,fMoney,cManagerNo,cManager,bAgree,cExaminerNo,cExaminer,bExamin,"+
	"cWhNo,cWh,bPost,cTime,cReason,bBalance,jiesuanno,bDone,bReceive,cBeizhu1,cBeizhu2,cOperator_FinanceCheck,"+
	"bReason_FinanceCheck,cReason_FinanceCheck,dDate_FinanceCheck,cOperatorNo_FinanceCheck,cStoreNo,cStoreName"+
	" FROM WH_StockVerify "+
	" where bExamin<>1  AND cWhNo = ? AND (dDate BETWEEN ? AND ?)  ");
				past.setString(1, cWhNo);
				past.setString(2, BGtime);
				past.setString(3, EDtime);
				rs=past.executeQuery();
				JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
				if(array!=null&&array.length()>0){
					out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				}
				else{
					out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
				}
				
			}catch (Exception e) {
				e.printStackTrace();
				out.print("{\"resultStatus\":\"" + 2 + "\"," + "\"dDate\":" +null + "}");
			}
			finally{
				DB.DB.closeResultSet(rs);
				DB.DB.closePreparedStatement(past);
				DB.DB.closeConn(conn);
			}
			
		}else{
			out.print("{\"resultStatus\":\"" + -1 + "\"," + "\"dDate\":" + null + "}");
		}
		
		out.flush();
		out.close();
	}

}
