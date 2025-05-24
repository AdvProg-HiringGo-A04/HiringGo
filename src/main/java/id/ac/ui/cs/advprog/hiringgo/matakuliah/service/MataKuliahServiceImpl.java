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

import static id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper.MataKuliahMapper.createMataKuliahRequestToMataKuliah;

@Service
public class MataKuliahServiceImpl implements MataKuliahService {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Override
    public MataKuliah createMataKuliah(CreateMataKuliahRequest request) {
        String kode = request.getKodeMataKuliah();

        if (mataKuliahRepository.existsById(kode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mata kuliah dengan kode " + kode + " sudah ada");
        }

        MataKuliah mataKuliah = createMataKuliahRequestToMataKuliah(request);

        return mataKuliahRepository.save(mataKuliah);
    }

    @Override
    public List<MataKuliah> findAll() {
        return mataKuliahRepository.findAll();
    }

    @Override
    public MataKuliah findByKode(String kodeMataKuliah) {
        return mataKuliahRepository.findById(kodeMataKuliah)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
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
        mataKuliahRepository.findById(kodeMataKuliah)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));

        mataKuliahRepository.deleteById(kodeMataKuliah);
    }
}
