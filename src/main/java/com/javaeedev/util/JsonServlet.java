package com.javaeedev.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonServlet extends HttpServlet {

    private String encoding = "UTF-8";
    private String contentType = "application/json";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        String encoding = config.getInitParameter("encoding");
        if (encoding!=null) {
            this.encoding = encoding;
            context.log("Set json encoding to: " + encoding);
        }
        else {
            context.log("Set json default encoding to UTF-8");
        }
        this.contentType = contentType + ";charset=" + encoding;
        context.log("Set contentType to: " + contentType);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(contentType);
        resp.setCharacterEncoding(encoding);
        PrintWriter pw = resp.getWriter();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
