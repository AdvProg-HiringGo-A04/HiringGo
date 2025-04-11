package id.ac.ui.cs.advprog.hiringgo.periksalog.service;

import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogDTO;
import id.ac.ui.cs.advprog.hiringgo.periksalog.dto.LogStatusUpdateDTO;
import java.util.List;

public interface LogService {
    List<LogDTO> getAllLogsByDosenId(Long dosenId);
    LogDTO updateLogStatus(Long dosenId, LogStatusUpdateDTO logStatusUpdateDTO);
    boolean isLogOwnedByDosen(Long logId, Long dosenId);
}