package org.horizon.smis.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/dashboard.xhtml", "/students.xhtml", "/courses.xhtml", "/enrollments.xhtml", "/grades.xhtml"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("username") != null);

        if (loggedIn) {
            chain.doFilter(request, response); // allow access
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
