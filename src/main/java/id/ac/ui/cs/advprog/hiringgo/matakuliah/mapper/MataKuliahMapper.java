package id.ac.ui.cs.advprog.hiringgo.matakuliah.mapper;

import id.ac.ui.cs.advprog.hiringgo.entity.MataKuliah;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.CreateMataKuliahRequest;
import id.ac.ui.cs.advprog.hiringgo.matakuliah.model.MataKuliahResponse;

public class MataKuliahMapper {

    private MataKuliahMapper() {
    }

    public static MataKuliahResponse mataKuliahToMataKuliahResponse(MataKuliah mataKuliah) {
        return MataKuliahResponse.builder()
                .namaMataKuliah(mataKuliah.getNamaMataKuliah())
                .kodeMataKuliah(mataKuliah.getKodeMataKuliah())
                .deskripsiMataKuliah(mataKuliah.getDeskripsiMataKuliah())
                .dosenPengampu(mataKuliah.getDosenPengampu())
                .build();
    }

    public static MataKuliah createMataKuliahRequestToMataKuliah(CreateMataKuliahRequest request) {
        return MataKuliah.builder()
                .namaMataKuliah(request.getNamaMataKuliah())
                .kodeMataKuliah(request.getKodeMataKuliah())
                .deskripsiMataKuliah(request.getDeskripsiMataKuliah())
                .dosenPengampu(request.getDosenPengampu())
                .build();
    }
}
