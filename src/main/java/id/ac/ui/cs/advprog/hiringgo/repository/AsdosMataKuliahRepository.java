package id.ac.ui.cs.advprog.hiringgo.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.MahasiswaDiterimaLowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.MahasiswaDiterimaLowonganId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsdosMataKuliahRepository extends JpaRepository<MahasiswaDiterimaLowongan, MahasiswaDiterimaLowonganId> {
    boolean existsByMahasiswaIdAndLowonganId(String mahasiswaId, String mataKuliahId);
}

