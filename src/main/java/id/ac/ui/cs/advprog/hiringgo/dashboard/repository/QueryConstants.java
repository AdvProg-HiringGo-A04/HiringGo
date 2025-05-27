package id.ac.ui.cs.advprog.hiringgo.dashboard.repository;

public class QueryConstants {
    public final String COUNT_USERS_BY_ROLE =
            "SELECT COUNT(u) FROM User u WHERE u.role = :role";
    public final String COUNT_MATA_KULIAH =
            "SELECT COUNT(mk) FROM MataKuliah mk";
    public final String COUNT_LOWONGAN =
            "SELECT COUNT(l) FROM Lowongan l";
    public final String COUNT_MATA_KULIAH_BY_DOSEN =
            "SELECT COUNT(mk) FROM MataKuliah mk JOIN mk.dosenPengampu d WHERE d.id = :dosenId";
    public final String COUNT_MAHASISWA_BY_DOSEN =
            "SELECT COUNT(DISTINCT pl.mahasiswa) FROM PendaftarLowongan pl " +
                    "WHERE pl.lowongan.id IN (SELECT l.id FROM Lowongan l " +
                    "WHERE l.mataKuliah.kodeMataKuliah IN (SELECT mk.kodeMataKuliah FROM MataKuliah mk " +
                    "JOIN mk.dosenPengampu d WHERE d.id = :dosenId)) AND pl.diterima = true";
    public final String COUNT_OPEN_LOWONGAN_BY_DOSEN =
            "SELECT COUNT(l) FROM Lowongan l " +
                    "WHERE l.mataKuliah.kodeMataKuliah IN (SELECT mk.kodeMataKuliah FROM MataKuliah mk " +
                    "JOIN mk.dosenPengampu d WHERE d.id = :dosenId) " +
                    "AND l.jumlahDibutuhkan > " +
                    "(SELECT COUNT(pl) FROM PendaftarLowongan pl WHERE pl.lowongan.id = l.id AND pl.diterima = true)";
    public final String COUNT_OPEN_LOWONGAN =
            "SELECT COUNT(l) FROM Lowongan l " +
                    "WHERE l.jumlahDibutuhkan > " +
                    "(SELECT COUNT(pl) FROM PendaftarLowongan pl WHERE pl.lowongan.id = l.id AND pl.diterima = true)";
    public final String COUNT_ACCEPTED_LOWONGAN =
            "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                    "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
    public final String COUNT_REJECTED_LOWONGAN =
            "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                    "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = false AND pl.diterima IS NOT NULL";
    public final String COUNT_PENDING_LOWONGAN =
            "SELECT COUNT(pl) FROM PendaftarLowongan pl " +
                    "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima IS NULL";
    public final String FIND_LOG_HOURS =
            "SELECT log.waktuMulai, log.waktuSelesai FROM Log log " +
                    "WHERE log.mahasiswa.id = :mahasiswaId AND log.status = :status";
    public final String FIND_ACCEPTED_LOWONGAN =
            "SELECT l FROM Lowongan l " +
                    "JOIN PendaftarLowongan pl ON pl.lowongan.id = l.id " +
                    "WHERE pl.mahasiswa.id = :mahasiswaId AND pl.diterima = true";
}