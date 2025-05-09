package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "users")
@Entity
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private String role;
}
