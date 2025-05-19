package id.ac.ui.cs.advprog.hiringgo.dashboard.strategy;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Role;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
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
        Users adminUser = createMockUser("1", Role.ADMIN);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(adminUser);

        // Assert
        assertTrue(strategy instanceof AdminDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsDosen_ShouldReturnDosenStrategy() {
        // Arrange
        Users dosenUser = createMockUser("2", Role.DOSEN);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(dosenUser);

        // Assert
        assertTrue(strategy instanceof DosenDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserIsMahasiswa_ShouldReturnMahasiswaStrategy() {
        // Arrange
        Users mahasiswaUser = createMockUser("3", Role.MAHASISWA);

        // Act
        DashboardStatisticsStrategy<?> strategy = factory.getStrategy(mahasiswaUser);

        // Assert
        assertTrue(strategy instanceof MahasiswaDashboardStrategy);
    }

    @Test
    void getStrategy_WhenUserRoleIsInvalid_ShouldThrowException() {
        // Arrange
        Users invalidUser = createMockUserWithNullRole("4");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> factory.getStrategy(invalidUser));
    }

    // Helper method to create mock Users objects
    private Users createMockUser(String id, Role role) {
        return new Users() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getEmail() {
                return "user" + id + "@example.com";
            }

            @Override
            public String getFullName() {
                return "User " + id;
            }

            @Override
            public Role getRole() {
                return role;
            }

            @Override
            public String getPassword() {
                return "password";
            }
        };
    }

    // Helper method to create mock Users with null role
    private Users createMockUserWithNullRole(String id) {
        return new Users() {
            @Override
            public String getId() {
                return id;
            }

            @Override
            public String getEmail() {
                return "user" + id + "@example.com";
            }

            @Override
            public String getFullName() {
                return "User " + id;
            }

            @Override
            public Role getRole() {
                return null;
            }

            @Override
            public String getPassword() {
                return "password";
            }
        };
    }
}