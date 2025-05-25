package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {

    Optional<Mahasiswa> findByNPM(String npm);
    Optional<Mahasiswa> findById(String id);
}
