package com.Exception.hander;

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
 * Servlet implementation class ExcetionHannder
 */
@WebServlet(description = "����һ���쳣������", urlPatterns = { "/ExcetionHannder" })
public class ExcetionHannder extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		response.setHeader("content-type", "text/html;charset=UTF-8");
    	PrintWriter out = response.getWriter();
    	//���ڽ��ձ��ύ������
    	
    	String imageurl=null;
    	String adcGoodsNo=request.getParameter("cGoodsNo");
    	System.out.println(adcGoodsNo);
    	//���Խ��ļ�  �༶Ŀ¼
    	long time=System.currentTimeMillis();
    	//��ȡ�����ļ���·��
        File fileDir = new File(this.getServletContext().getRealPath("\\ExcetionHannder"));  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
        System.out.println(fileDir.getPath());
        
     
		
//		//��Ӧɾ�����ݿ��е�ͼƬ,�ͷ����ݿ���Դ
//		String imagePath=null;
//		//·������
//		String address=fileDir.getPath()+"\\"+imagePath;
//		
//		 File	file = new File(address);
//			// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
//			if (file.isFile() && file.exists()) {
//				file.delete();
//			}
        // �����ϴ��ļ��Ĵ�С�����������С ���׳�IOException�쳣��Ĭ�ϴ�С��10M��  
        int inmaxPostSize = 100 * 1024 * 1024;  
        MultipartRequest multirequest = null;  
        // �ϴ��ļ�����������  
        RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();  
        try {  
            // MultipartRequest()��8�ֹ��캯����  
            multirequest = new MultipartRequest(request, fileDir  
                    .getAbsolutePath(), inmaxPostSize, "UTF-8", myRenamePolicyCos); // GBK���ı���ģʽ�ϴ��ļ�  
            String subject = multirequest.getParameter("subject");// ��ȡ��ͨ��Ϣ  (������ȡ����)
            System.out.println(subject);  
            Enumeration<String> filedFileNames = multirequest.getFileNames();  
            String filedName = null;  
            
            if (null != filedFileNames) {  
                while (filedFileNames.hasMoreElements()) {  
                    filedName = filedFileNames.nextElement();// �ļ��ı��������  
                    // ��ȡ���ļ������ϴ����ļ�������Ӧ���ϴ����������е��ļ�  
                    File uploadFile = multirequest.getFile(filedName);  
                    if (null != uploadFile && uploadFile.length() > 0) {  
                    	imageurl=uploadFile.getName();
                        System.out.println(uploadFile.getName());  
                        System.out.println(uploadFile.getPath());  
                        System.out.println(uploadFile.length());  
                    }  
                    // ��ȡδ���������ļ�����  
                    String Originalname = multirequest  
                            .getOriginalFileName(filedName);  
                    System.out.println(Originalname);  
                }  
            }  
            System.out.println(imageurl);
           
            try{
            	
            	System.out.println("�ϴ��ɹ�");
            	//out.print("�ϴ��ɹ�");
            }catch(Exception e){
            	e.printStackTrace();
            	//out.print("�ϴ�ʧ��");
            	System.out.println("�ϴ�ʧ��");
            }
           
        } catch (Exception e) {  
        	out.print("�ϴ�ʧ��----��Ǹ");
            e.printStackTrace();  
        }  
	}

}

