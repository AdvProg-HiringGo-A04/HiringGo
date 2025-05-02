package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "mahasiswa")
@Entity
public class Mahasiswa {

    @Id
    private String id;

    private String namaLengkap;

    private String NPM;
}