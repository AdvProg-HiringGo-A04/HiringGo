package id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.repository;

import id.ac.ui.cs.advprog.hiringgo.manajemenlowongan.entity.Lowongan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LowonganRepository extends JpaRepository<Lowongan, UUID> {
    
}
