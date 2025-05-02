package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Mahasiswa implements Account {
    private final String id = UUID.randomUUID().toString();
    private final String nip;
    private final String fullName;
    private final String email;
    private Role role = Role.MAHASISWA;

    public Mahasiswa(AccountData data) {
        if (data.getIdentifier() == null || data.getFullName() == null || data.getEmail() == null || data.getPassword() == null)
            throw new IllegalArgumentException("Mahasiswa data incomplete");
        this.nip = data.getIdentifier();
        this.fullName = data.getFullName();
        this.email = data.getEmail();
    }

    public String getNim() {
        return nip;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}