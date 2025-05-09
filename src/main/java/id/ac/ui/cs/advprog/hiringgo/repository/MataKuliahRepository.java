package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, String> {

    Optional<MataKuliah> findByKodeMataKuliah(String kode);
}
