package org.horizon.smis.servlet;

import jakarta.annotation.Resource;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.sql.DataSource;
import java.sql.Connection;

@WebServlet("/db-test")
public class DbTestServlet extends HttpServlet {

    @Resource(name = "InventoryDataSource")
    private DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (Connection c = ds.getConnection()) {
            resp.getWriter().println("DB Connected: " + !c.isClosed());
        } catch (Exception e) {
            e.printStackTrace();
            try { resp.getWriter().println(e.toString()); } catch (Exception ignored) {}
        }
    }
}
