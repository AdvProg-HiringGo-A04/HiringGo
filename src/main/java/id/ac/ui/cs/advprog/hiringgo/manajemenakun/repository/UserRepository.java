package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;

import java.util.List;

public interface UserRepository {
    void save(Account account);
    Account findById(String id);
    List<Account> findAll();
    void delete(String id);
}