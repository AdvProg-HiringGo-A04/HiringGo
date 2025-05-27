package id.ac.ui.cs.advprog.hiringgo.dashboard.service;

import id.ac.ui.cs.advprog.hiringgo.dashboard.dto.LowonganDTO;
import id.ac.ui.cs.advprog.hiringgo.entity.Lowongan;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LowonganConverter {

    public static List<LowonganDTO> convertListToDTO(List<Lowongan> lowonganList, Logger log) {
        if (lowonganList == null || lowonganList.isEmpty()) {
            return Collections.emptyList();
        }

        return lowonganList.stream()
                .map(lowongan -> convertToDTO(lowongan, log))
                .filter(Objects::nonNull)
                .toList();
    }

    public static LowonganDTO convertToDTO(Lowongan lowongan, Logger log) {
        if (lowongan == null) {
            log.warn("Attempted to convert null Lowongan to DTO");
            return null;
        }

        try {
            String mataKuliahName = extractMataKuliahName(lowongan);

            return LowonganDTO.builder()
                    .id(lowongan.getId() != null ? lowongan.getId() : "")
                    .mataKuliahName(mataKuliahName)
                    .tahunAjaran(lowongan.getTahunAjaran() != null ? lowongan.getTahunAjaran() : "")
                    .semester(lowongan.getSemester() != null ? lowongan.getSemester() : "")
                    .build();
        } catch (Exception e) {
            log.error("Error converting Lowongan to DTO: {}", e.getMessage());
            return null;
        }
    }

    private static String extractMataKuliahName(Lowongan lowongan) {
        if (lowongan.getMataKuliah() != null) {
            return lowongan.getMataKuliah().getNamaMataKuliah();
        }
        return "";
    }
}