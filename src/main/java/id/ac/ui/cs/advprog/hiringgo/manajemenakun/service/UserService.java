package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

import id.ac.ui.cs.advprog.hiringgo.entity.User;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.AdminDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.DosenDTO;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Admin;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.entity.Dosen;
import id.ac.ui.cs.advprog.hiringgo.entity.Mahasiswa;
import id.ac.ui.cs.advprog.hiringgo.manajemenakun.model.RoleUpdateDTO;

import java.util.List;

public interface UserService {
    User createAdmin(AdminDTO adminDTO);
    User createDosen(DosenDTO dosenDTO);

    Object getUserResponseById(String id);
    List<Object> getAllUsersResponse();

    List<User> getAllUsers();
    List<Admin> getAllAdmins();
    List<Dosen> getAllDosens();
    List<Mahasiswa> getAllMahasiswas();

    User getUserById(String id);
    Admin getAdminById(String id);
    Dosen getDosenById(String id);
    Mahasiswa getMahasiswaById(String id);

    User updateUserRole(String id, RoleUpdateDTO roleUpdateDTO);

    void deleteAdmin(String id);
    void deleteDosen(String id);
    void deleteMahasiswa(String id);
}