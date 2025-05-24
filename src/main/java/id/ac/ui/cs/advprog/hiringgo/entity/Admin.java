package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "admin")
@Entity
public class Admin {
    @Id
    private String id;
}