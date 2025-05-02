package id.ac.ui.cs.advprog.hiringgo.dashboardhonor.repository;

import id.ac.ui.cs.advprog.hiringgo.dashboardhonor.model.Lowongan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class LowonganRepositoryImpl implements LowonganRepository {
    private final List<Lowongan> store = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(1);

    @Override
    public Lowongan save(Lowongan vac) {
        if (vac.getId() == null) {
            vac.setId("vac-" + seq.getAndIncrement());
        }
        store.add(vac);
        return vac;
    }

    @Override
    public List<Lowongan> findByMahasiswaId(String mahasiswaId) {
        return store.stream()
                .filter(v -> mahasiswaId.equals(v.getMahasiswaId()))
                .collect(Collectors.toList());
    }
}
