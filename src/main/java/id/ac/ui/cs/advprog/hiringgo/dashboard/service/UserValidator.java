package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getRole() == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }
}