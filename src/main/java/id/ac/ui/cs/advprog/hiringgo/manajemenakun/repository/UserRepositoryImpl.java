package id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.Users;
import java.util.*;
import org.springframework.stereotype.Repository;


@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, Users> store = new HashMap<>();

    @Override
    public void save(Users users) {
        store.put(users.getId(), users);
    }

    @Override
    public Users findById(String id) {
        return store.get(id);

    }
    @Override
    public List<Users> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void delete(String id) {
        store.remove(id);
    }
}
