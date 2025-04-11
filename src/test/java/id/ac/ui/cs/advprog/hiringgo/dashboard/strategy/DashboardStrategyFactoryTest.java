// src/test/java/id/ac/ui/cs/advprog/hiringgo/dashboard/strategy/DashboardStrategyFactoryTest.java
package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.common.model.User;
import id.ac.ui.cs.advprog.hiringgo.common.model.UserRole;
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
        User adminUser = User.builder().role(UserRole.ADMIN).build();

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(adminUser);

        // Assert
        assertTrue(strategy instanceof AdminDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsDosen_ShouldReturnDosenStrategy() {
        // Arrange
        User dosenUser = User.builder().role(UserRole.DOSEN).build();

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(dosenUser);

        // Assert
        assertTrue(strategy instanceof DosenDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsMahasiswa_ShouldReturnMahasiswaStrategy() {
        // Arrange
        User mahasiswaUser = User.builder().role(UserRole.MAHASISWA).build();

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(mahasiswaUser);

        // Assert
        assertTrue(strategy instanceof MahasiswaDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserRoleIsInvalid_ShouldThrowException() {
        // Arrange
        User invalidUser = new User();
        // No role set

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.getStrategy(invalidUser));
    }
}