package id.ac.ui.cs.advprog.hiringgo.daftarLowongan.exception;

import java.util.Map;

public class InvalidDataException extends RuntimeException {
    private final Map<String, String> errors;

    public InvalidDataException(Map<String, String> errors) {
        super("Data tidak valid");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
