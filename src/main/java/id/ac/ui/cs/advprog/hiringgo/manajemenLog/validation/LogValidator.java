package id.ac.ui.cs.advprog.hiringgo.manajemenLog.validation;

import java.util.Map;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;

public interface LogValidator {
    Map<String, String> validate(LogRequest logRequest);
}
