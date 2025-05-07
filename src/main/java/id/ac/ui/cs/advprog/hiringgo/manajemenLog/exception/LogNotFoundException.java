package id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception;

import java.util.Map;

public class LogNotFoundException extends RuntimeException {
    private final Map<String, String> errors;

    public LogNotFoundException(Map<String, String> errors) {
        super("Log tidak ditemukan");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
