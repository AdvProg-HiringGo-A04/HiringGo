package id.ac.ui.cs.advprog.hiringgo.authentication.repository;

import id.ac.ui.cs.advprog.hiringgo.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
