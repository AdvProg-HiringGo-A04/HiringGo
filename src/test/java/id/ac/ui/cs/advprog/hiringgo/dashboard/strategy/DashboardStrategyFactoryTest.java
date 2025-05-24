package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.entity.Role;
import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.dashboard.repository.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class DashboardStrategyFactoryTest {

    @Mock
    private DashboardRepository dashboardRepository;

    private DashboardStrategyFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        factory = new DashboardStrategyFactory(dashboardRepository);
    }

    @Test
    void getStrategy_WhenUserIsAdmin_ShouldReturnAdminStrategy() {
        // Arrange
        User adminUser = createMockUser("1", Role.ADMIN);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(adminUser);

        // Assert
        assertTrue(strategy instanceof AdminDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsDosen_ShouldReturnDosenStrategy() {
        // Arrange
        User dosenUser = createMockUser("2", Role.DOSEN);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(dosenUser);

        // Assert
        assertTrue(strategy instanceof DosenDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsMahasiswa_ShouldReturnMahasiswaStrategy() {
        // Arrange
        User mahasiswaUser = createMockUser("3", Role.MAHASISWA);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(mahasiswaUser);

        // Assert
        assertTrue(strategy instanceof MahasiswaDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserRoleIsInvalid_ShouldThrowException() {
        // Arrange
        User invalidUser = createMockUserWithNullRole("4");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.getStrategy(invalidUser));
    }

    // Helper method to create mock User objects
    private User createMockUser(String id, Role role) {
        User user = new User();
        user.setId(id);
        user.setEmail("user" + id + "@example.com");
        user.setRole(role);
        user.setPassword("password");
        return user;
    }

    // Helper method to create mock User with null role
    private User createMockUserWithNullRole(String id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user" + id + "@example.com");
        user.setRole(null);
        user.setPassword("password");
        return user;
    }
}