package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    @Test
    public void interfaceShouldHaveGetAllUsersMethod() {
        Class<?> interfaceClass = UserRepository.class;

        boolean hasMethod = false;
        try {
            hasMethod = interfaceClass.getMethod("getAllUsers") != null;
        } catch (NoSuchMethodException e) {
        }

        assertTrue(hasMethod, "UserRepository harus memiliki metode getAllUsers");
    }
}