package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Account;
import java.util.*;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, Account> store = new HashMap<>();

    @Override
    public void save(Account account) {
        store.put(account.getId(), account);
    }

    @Override
    public Account findById(String id) {
        return store.get(id);

    }
    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }
}
