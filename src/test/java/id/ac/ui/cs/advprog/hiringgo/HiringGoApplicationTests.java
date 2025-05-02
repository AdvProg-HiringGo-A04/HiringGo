package id.ac.ui.cs.advprog.hiringgo;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class HiringGoApplicationTests {
    @MockitoBean
    private UserManagementService userManagementService;

    @Test
    void contextLoads() {
        HiringGoApplication.main(new String[]{});
    }

}
