package Fen_dian_yan_shou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import DB.DBYan_Huo_update;
import DB.GetConnection;


@WebServlet(description = "提交门店图片", urlPatterns = { "/Upload_Men_Dian_image" })
public class Upload_Men_Dian_image extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		Connection conn = null;
		// [{"imagecontent":"" + image + "\",\"cStoreNo\":\"" +
		// SendMessage.storeno + "\",\"cGoodsNo\":\"" + cGoodsNo + ""}]
		System.out.println(name);
		try {
			conn = GetConnection.getStoreConn();
			JSONArray array = new JSONArray("name");
			JSONObject obj = array.getJSONObject(0);
			String imagecontent = obj.getString("imagecontent").replace(' ', '+').replace("\n", "").replace("\r", "");
			String cStoreNo = obj.getString("cStoreNo");
			String cGoodsNo = obj.getString("cGoodsNo");
			File fileDir = new File(this.getServletContext().getRealPath("/FileDir"));
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			byte[] b = Base64.getDecoder().decode(imagecontent);
			FileOutputStream fout = new FileOutputStream(fileDir);
			// 将字节写入文件
			fout.write(b);
			fout.close();

			FileInputStream input = new FileInputStream(fileDir);
//			ps.setString(1, "cute");
//			ps.setBinaryStream(2, input, (int) f.length());
//			ps.executeUpdate();
//			ps.close();
			input.close();

			boolean a = DBYan_Huo_update.insert_into_ru_ku_dan(GetConnection.getStoreConn(), array);
			if (a) {
				out.print("{\"resultStatus\":\"" + 1 + "\"" + "}");
			} else {
				out.print("{\"resultStatus\":\"" + 0 + "\"" + "}");
			}
			System.out.println("{\"resultStatus\":\"" + 1 + "\"" + "}");
		} catch (Exception e) {
			Logger.getLogger(Upload_Fen_dian_ru_ku.class).error(e.getLocalizedMessage());
			e.printStackTrace();
		}
		out.flush();
		out.close();
	}
}
