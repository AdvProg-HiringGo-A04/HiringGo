package id.ac.ui.cs.advprog.hiringgo.manajemenLog.service;

import java.util.List;

import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogRequest;
import id.ac.ui.cs.advprog.hiringgo.manajemenLog.dto.LogResponse;

public interface LogService {
    List<LogResponse> getAllLogs(String mataKuliahId, String mahasiswaId);
    LogResponse getLogById(String logId, String mahasiswaId);
    LogResponse createLog(LogRequest logRequest, String mahasiswaId);
    LogResponse updateLog(String logId, LogRequest logRequest, String mahasiswaId);
    void deleteLog(String logId, String mahasiswaId);
    boolean validateEnrollment(String mataKuliahId, String mahasiswaId);
}
