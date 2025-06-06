package id.ac.ui.cs.advprog.hiringgo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Table(name = "mahasiswa")
@Entity
@ToString
public class Mahasiswa {

    @Id
    private String id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "nim")
    private String NPM;
}
