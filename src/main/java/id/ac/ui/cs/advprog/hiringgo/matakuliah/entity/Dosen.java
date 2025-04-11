package id.ac.ui.cs.advprog.hiringgo.matakuliah.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dosen")
public class Dosen {

    @Id
    private String nip;

}
