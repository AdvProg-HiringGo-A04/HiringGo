package id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.validators;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation.LogValidator;

public class TimeValidator implements LogValidator {
    
    @Override
    public Map<String, String> validate(LogRequest logRequest, boolean isUpdate) {
        Map<String, String> errors = new HashMap<>();
        
        LocalTime waktuMulai = logRequest.getWaktuMulai();
        LocalTime waktuSelesai = logRequest.getWaktuSelesai();
        LocalDate tanggalLog = logRequest.getTanggalLog();
        
        if (!isUpdate) {
            // Validasi waktu tidak boleh null
            if (waktuMulai == null) {
                errors.put("waktuMulai", "Waktu mulai tidak boleh kosong");
                return errors;
            }
            
            if (waktuSelesai == null) {
                errors.put("waktuSelesai", "Waktu selesai tidak boleh kosong");
                return errors;
            }
            
            if (tanggalLog == null) {
                errors.put("tanggalLog", "Tanggal log tidak boleh kosong");
                return errors;
            }
        }
        
        // Validasi tanggal log tidak boleh di masa depan
        if (tanggalLog.isAfter(LocalDate.now())) {
            errors.put("tanggalLog", "Tanggal log tidak boleh di masa depan");
        }
        
        // Validasi waktu mulai harus sebelum waktu selesai
        if (waktuMulai.isAfter(waktuSelesai) || waktuMulai.equals(waktuSelesai)) {
            errors.put("rangeWaktu", "Waktu mulai harus sebelum waktu selesai");
        }
        
        return errors;
    }
}
