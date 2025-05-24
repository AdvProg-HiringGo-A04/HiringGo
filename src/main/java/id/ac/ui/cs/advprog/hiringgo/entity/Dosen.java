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
@Table(name = "dosen")
@Entity
@ToString
public class Dosen {
    @Id
    private String id;

    @Column(name = "nama_lengkap")
    private String namaLengkap;

    @Column(name = "nip")
    private String NIP;
}
