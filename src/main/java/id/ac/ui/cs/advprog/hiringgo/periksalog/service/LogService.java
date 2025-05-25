package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LogService {

    CompletableFuture<List<LogDTO>> getAllLogsByDosenIdAsync(String dosenId);
    CompletableFuture<LogDTO> updateLogStatusAsync(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO);
    CompletableFuture<Boolean> isLogOwnedByDosenAsync(String logId, String dosenId);

    List<LogDTO> getAllLogsByDosenId(String dosenId);
    LogDTO updateLogStatus(String dosenId, LogStatusUpdateDTO logStatusUpdateDTO);
    boolean isLogOwnedByDosen(String logId, String dosenId);
}