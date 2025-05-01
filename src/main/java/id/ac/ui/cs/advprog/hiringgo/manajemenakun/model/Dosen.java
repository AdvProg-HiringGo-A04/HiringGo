package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Dosen implements Account {
    private final String id = UUID.randomUUID().toString();
    private final String nip;
    private final String fullName;
    private final String email;
    private Role role = Role.DOSEN;

    public Dosen(AccountData data) {
        this.nip = data.getNip();
        this.fullName = data.getFullName();
        this.email = data.getEmail();
    }

    public String getNip() {
        return nip;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override public String getEmail() {
        return email;
    }

    @Override public String getFullName() {
        return fullName;
    }

    @Override public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}