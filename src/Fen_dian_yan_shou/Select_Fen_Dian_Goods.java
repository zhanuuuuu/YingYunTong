package Fen_dian_yan_shou;
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

/**
 * Servlet implementation class Select_Fen_Dian_Goods
 */
@WebServlet(description = "查询门店商品", urlPatterns = { "/Select_Fen_Dian_Goods" })
public class Select_Fen_Dian_Goods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
//SELECT TOP 10000 *  FROM (SELECT ROW_NUMBER() OVER (ORDER BY  cGoodsNo)
//    AS RowNumber,* FROM  
//    (select  a.cGoodsNo,a.cGoodsName,a.cBarcode,a.cUnit,a.cSpec,a.fNormalPrice,a.fCKPrice, a.cSupNo,b.EndQty as fQty_Cur , 
//    a.fPreservationUp,a.fPreservationDown,a.fPreservation_soft from t_cStoreGoods a,t_goodsKuCurQty_wei b where 
//     a.cStoreNo='1001' and b.cStoreNo=a.cStoreNo and a.cGoodsNo=b.cGoodsNo ) as  Z ) as A WHERE RowNumber > 10000*(0)
//     //更新分店带库存
//     
    
    //更新门店不带库存
//     SELECT TOP 10000 *  FROM (SELECT ROW_NUMBER() OVER (ORDER BY  cGoodsNo) 
//     AS RowNumber,* FROM  
//     (select cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,fQty_Cur=0,fPreservationUp,fPreservationDown,fPreservation_soft
//      from t_cStoreGoods
//      where  cStoreNo='1001'  ) as  Z ) as A WHERE RowNumber > 10000*(0)
//    select  cGoodsNo,cGoodsName,cBarcode,cUnit,cSpec,fNormalPrice,fCKPrice,cSupNo,
//    fQty_Cur=0,fPreservationUp,fPreservationDown,fPreservation_soft 
//    from t_cStoreGoods where  cStoreNo='1001'
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn= GetConnection.getStoreConn();
		String Number_of_pages=request.getParameter("Number_of_pages");
		String cStoreNo=request.getParameter("cStoreNo");
		PreparedStatement past = null;
		ResultSet rs = null;
		try {
			LoggerUtil.info(Number_of_pages);
			String sql=String_Tool.sql("cGoodsNo", "select  a.cGoodsNo,a.cGoodsName,a.cBarcode,a.cUnit,a.cSpec,a.fNormalPrice,a.fCKPrice, a.cSupNo,b.EndQty as fQty_Cur , a.fPreservationUp,a.fPreservationDown,a.fPreservation_soft,bStorage from t_cStoreGoods a,t_goodsKuCurQty_wei b where  a.cStoreNo=? and b.cStoreNo=a.cStoreNo and a.cGoodsNo=b.cGoodsNo", Integer.parseInt(Number_of_pages));
		    LoggerUtil.info(sql);
			past = conn.prepareStatement(sql);
			past.setString(1, cStoreNo);
		    rs=  past.executeQuery();
			JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
			if(array.length()>0){
				out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
			else{
				out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":" + array.toString().replace(" ", "") + "}");
			}
			System.err.println("数组的长度"+array.length());
		} catch (Exception e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"dDate\":\"" +0 + "\"}");
			e.printStackTrace();
		}
		finally {
			DB.closeRs_Con(rs, past, conn);
		}
	}

}
