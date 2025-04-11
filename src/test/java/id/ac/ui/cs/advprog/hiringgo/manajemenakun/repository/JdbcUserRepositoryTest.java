package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JdbcUserRepositoryTest {

    @Mock private DataSource dataSource;
    @Mock private Connection connection;
    @Mock private PreparedStatement statement;
    @Mock private ResultSet resultSet;

    private JdbcUserRepository repository;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);

        repository = new JdbcUserRepository(dataSource);
    }

    @Test
    public void getAllUsersShouldReturnAllUserTypes() throws Exception {
        when(resultSet.next()).thenReturn(true, true, true, false);

        when(resultSet.getString("id")).thenReturn("1", "2", "3");
        when(resultSet.getString("role")).thenReturn("ADMIN", "DOSEN", "MAHASISWA");
        when(resultSet.getString("email")).thenReturn("admin@example.com", "dosen@example.com", "mahasiswa@example.com");
        when(resultSet.getString("password")).thenReturn("pass1", "pass2", "pass3");

        when(resultSet.getString("nip")).thenReturn(null, "12345", null);
        when(resultSet.getString("nama_lengkap")).thenReturn("Nama Dosen", "Nama Mahasiswa");

        when(resultSet.getString("nim")).thenReturn(null, null, "M12345");

        List<User> users = repository.getAllUsers();

        assertEquals(3, users.size());

        assertTrue(users.get(0) instanceof Admin);
        assertEquals("-", users.get(0).getNamaLengkap());
        assertEquals("admin@example.com", users.get(0).getEmail());
        assertEquals("ADMIN", users.get(0).getRole());

        assertTrue(users.get(1) instanceof Dosen);
        assertEquals("Nama Dosen", users.get(1).getNamaLengkap());
        assertEquals("dosen@example.com", users.get(1).getEmail());
        assertEquals("DOSEN", users.get(1).getRole());

        assertTrue(users.get(2) instanceof Mahasiswa);
        assertEquals("Nama Mahasiswa", users.get(2).getNamaLengkap());
        assertEquals("mahasiswa@example.com", users.get(2).getEmail());
        assertEquals("MAHASISWA", users.get(2).getRole());

        verify(connection).prepareStatement("SELECT * FROM users");
        verify(statement).executeQuery();
    }
}