package mathchem.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mathchem.data.MaterialItem;
import mathchem.data.ReactionItem;
import mathchem.data.ReactionItemDAO;

public class ListReactionItemsServlet extends HttpServlet
{
   private RequestDispatcher jsp;

   public void init(ServletConfig config) throws ServletException {
      ServletContext context = config.getServletContext();
      jsp = context.getRequestDispatcher("/WEB-INF/jsp/list-reaction-items.jsp");
   }
   
   
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException
   {
      List<ReactionItem> reactionItems = new ReactionItemDAO().findAll();
      List<MaterialItem> materialItems = new ReactionItemDAO().findMaterials();
      
      req.setAttribute("reactionItems", reactionItems);
      req.setAttribute("materialItems", materialItems);
      jsp.forward(req, resp);
   }
}