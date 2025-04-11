package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.User;
import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
}