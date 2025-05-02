package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;
import java.util.List;

public interface LowonganRepository {
    Lowongan save(Lowongan vac);
    List<Lowongan> findByMahasiswaId(String studentId);
}
