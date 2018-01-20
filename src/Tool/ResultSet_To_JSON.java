package Tool;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class ResultSet_To_JSON {

	static Logger logger = Logger.getLogger(ResultSet_To_JSON.class);

	public static String resultSetToJson(ResultSet rs) {
		JSONArray array = new JSONArray();
		try {
			// ��ȡ����
			ResultSetMetaData metaData;
			metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			
			// ����ResultSet�е�ÿ������
			while (rs.next()) {
				JSONObject jsonObj = new JSONObject();
				// ����ÿһ��
				for (int i = 1; i <= columnCount; i++) { // ����
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					if(String_Tool.isEmpty(value)){
						value="";
					}
					try {
						jsonObj.put(columnName, value);
					} catch (JSONException e) {
						logger.error("�ѽ�����ŵ�json���쳣");
						e.printStackTrace();
					}
				}
				array.put(jsonObj);
			}
		} catch (SQLException e) {
			logger.error("�ѽ�����쳣"+e.getLocalizedMessage());
			e.printStackTrace();
		}
		return array.toString().replace(" ", "");
	}

	public static JSONArray resultSetToJsonArray(ResultSet rs) {
		JSONArray array = new JSONArray();
		try {
			// ��ȡ����
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			// ����ResultSet�е�ÿ������
			while (rs.next()) {
				JSONObject jsonObj = new JSONObject();
				// ����ÿһ��
				for (int i = 1; i <= columnCount; i++) { // ����
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					if(String_Tool.isEmpty(value)){
						value="";
					}
					jsonObj.put(columnName, value);
				}
				array.put(jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return array;
	}
	public static JSONObject resultSetToJsonObject(ResultSet rs) {
		JSONObject jsonObj=new JSONObject();
		try {
			// ��ȡ����
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			// ����ResultSet�е�ÿ������
			if(rs.next()) {
				jsonObj = new JSONObject();
				// ����ÿһ��
				for (int i = 1; i <= columnCount; i++) { // ����
					String columnName = metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					if(String_Tool.isEmpty(value)){
						value="";
					}
					jsonObj.put(columnName, value.replace(" ", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage());
		}
		return jsonObj;
	}
	public String Map_To_JSON(List<Map<String,String>> list){
		Gson gson=new Gson();
		String str=gson.toJson(list);
		return str;
	}
	public static String resultSetTostr(ResultSet rs) {
		try {
			String str = "";
			ResultSetMetaData metaData;
			metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			// String columnName = metaData.getColumnLabel(i);
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
			
				int ColumnType = metaData.getColumnType(i);
				str = str + columnName +"\n";

			}
			return str;

		} catch (SQLException e) {
			logger.error("" + e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}

	}

}
