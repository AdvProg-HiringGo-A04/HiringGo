package id.ac.ui.cs.advprog.hiringgo.manajemenLog.exception;

import java.util.Map;

public class InvalidLogException extends RuntimeException {
    private final Map<String, String> errors;
    
    public InvalidLogException(Map<String, String> errors) {
        super("Log tidak valid");
        this.errors = errors;
    }
    
    public Map<String, String> getErrors() {
        return errors;
    }
}
