CREATE TABLE IF NOT EXISTS users(
    id CHAR(36) NOT NULL PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    password CHAR(60) NOT NULL,
    role VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS admin(
    id CHAR(36) NOT NULL PRIMARY KEY,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS mahasiswa(
    id CHAR(36) NOT NULL PRIMARY KEY,
    nim CHAR(10) NOT NULL UNIQUE,
    nama_lengkap VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS dosen(
    id CHAR(36) NOT NULL PRIMARY KEY,
    nip VARCHAR(18) NOT NULL UNIQUE,
    nama_lengkap VARCHAR(255) NOT NULL,
    FOREIGN KEY (id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS mata_kuliah(
    nama_mata_kuliah VARCHAR(100) NOT NULL,
    kode_mata_kuliah VARCHAR(10) NOT NULL UNIQUE PRIMARY KEY,
    deskripsi_mata_kuliah TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS mengampu_mata_kuliah(
    kode_mata_kuliah VARCHAR(10) NOT NULL,
    id CHAR(36) NOT NULL,
    PRIMARY KEY (kode_mata_kuliah, id),
    FOREIGN KEY (kode_mata_kuliah) REFERENCES mata_kuliah(kode_mata_kuliah) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (id) REFERENCES dosen(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lowongan (
    id CHAR(36) NOT NULL PRIMARY KEY,
    kode_mata_kuliah VARCHAR(10) NOT NULL,
    tahun_ajaran VARCHAR(50) NOT NULL,
    semester VARCHAR(50) NOT NULL,
    jumlah_dibutuhkan INT NOT NULL,
    FOREIGN KEY (kode_mata_kuliah) REFERENCES mata_kuliah(kode_mata_kuliah)
);

CREATE TABLE IF NOT EXISTS log (
    id CHAR(36) NOT NULL PRIMARY KEY,
    judul VARCHAR(255) NOT NULL,
    keterangan TEXT,
    kategori VARCHAR(24) NOT NULL,
    waktu_mulai TIME NOT NULL,
    waktu_selesai TIME NOT NULL,
    tanggal_log DATE NOT NULL,
    pesan VARCHAR(255),
    status VARCHAR(10) NOT NULL,
    mata_kuliah_id VARCHAR(10) NOT NULL,
    lowongan_id CHAR(36) NOT NULL,
    mahasiswa_id CHAR(36) NOT NULL,
    created_at DATE NOT NULL,
    updated_at DATE,
    FOREIGN KEY (mata_kuliah_id) REFERENCES mata_kuliah(kode_mata_kuliah) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lowongan_id) REFERENCES lowongan(id)
);

CREATE TABLE IF NOT EXISTS mahasiswa_pendaftar_lowongan (
    id CHAR(36) NOT NULL PRIMARY KEY,
    lowongan_id CHAR(36) NOT NULL,
    mahasiswa_id CHAR(36) NOT NULL,
    jumlah_sks INT NOT NULL,
    ipk DOUBLE PRECISION NOT NULL,
    diterima BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (lowongan_id) REFERENCES lowongan(id),
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id)
);

CREATE TABLE IF NOT EXISTS mahasiswa_diterima_lowongan (
    mahasiswa_id CHAR(36) NOT NULL,
    lowongan_id CHAR(36) NOT NULL,
    PRIMARY KEY (mahasiswa_id, lowongan_id),
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (lowongan_id) REFERENCES lowongan(id) ON UPDATE CASCADE ON DELETE CASCADE
);
