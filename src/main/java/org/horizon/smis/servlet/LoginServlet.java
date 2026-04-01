package org.horizon.smis.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.horizon.smis.security.PasswordUtil;

import org.horizon.smis.dao.UserDao;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Inject
    private UserDao userDao;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Please enter username and password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try {
            UserDao.UserRecord user = userDao.findByUsername(username);

            if (user == null || !org.horizon.smis.security.PasswordUtil.verify(password, user.passwordHash())) {
                req.setAttribute("error", "Invalid username or password");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.id());
            session.setAttribute("username", user.username());
            session.setAttribute("role", user.role());

            resp.sendRedirect(req.getContextPath() + "/dashboard.xhtml");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}
