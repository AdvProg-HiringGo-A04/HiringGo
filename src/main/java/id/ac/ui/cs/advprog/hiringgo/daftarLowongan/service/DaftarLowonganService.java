package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.EntityNotFoundException;
import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception.InvalidDataException;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.entity.PendaftarLowongan;
import id.ac.ui.cs.advprog.hiringgo.repository.LowonganRepository;
import id.ac.ui.cs.advprog.hiringgo.repository.MahasiswaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DaftarLowonganService {
    private final LowonganRepository lowonganRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Transactional
    public void registerToLowongan(int sks, double ipk, String lowonganId, String mahasiswaId) {
        validateData(sks, ipk);

        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("lowonganId", "Lowongan tidak ditemukan")));

        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("mahasiswaId", "Mahasiswa tidak ditemukan")));

        Optional<PendaftarLowongan> existingPendaftar = lowonganRepository.findPendaftarByLowonganAndMahasiswa(lowonganId, mahasiswaId);
        if (existingPendaftar.isPresent()) {
            throw new EntityNotFoundException(Map.of("mahasiswaId", "Mahasiswa sudah mendaftar ke lowongan ini"));
        }

        PendaftarLowongan pendaftar = PendaftarLowongan.builder()
                .id(UUID.randomUUID().toString())
                .lowongan(lowongan)
                .mahasiswa(mahasiswa)
                .jumlahSks(sks)
                .ipk(ipk)
                .diterima(false)
                .build();

        lowongan.getPendaftar().add(pendaftar);
        lowonganRepository.save(lowongan);

        log.info("Registration processed successfully for mahasiswa: {} to lowongan: {}", mahasiswaId, lowonganId);
    }

    private void validateData(int sks, double ipk) {
        Map<String, String> errors = new HashMap<>();

        if (ipk < 0 || ipk > 4) {
            errors.put("ipk", "IPK harus antara 0 dan 4");
        }

        if (sks < 0) {
            errors.put("sks", "SKS tidak boleh negatif");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

}
