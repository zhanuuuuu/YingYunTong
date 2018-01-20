package operation;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
 * Servlet implementation class AA
 */
@WebServlet("/AA")
public class AA extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AA() {
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
		Connection conn=GetConnection.getStoreConn();
		String userNo=request.getParameter("userNo");
		String pass=request.getParameter("pass");
		ResultSet rs=null;
		PreparedStatement past=null;
		try {
			past=conn.prepareStatement("select a.*,c.cStoreNo,c.cStoreName from dbo.YingYunTongFunction a ,dbo.YingYunTongPermission  b,t_Pass c  where a.ID=b.ID and b.UserNo=c.[User] and c.[User]=?  and Pass=?  ");
		    past.setString(1, userNo);
		    past.setString(2, pass);
			rs=past.executeQuery();
		    JSONArray array=ResultSet_To_JSON.resultSetToJsonArray(rs);
		    if(array!=null&&array.length()>0){
		    	out.print("{\"resultStatus\":\"" + 1 + "\"," + "\"data\":"+ array.toString().replace(" ", "") + "}");
		    }
		    else{
		    	out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":"+ array.toString()+ "}");
		    }
		} catch (SQLException e) {
			out.print("{\"resultStatus\":\"" + 0 + "\"," + "\"data\":\""+ ""+ "\"}");
			e.printStackTrace();
		}
		finally{
			DB.closePreparedStatement(past);
			DB.closeConn(conn);
		}
	}

}
