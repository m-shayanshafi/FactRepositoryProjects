package fruitwar.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fruitwar.supp.RobotProps;
import fruitwar.util.Logger;
import fruitwar.web.server.Server;
import fruitwar.web.util.HtmlPrintWriter;


import java.io.*;


public class Upload extends MyServletBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	String getMyURL() {
		return BaseServletURL + "upload";
	}

	public StringBuffer handleRequest(HttpServletRequest request, HttpServletResponse response, String subURI)
			throws ServletException, IOException {
				
		String uploadType = request.getParameter("UploadType");
		
		//
		//TODO:
		//We need to prevent flood attack here...
		//This action costs much CPU time.
		//
		StringBuffer buf;
		
		if(uploadType != null && uploadType.equalsIgnoreCase("src"))
			buf = handleUploadSrc(request, response);
		else 
			buf = handleUploadJar(request);
		//else
		//	response.getWriter().println("Unknown upload type: " + uploadType);

		//add a back link
		if(buf != null)
			buf.append("\n<hr><a href='" + HtmlForger.URL_BACK + "'>Back to previous page</a><br>\n");
			buf.append("<a href='" + Main.URL_MY_ROBOTS + "'>Go to My Robots page</a>\n");
		return buf;
	}

	
	private StringBuffer handleUploadSrc(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException{

		//response.setContentType("text/html; charset=ASCII");
		
		String src = request.getParameter("src");
		
		//make properties object
		RobotProps prop = new RobotProps();
		//author
		//client will be redirected to login page if not logged in.
		String author = Access.requestLoginName(request, response);
		if(author == null)
			return null;	//nothing.
		prop.set(RobotProps.AUTHOR, author);
		//description
		String desc = request.getParameter("desc");
		if(desc != null){
			if(desc.length() > 300)
				desc = desc.substring(0, 300);
			//HTML tags are forbidden
			desc.replaceAll("<", "&lt;");
			desc.replaceAll(">", "&gt;");
			prop.set(RobotProps.DESCRIPTION, desc);
		}
		
		Logger.log("Upload robot by: [" + author + "]");

		StringWriter out = new StringWriter();
		PrintWriter pw = new HtmlPrintWriter(out);
		Server.uploadRobotSource(src, prop, pw);
		pw.close();
		return out.getBuffer();
	}
	
	private StringBuffer handleUploadJar(HttpServletRequest request)
		throws ServletException, IOException{
		
		return new StringBuffer("_TODO_");
		/*
		PrintWriter out = response.getWriter();
		//System.out.println(req.getContentLength());
		//System.out.println(req.getContentType());
		DefaultFileItemFactory factory = new DefaultFileItemFactory();
		// maximum size that will be stored in memory
		// 允许设置内存中存储数据的门限，单位：字节
		factory.setSizeThreshold(4096);
		// the location for saving data that is larger than getSizeThreshold()
		// 如果文件大小大于SizeThreshold，则保存到临时目录
		factory.setRepository(new File(
				"D:\\temp"));

		FileUpload upload = new FileUpload(factory);
		// maximum size before a FileUploadException will be thrown
		// 最大上传文件，单位：字节
		upload.setSizeMax(1000000);
		try {
			List fileItems = upload.parseRequest(request);

			Iterator iter = fileItems.iterator();

			while(iter.hasNext()){
				FileItem i = (FileItem)iter.next();
				//skip non-file items.
				if(i.isFormField())
					continue;
				
				out.println("Uploaded file: " + i.getName() + ", " + i.getSize() + "-----------TODO");
			}*/
			/*
			// 正则匹配，过滤路径取文件名
			String regExp = ".+\\\\(.+)$";

			// 过滤掉的文件类型
			String[] errorType = { ".exe", ".com", ".cgi", ".asp" };
			Pattern p = Pattern.compile(regExp);
			String itemNo = "";// 文件存放路径
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				// 忽略其他不是文件域的所有表单信息
				if (!item.isFormField()) {
					String name = item.getName();
					long size = item.getSize();
					if ((name == null || name.equals("")) && size == 0)
						continue;
					Matcher m = p.matcher(name);
					boolean result = m.find();
					if (result) {
						for (int temp = 0; temp < errorType.length; temp++) {
							if (m.group(1).endsWith(errorType[temp])) {
								throw new IOException(name + ": wrong type");
							}
						}
						try {

							// 保存上传的文件到指定的目录

							// 在下文中上传文件至数据库时，将对这里改写
							item.write(new File("d:\\" + m.group(1)));

							out.print(name + "&nbsp;&nbsp;" + size + "<br>");

						} catch (Exception e) {
							out.println(e);
						}

					} else {
						throw new IOException("fail to upload");
					}
				}
				
			}
			*/
//		} catch (IOException e) {
//			out.println(e);
//		} catch (FileUploadException e1) {
//			out.println(e1);
//		}
		
	}

	
}