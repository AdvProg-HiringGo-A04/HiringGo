package id.ac.ui.cs.advprog.hiringgo.manajemenLog.repository;

import id.ac.ui.cs.advprog.hiringgo.entity.AsdosMataKuliah;
import id.ac.ui.cs.advprog.hiringgo.entity.AsdosMataKuliahId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsdosMataKuliahRepository extends JpaRepository<AsdosMataKuliah, AsdosMataKuliahId> {
    boolean existsByMahasiswaIdAndMataKuliahId(String mahasiswaId, String mataKuliahId);
}

