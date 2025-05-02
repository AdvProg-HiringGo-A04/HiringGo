package id.ac.ui.cs.advprog.hiringgo.authentication.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String username);
}
