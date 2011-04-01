package com.indextank;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flaptor.indextank.apiclient.IndexDoesNotExistException;
import com.flaptor.indextank.apiclient.IndexTankClient;
import com.flaptor.indextank.apiclient.IndexTankClient.Index;

public class TestServlet extends HttpServlet {

  private transient ServletContext context;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init();
    context = config.getServletContext();
  }

  public void service(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {

    IndexTankClient client = new IndexTankClient
      ("http://<public-part>", "<auth-info>");
    for (Index index : client.listIndexes()) {
      try {
        String code = index.getCode();
        res.getWriter().write(code);
      } catch (IndexDoesNotExistException e) {
        throw new ServletException(e);
      }
    }
    Map<String, String> map = new HashMap<String, String>();
    map.put("text", "hello world");
    try {
      client.listIndexes().get(0).addDocument("test1", map);
      res.getWriter().write("+");
    } catch (IndexDoesNotExistException e) {
      throw new ServletException(e);
    }
  }

}
