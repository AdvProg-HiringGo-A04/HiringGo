package id.ac.ui.cs.advprog.hiringgo.matakuliah.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;

import java.util.List;

public interface MataKuliahService {

    MataKuliah createMataKuliah(CreateMataKuliahRequest request);

    List<MataKuliah> findAll();

    MataKuliah findByKode(String kodeMataKuliah);

    MataKuliah updateMataKuliah(String kodeMataKuliah, UpdateMataKuliahRequest request);

    void deleteMataKuliah(String kodeMataKuliah);
}
