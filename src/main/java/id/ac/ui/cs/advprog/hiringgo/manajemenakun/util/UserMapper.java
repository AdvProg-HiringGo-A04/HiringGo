package id.ac.ui.cs.advprog.hiringgo.manajemenakun.util;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.BaseUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.MahasiswaUserResponse;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.repository.DosenRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper {

    private final DosenRepository dosenRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public UserMapper(
            DosenRepository dosenRepository,
            MahasiswaRepository mahasiswaRepository) {
        this.dosenRepository = dosenRepository;
        this.mahasiswaRepository = mahasiswaRepository;
    }

    public Object mapUserToResponse(User user) {
        if (user == null) {
            return null;
        }

        BaseUserResponse baseResponse;

        switch (user.getRole()) {
            case ADMIN:
                baseResponse = new AdminUserResponse();
                break;

            case DOSEN:
                DosenUserResponse dosenResponse = new DosenUserResponse();
                Optional<Dosen> dosenOpt = dosenRepository.findById(user.getId());
                if (dosenOpt.isPresent()) {
                    Dosen dosen = dosenOpt.get();
                    dosenResponse.setNamaLengkap(dosen.getNamaLengkap());
                    dosenResponse.setNip(dosen.getNIP());
                }
                baseResponse = dosenResponse;
                break;

            case MAHASISWA:
                MahasiswaUserResponse mahasiswaResponse = new MahasiswaUserResponse();
                Optional<Mahasiswa> mahasiswaOpt = mahasiswaRepository.findById(user.getId());
                if (mahasiswaOpt.isPresent()) {
                    Mahasiswa mahasiswa = mahasiswaOpt.get();
                    mahasiswaResponse.setNamaLengkap(mahasiswa.getNamaLengkap());
                    mahasiswaResponse.setNim(mahasiswa.getNPM());
                }
                baseResponse = mahasiswaResponse;
                break;

            default:
                baseResponse = new BaseUserResponse();
        }

        baseResponse.setEmail(user.getEmail());
        baseResponse.setRole(user.getRole());

        return baseResponse;
    }
}