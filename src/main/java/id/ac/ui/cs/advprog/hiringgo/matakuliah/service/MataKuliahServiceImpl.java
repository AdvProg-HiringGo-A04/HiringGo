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

        MataKuliah mataKuliah = new MataKuliah();
        mataKuliah.setNamaMataKuliah(request.getNamaMataKuliah());
        mataKuliah.setKodeMataKuliah(request.getKodeMataKuliah());
        mataKuliah.setDeskripsiMataKuliah(request.getDeskripsiMataKuliah());
        mataKuliah.setDosenPengampu(request.getDosenPengampu());

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
        Optional<MataKuliah> optionalMataKuliah = mataKuliahRepository.findById(kodeMataKuliah);

        if (optionalMataKuliah.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

        MataKuliah mataKuliah = optionalMataKuliah.get();

        if (request.getNamaMataKuliah() != null) {
            mataKuliah.setNamaMataKuliah(request.getNamaMataKuliah());
        }
        if (request.getDeskripsiMataKuliah() != null) {
            mataKuliah.setDeskripsiMataKuliah(request.getDeskripsiMataKuliah());
        }
        if (request.getDosenPengampu() != null) {
            mataKuliah.setDosenPengampu(request.getDosenPengampu());
        }

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
