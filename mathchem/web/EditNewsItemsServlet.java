package mathchem.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


import mathchem.chart.IsotermTask;
import mathchem.chart.MoveTask;
import mathchem.chart.Task;
import mathchem.chart.ZernoTask;
import mathchem.chart.typePaint;
import mathchem.data.NewsItem;
import mathchem.data.NewsItemDAO;

public class EditNewsItemsServlet extends HttpServlet
{
	private RequestDispatcher jsp;

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		jsp = context.getRequestDispatcher("/WEB-INF/jsp/news/edit-news-item.jsp");
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		String id = req.getParameter("id") ;
		NewsItemDAO newsItemDAO = new NewsItemDAO();
		if (id != null) {			
			NewsItem newsItem = newsItemDAO.find(id);		
			req.setAttribute("newsItem", newsItem);
			req.setAttribute("isNew", false);
		} else {
			NewsItem newsItem = new NewsItem();
			Long id1 = newsItemDAO.getNewId();
			newsItem.setId(id1);
			newsItem.setTitle("");
			newsItem.setContent("");
			Calendar currentDate = Calendar.getInstance(); //Get the current date
			SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
			String dateNow = formatter.format(currentDate.getTime());
			newsItem.setNewsDate(dateNow);
			req.setAttribute("newsItem", newsItem);
			req.setAttribute("isNew", true);
		}
		
		HttpSession session = req.getSession();	
		Long userPermission = (long) 0;

		if (session.getAttribute("userPermission") != null) {
			userPermission =  (Long) session.getAttribute("userPermission");
		}

		jsp = req.getRequestDispatcher("/WEB-INF/jsp/news/edit-news-item.jsp");
		if (userPermission == 1 || userPermission==2)		
			jsp.forward(req, resp);
		else {
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/about.jsp");		
			jsp.forward(req, resp);
		}
			
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
			req.setCharacterEncoding("UTF-8");
			
			// Check if cancel button was pressed.
		    String cancelButton = req.getParameter("cancel-button");
		    if (cancelButton != null)
		    {
		    	resp.sendRedirect("list-news-items?group=1");
		        return;
		    }
		      
			String title = req.getParameter("title");
			String newsDate = req.getParameter("newsDate");
			String content = req.getParameter("content");
			Long id = Long.parseLong(req.getParameter("id"));
			Boolean isNew = Boolean.parseBoolean(req.getParameter("isNew"));
			
			NewsItem newsItem = new NewsItem();
			newsItem.setTitle(title);
			newsItem.setContent(content);
			newsItem.setNewsDate(newsDate);
			newsItem.setId(id);
			
			if (!isNew) {
				new NewsItemDAO().update(newsItem);
			}
			else {
				new NewsItemDAO().insert(newsItem);
			}
			
//*-------------*/
//			try { 
//				               //Список загружаемых файлов 
//				               List files = new ArrayList(); 
//				               //Список обычных параметров из HTML-формы 
//				               Map params = new HashMap(); 
//				               //Инициализируем структуры files и params 
//				               init(req, params, files); 
//				               //Сохраняем файл на сервере 
//				               save(files, params); 
//				               resp.setContentType("text/html; charset=windows-1251"); 
//				               final PrintWriter writer = resp.getWriter(); 
//				               writer.println("Файл успешно загружен<br>"); 
//				               writer.println("<a href='" + req.getContextPath() + "/uploadform.html'>Загрузить еще >></a>"); 
//				               writer.close(); 
//				           } 
//				           catch (FileUploadException fue) { 
//				               fue.printStackTrace(); 
//				               throw new ServletException(fue); 
//				           } 
//*-----------------/			
			
			jsp = req.getRequestDispatcher("/WEB-INF/jsp/news/save-news-item.jsp");
			jsp.forward(req, resp);
		
	}
	private void save(List files, Map params) throws IOException { 
		           try { 
		               for (Iterator i = files.iterator(); i.hasNext();) { 
		                   FileItem item = (FileItem) i.next(); 
		                   //Файл, в который нужно произвести запись 
		                   final File file = new File(params.get("where") + File.separator + item.getName()); 
		                   FileOutputStream fos = new FileOutputStream(file); 
		                   fos.write(item.get()); 
		                   fos.close(); 
		               } 
		           } 
		           catch (IOException e) { 
		               e.printStackTrace(); 
		               throw e; 
		           } 
		       } 
		    
		       /** 
		        * Инициализирует структуру Map params параметрами из формы и List files прикрепленными файлами 
		        * (в нашем случае один файл) 
		        * 
		        * @param request 
		        * @param params 
		        * @param files 
		        * @throws FileUploadException 
		        */ 
		       private void init(HttpServletRequest request, Map params, List files) throws FileUploadException { 
		           DiskFileItemFactory factory = new DiskFileItemFactory(); 
		           //Устанавливаем каталог для временных файлов 
		           factory.setRepository(new File("/files")); 
		           ServletFileUpload upload = new ServletFileUpload(factory); 
		           //Установим ограничение на размер загружаемого файла в битах 
		           upload.setSizeMax(10240); 
		           List items = upload.parseRequest(request); 
		           for (Iterator i = items.iterator(); i.hasNext();) { 
		               FileItem item = (FileItem) i.next(); 
		               //Проверяем, является ли параметр обычным полем из HTML-формы, 
		               //если да, то помещаем в Map пару name=value... 
		               if (item.isFormField()) { 
		                   params.put(item.getFieldName(), item.getString()); 
		               } 
		               //... если нет, то конструируем объект AttachmentDataSource и 
		               //помещаем его в список прикрепленных файлов 
		               else { 
		                   if (item.getSize() <= 0) continue; 
		                   files.add(item); 
		               } 
		           } 
		       } 
}