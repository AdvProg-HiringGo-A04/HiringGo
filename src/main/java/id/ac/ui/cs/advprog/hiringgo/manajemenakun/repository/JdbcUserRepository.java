package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.exception.DataAccessException;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class JdbcUserRepository implements UserRepository {
    private static final Logger LOGGER = Logger.getLogger(JdbcUserRepository.class.getName());
    private final DataSource dataSource;

    public JdbcUserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all users", e);
            throw new DataAccessException("Failed to retrieve users", e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        String id = rs.getString("id");
        String email = rs.getString("email");
        String password = rs.getString("password");

        switch (role) {
            case "ADMIN":
                return new Admin(id, email, password);

            case "DOSEN":
                String nip = rs.getString("nip");
                String dosenName = rs.getString("nama_lengkap");
                return new Dosen(id, nip, dosenName, email, password);

            case "MAHASISWA":
                String nim = rs.getString("nim");
                String mahasiswaName = rs.getString("nama_lengkap");
                return new Mahasiswa(id, nim, mahasiswaName, email, password);

            default:
                return new User(id, email, password, role);
        }
    }
}