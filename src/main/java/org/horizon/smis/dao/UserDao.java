package org.horizon.smis.dao;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.*;

@ApplicationScoped
public class UserDao {

    @Resource(name = "SMISDataSource")
    private DataSource ds;

    public UserRecord findByUsername(String username) throws SQLException {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username = ?";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                return new UserRecord(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role")
                );
            }
        }
    }

    public record UserRecord(long id, String username, String passwordHash, String role) {}
}

