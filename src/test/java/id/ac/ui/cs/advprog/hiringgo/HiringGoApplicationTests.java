package id.ac.ui.cs.advprog.hiringgo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.service.UserService;

@SpringBootTest
class HiringGoApplicationTests {
    @MockitoBean
    private UserService userService;

    @Test
    void contextLoads() {
        HiringGoApplication.main(new String[]{});
    }

}
