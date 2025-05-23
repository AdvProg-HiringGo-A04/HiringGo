package id.ac.ui.cs.advprog.hiringgo.matakuliah.service;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.UpdateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.repository.MataKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MataKuliahServiceImpl implements MataKuliahService {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Override
    public MataKuliah createMataKuliah(CreateMataKuliahRequest request) {
        if (mataKuliahRepository.existsById(request.getKodeMataKuliah())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate");
        }

        MataKuliah mataKuliah = MataKuliah.builder()
                .namaMataKuliah(request.getNamaMataKuliah())
                .kodeMataKuliah(request.getKodeMataKuliah())
                .deskripsiMataKuliah(request.getDeskripsiMataKuliah())
                .dosenPengampu(request.getDosenPengampu())
                .build();

        return mataKuliahRepository.save(mataKuliah);
    }

    @Override
    public List<MataKuliah> findAll() {
        return mataKuliahRepository.findAll();
    }

    @Override
    public MataKuliah findByKode(String kodeMataKuliah) {
        Optional<MataKuliah> mataKuliah = mataKuliahRepository.findById(kodeMataKuliah);

        if (mataKuliah.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        return mataKuliah.get();
    }

    @Override
    public MataKuliah updateMataKuliah(String kodeMataKuliah, UpdateMataKuliahRequest request) {
        MataKuliah mataKuliah = mataKuliahRepository.findById(kodeMataKuliah)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));

        Optional.ofNullable(request.getNamaMataKuliah())
                .ifPresent(mataKuliah::setNamaMataKuliah);

        Optional.ofNullable(request.getDeskripsiMataKuliah())
                .ifPresent(mataKuliah::setDeskripsiMataKuliah);

        Optional.ofNullable(request.getDosenPengampu())
                .ifPresent(mataKuliah::setDosenPengampu);

        return mataKuliahRepository.save(mataKuliah);
    }

    @Override
    public void deleteMataKuliah(String kodeMataKuliah) {
        if (!mataKuliahRepository.existsById(kodeMataKuliah)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        mataKuliahRepository.deleteById(kodeMataKuliah);
    }
}
