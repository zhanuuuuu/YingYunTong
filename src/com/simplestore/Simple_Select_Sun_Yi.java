package com.simplestore;
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

@WebServlet(description = "�����ѯ������", urlPatterns = { "/Simple_Select_Sun_Yi" })
public class Simple_Select_Sun_Yi extends HttpServlet {
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
		try {
			String cSheetno = request.getParameter("cSheetno");
			String dDate1 = request.getParameter("dDate1");
			String dDate2 = request.getParameter("dDate2");
			String fage = request.getParameter("fage"); // "0�Ǳ��� 1�ǰ���"
			LoggerUtil.info("0�Ǳ��� 1�ǰ���");
			LoggerUtil.info(cSheetno);
			LoggerUtil.info(dDate1);
			LoggerUtil.info(dDate2);
			LoggerUtil.info(fage);
			JSONArray array=new JSONArray();
			if (fage.equals("0")) {
				array = Select_Bao_Sun(GetConnection.getStoreConn(), cSheetno,dDate1,dDate2);
			}
			else {
				array = Select_Bao_Yi(GetConnection.getStoreConn(), cSheetno,dDate1,dDate2);
			}
			if (array.length() > 0) {
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString() + "}");
				LoggerUtil.info("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString() + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString() + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
	
	public static JSONArray Select_Bao_Sun(Connection conn, String cSheetno, String beginDate, String endDate) { // ��ѯ�Ѿ���˵ķֵ����յ�,���ǻ�û���
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			if (String_Tool.isEmpty(cSheetno)) { // �ܲ���
				past = conn.prepareStatement(" select dDate,cSheetno,cSupplierNo,cSupplier,cOperator,cOperatorNo from wh_LossWarehouse  where dDate between ?  and ? ");
				past.setString(1, beginDate);
				past.setString(2, endDate);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			} else { // �ŵ���
				past = conn.prepareStatement(" select * from wh_LossWarehouseDetail where cSheetno=? ");
				past.setString(1, cSheetno);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}
	public static JSONArray Select_Bao_Yi(Connection conn, String cSheetno, String beginDate, String endDate) { // ��ѯ�Ѿ���˵ķֵ����յ�,���ǻ�û���
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			if (String_Tool.isEmpty(cSheetno)) { // �ܲ���
				past = conn.prepareStatement(" select dDate,cSheetno,cSupplierNo,cSupplier,cOperator,cOperatorNo from wh_EffusionWh where dDate between ?  and ? ");
				past.setString(1, beginDate);
				past.setString(2, endDate);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			} else { // �ŵ���
				past = conn.prepareStatement(" select * from wh_EffusionWhDetail where cSheetno=? ");
				past.setString(1, cSheetno);
				rs = past.executeQuery();
				JSONArray array = ResultSet_To_JSON.resultSetToJsonArray(rs);
				return array;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.closeResultSet(rs);
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
		return null;
	}
}
