package operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
public class Version extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/version.properties");
		try {
			prop.load(in);
			in.close();
			String version = prop.getProperty("version");
			out.print(version);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("content-type", "text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Properties prop = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/version.properties");
		try {
			prop.load(in);
			in.close();
			String version = prop.getProperty("version");
			String force = prop.getProperty("force");
			String explain = prop.getProperty("explain");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("version", version);
			map.put("force", force);
			map.put("explain", explain);
			Gson gson = new Gson();
			String str = gson.toJson(map);
			out.print(str);
			out.flush();
			out.close();
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
