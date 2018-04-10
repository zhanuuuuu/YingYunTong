package com.qian.xi.function;
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

import com.cloopen.rest.sdk.utils.LoggerUtil;

import DB.DB;
import DB.GetConnection;
import Tool.ResultSet_To_JSON;
import Tool.String_Tool;


@WebServlet(description = "查询配送出库单", urlPatterns = { "/Select_Qian_Xi_Pei_Song_Chu_ku_Dan" })
public class Select_Qian_Xi_Pei_Song_Chu_ku_Dan extends HttpServlet {
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
		PreparedStatement past=null;
		ResultSet rs=null;
		Connection conn=GetConnection.getStoreConn();
		String cSheetno=request.getParameter("cSheetno");
		String cStoreNo=request.getParameter("cStoreNo");
		LoggerUtil.info(cStoreNo+"店铺编号为空查具体单子的内容");
		LoggerUtil.info(cSheetno+"单号为空查店铺的出库单");
		try{
			if(String_Tool.isEmpty(cSheetno)){
				past=conn.prepareStatement("select dDate,cSheetno,cCustomerNo,cCustomer,cOperatorNo,cOperator FROM wh_cStoreOutWarehouse where dDate>GETDATE()-15 and isnull(bReceive,0)='0' and bExamin='1' and cCustomerNo=?  ");
				past.setString(1, cStoreNo);
				rs=past.executeQuery();
			}else{
				past=conn.prepareStatement("select iLineNo,cGoodsNo,cGoodsName,cBarcode,cUnitedNo,fQuantity,fInPrice,fInMoney,fTaxPrice,cUnit,cSpec,a.cStoreNo,a.cStoreName,a.cWhNo,a.cWh,a.cCustomerNo,a.cCustomer FROM wh_cStoreOutWarehouse a ,wh_cStoreOutWarehouseDetail b  where a.cSheetno=b.cSheetno and b.cSheetno=?");
				past.setString(1, cSheetno);
				rs=past.executeQuery();
			}
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array!=null&&array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString() + "}");
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		out.flush();
		out.close();
	}

}
