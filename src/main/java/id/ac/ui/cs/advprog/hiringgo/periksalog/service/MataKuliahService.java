package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MataKuliahService {
    private final MataKuliahRepository mataKuliahRepository;

    public MataKuliah getMataKuliahByKode(String kode) {
        return mataKuliahRepository.findByKodeMataKuliah(kode)
                .orElseThrow(() -> new NoSuchElementException("Mata kuliah tidak ditemukan"));
    }
}
