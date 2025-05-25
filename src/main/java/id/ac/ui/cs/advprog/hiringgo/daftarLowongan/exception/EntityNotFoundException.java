package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception;

import java.util.Map;

public class EntityNotFoundException extends RuntimeException {
    private final Map<String, String> errors;

    public EntityNotFoundException(Map<String, String> errors) {
        super("Data tidak ditemukan");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

