package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Admin implements Account {
    private final String id = UUID.randomUUID().toString();
    private final String email;
    private Role role = Role.ADMIN;

    public Admin(AccountData data) {
        this.email = data.getEmail();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override public String getFullName() {
        return "";
    }

    @Override public Role getRole() {
        return role;
    }
}