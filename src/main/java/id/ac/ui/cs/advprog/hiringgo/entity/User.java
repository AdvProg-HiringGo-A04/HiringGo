package id.ac.ui.cs.advprog.hiringgo.entity;

import id.ac.ui.cs.advprog.hiringgo.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")
@Table(name = "users")
@Entity
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}