package id.ac.ui.cs.advprog.hiringgo.common.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MataKuliah {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "matakuliah_dosen",
            joinColumns = @JoinColumn(name = "matakuliah_id"),
            inverseJoinColumns = @JoinColumn(name = "dosen_id")
    )
    private Set<User> dosenPengampu = new HashSet<>();
}