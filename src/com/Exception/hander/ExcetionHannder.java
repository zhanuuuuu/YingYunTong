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
@WebServlet(description = "这是一个异常处理类", urlPatterns = { "/ExcetionHannder" })
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
    	//用于接收表单提交的数据
    	
    	String imageurl=null;
    	String adcGoodsNo=request.getParameter("cGoodsNo");
    	System.out.println(adcGoodsNo);
    	//可以建文件  多级目录
    	long time=System.currentTimeMillis();
    	//获取本地文件家路径
        File fileDir = new File(this.getServletContext().getRealPath("\\ExcetionHannder"));  
        if (!fileDir.exists()) {  
            fileDir.mkdirs();  
        }  
        System.out.println(fileDir.getPath());
        
     
		
//		//对应删除数据库中的图片,释放数据库资源
//		String imagePath=null;
//		//路径问题
//		String address=fileDir.getPath()+"\\"+imagePath;
//		
//		 File	file = new File(address);
//			// 路径为文件且不为空则进行删除
//			if (file.isFile() && file.exists()) {
//				file.delete();
//			}
        // 设置上传文件的大小，超过这个大小 将抛出IOException异常，默认大小是10M。  
        int inmaxPostSize = 100 * 1024 * 1024;  
        MultipartRequest multirequest = null;  
        // 上传文件重命名策略  
        RenamePolicyCos myRenamePolicyCos = new RenamePolicyCos();  
        try {  
            // MultipartRequest()有8种构造函数！  
            multirequest = new MultipartRequest(request, fileDir  
                    .getAbsolutePath(), inmaxPostSize, "UTF-8", myRenamePolicyCos); // GBK中文编码模式上传文件  
            String subject = multirequest.getParameter("subject");// 获取普通信息  (根本获取不到)
            System.out.println(subject);  
            Enumeration<String> filedFileNames = multirequest.getFileNames();  
            String filedName = null;  
            
            if (null != filedFileNames) {  
                while (filedFileNames.hasMoreElements()) {  
                    filedName = filedFileNames.nextElement();// 文件文本框的名称  
                    // 获取该文件框中上传的文件，即对应到上传到服务器中的文件  
                    File uploadFile = multirequest.getFile(filedName);  
                    if (null != uploadFile && uploadFile.length() > 0) {  
                    	imageurl=uploadFile.getName();
                        System.out.println(uploadFile.getName());  
                        System.out.println(uploadFile.getPath());  
                        System.out.println(uploadFile.length());  
                    }  
                    // 获取未重命名的文件名称  
                    String Originalname = multirequest  
                            .getOriginalFileName(filedName);  
                    System.out.println(Originalname);  
                }  
            }  
            System.out.println(imageurl);
           
            try{
            	
            	System.out.println("上传成功");
            	//out.print("上传成功");
            }catch(Exception e){
            	e.printStackTrace();
            	//out.print("上传失败");
            	System.out.println("上传失败");
            }
           
        } catch (Exception e) {  
        	out.print("上传失败----抱歉");
            e.printStackTrace();  
        }  
	}

}

