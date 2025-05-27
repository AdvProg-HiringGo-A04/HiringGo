package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.service;

import id.ac.ui.cs.advprog.hiringgo.daftarLowongan.command.DaftarLowonganCommand;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class DaftarLowonganService {
    private final LowonganRepository lowonganRepository;
    private final MahasiswaRepository mahasiswaRepository;

    @Transactional
    public void execute(DaftarLowonganCommand command) {
        validateCommand(command);
        processRegistration(command);
    }

    @Async("databaseTaskExecutor")
    @Transactional
    public CompletableFuture<Void> executeAsync(DaftarLowonganCommand command) {
        log.info("Starting async execution for lowongan registration: {}", command.getLowonganId());

        try {
            validateCommand(command);
            processRegistration(command);

            log.info("Successfully completed async registration for lowongan: {}", command.getLowonganId());
            return CompletableFuture.completedFuture(null);

        } catch (Exception e) {
            log.error("Error during async registration for lowongan: {}", command.getLowonganId(), e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private void validateCommand(DaftarLowonganCommand command) {
        Map<String, String> errors = new HashMap<>();

        if (command.getIpk() < 0 || command.getIpk() > 4) {
            errors.put("ipk", "IPK harus antara 0 dan 4");
        }

        if (command.getSks() < 0) {
            errors.put("sks", "SKS tidak boleh negatif");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private void processRegistration(DaftarLowonganCommand command) {
        // Ambil lowongan
        String lowonganId = command.getLowonganId();
        Lowongan lowongan = lowonganRepository.findById(lowonganId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("lowonganId", "Lowongan tidak ditemukan")));

        // Ambil mahasiswa
        String mahasiswaId = command.getMahasiswaId();
        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                .orElseThrow(() -> new EntityNotFoundException(Map.of("mahasiswaId", "Mahasiswa tidak ditemukan")));

        // Cek mahasiswa sudah daftar
        Optional<PendaftarLowongan> existingPendaftar = lowonganRepository.findPendaftarByLowonganAndMahasiswa(lowonganId, mahasiswaId);

        if (existingPendaftar.isPresent()) {
            throw new EntityNotFoundException(Map.of("mahasiswaId", "Mahasiswa sudah mendaftar ke lowongan ini"));
        }

        // Buat pendaftar baru
        PendaftarLowongan pendaftar = PendaftarLowongan.builder()
                .id(UUID.randomUUID().toString())
                .lowongan(lowongan)
                .mahasiswa(mahasiswa)
                .jumlahSks(command.getSks())
                .ipk(command.getIpk())
                .diterima(null) // default null
                .build();

        lowongan.getPendaftar().add(pendaftar);
        lowonganRepository.save(lowongan);

        log.info("Registration processed successfully for mahasiswa: {} to lowongan: {}", mahasiswaId, lowonganId);
    }

    // Batch processing method - useful untuk bulk operations
    @Async("databaseTaskExecutor")
    public CompletableFuture<Map<String, String>> executeAsyncBatch(java.util.List<DaftarLowonganCommand> commands) {
        log.info("Starting batch async execution for {} registration commands", commands.size());

        Map<String, String> results = new HashMap<>();

        for (DaftarLowonganCommand command : commands) {
            try {
                validateCommand(command);
                processRegistration(command);
                results.put(command.getLowonganId(), "SUCCESS");

            } catch (Exception e) {
                log.error("Error processing command in batch for lowongan: {}", command.getLowonganId(), e);
                results.put(command.getLowonganId(), "FAILED: " + e.getMessage());
            }
        }

        log.info("Completed batch async execution. Success: {}, Failed: {}",
                results.values().stream().mapToInt(v -> v.equals("SUCCESS") ? 1 : 0).sum(),
                results.values().stream().mapToInt(v -> v.startsWith("FAILED") ? 1 : 0).sum());

        return CompletableFuture.completedFuture(results);
    }
}
