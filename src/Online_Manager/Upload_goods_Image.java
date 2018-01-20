package Online_Manager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import Tool.RenamePolicyCos;

/**
 * Servlet implementation class Upload_goods_Image
 */
@WebServlet(description = "�ϴ���ƷͼƬ", urlPatterns = { "/Upload_goods_Image" })
public class Upload_goods_Image extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	response.setHeader("content-type", "text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
        File fileDir = new File(this.getServletContext().getRealPath("/FileDir"));  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
        // �����ϴ��ļ��Ĵ�С�����������С ���׳�IOException�쳣��Ĭ�ϴ�С��10M��  
        int inmaxPostSize = 100 * 1024 * 1024;  
        MultipartRequest multirequest = null;  
        
        // �ϴ��ļ�����������  
        RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();  
        try {  
            // MultipartRequest()��8�ֹ��캯����  
            multirequest = new MultipartRequest(request, fileDir.getAbsolutePath(), inmaxPostSize, "UTF-8", myRenamePolicyCos); // GBK���ı���ģʽ�ϴ��ļ�  
            String cBarcode= multirequest.getParameter("cBarcode");// ��ȡ��ͨ��Ϣ  
            System.out.println(cBarcode);  
            Enumeration<String> filedFileNames = multirequest.getFileNames();  
            String filedName = null;  
            if (null != filedFileNames) {  
                while (filedFileNames.hasMoreElements()) {  
                    filedName = filedFileNames.nextElement();// �ļ��ı��������  
                    // ��ȡ���ļ������ϴ����ļ�������Ӧ���ϴ����������е��ļ�  
                    File uploadFile = multirequest.getFile(filedName);  
                    if (null != uploadFile && uploadFile.length() > 0) {  
                        System.out.println(uploadFile.getName());  
                        System.out.println(uploadFile.getPath());  
                        
                        System.out.println(uploadFile.length());  
                    }  
                    // ��ȡδ���������ļ�����  
                    String Originalname = multirequest  
                            .getOriginalFileName(filedName);  
                    System.out.println(Originalname);  
                    
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
    	out.print("���get����,�ϴ���post");
    } 

}