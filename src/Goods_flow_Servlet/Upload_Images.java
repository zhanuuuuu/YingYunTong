package Goods_flow_Servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloopen.rest.sdk.utils.LoggerUtil;
import com.oreilly.servlet.MultipartRequest;

import Tool.RenamePolicyCos;

public class Upload_Images extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	response.setHeader("content-type", "text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
        File fileDir = new File(this.getServletContext().getRealPath("/FileDir"));  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
        // 设置上传文件的大小，超过这个大小 将抛出IOException异常，默认大小是10M。  
        int inmaxPostSize = 100 * 1024 * 1024;  
        MultipartRequest multirequest = null;  
        
        // 上传文件重命名策略  
        RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();  
        try {  
            // MultipartRequest()有8种构造函数！  
            multirequest = new MultipartRequest(request, fileDir.getAbsolutePath(), inmaxPostSize, "UTF-8", myRenamePolicyCos); // GBK中文编码模式上传文件  
            String cStoreNo= multirequest.getParameter("cStoreNo");// 获取普通信息  
            LoggerUtil.info(cStoreNo);  
            Enumeration<String> filedFileNames = multirequest.getFileNames();  
            String filedName = null;  
            if (null != filedFileNames) {  
                while (filedFileNames.hasMoreElements()) {  
                    filedName = filedFileNames.nextElement();// 文件文本框的名称  
                    // 获取该文件框中上传的文件，即对应到上传到服务器中的文件  
                    File uploadFile = multirequest.getFile(filedName);  
                    if (null != uploadFile && uploadFile.length() > 0) {  
                        LoggerUtil.info(uploadFile.getName());  
                        LoggerUtil.info(uploadFile.getPath());  
                        LoggerUtil.info(uploadFile.length());  
                    }  
                    // 获取未重命名的文件名称  
                    String Originalname = multirequest.getOriginalFileName(filedName);  
                    LoggerUtil.info(Originalname);  
                    
//                    Connection conn=GetConnection.getStoreConn();
//                    String sql="Update t_Store set image_path=? where  cStoreNo=?";
//                    PreparedStatement past=conn.prepareStatement(sql);
//                    past.setString(1, uploadFile.getPath());
//                    past.setString(2, cStoreNo);
//                    past.executeUpdate();
//                    DB.closePreparedStatement(past);
//                    DB.closeConn(conn);
                    
                }  
            }  
    		out.print(" {\"resultStatus\":1" + "}");
        } catch (Exception e) {  
        	out.print(" {\"resultStatus\":-1" + "}");
            e.printStackTrace();  
        }  
    }  
  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	response.setHeader("content-type", "text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	out.print("这个get请求,上传用post");
    } 

}
