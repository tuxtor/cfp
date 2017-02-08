package com.nabenik.cfp.view;

import com.nabenik.cfp.controller.RandomStringService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/stringServlet")
public class StringServlet extends HttpServlet {
    
    @Inject
    RandomStringService randomStringService;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.println("Hello");
            out.println(randomStringService.generateRandomString());
        } catch (IOException ex) {
            Logger.getLogger(StringServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
    
}