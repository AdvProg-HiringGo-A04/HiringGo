package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import java.util.List;

public interface UserRepository {
    void save(Users users);
    Users findById(String id);
    List<Users> findAll();
    void delete(String id);
}