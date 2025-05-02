package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Admin implements Users {
    private String id = UUID.randomUUID().toString();
    private String email;
    private Role role = Role.ADMIN;
    private String password;

    public Admin(AccountData data) {
        this.email = data.getEmail();
        this.password = data.getPassword();
    }

    @Override public String getId()        { return id; }
    @Override public String getEmail()     { return email; }
    @Override public String getFullName()  { return ""; }
    @Override public Role getRole()        { return role; }
    @Override public String getPassword()  { return password; }

    public void setRole(Role role)         { this.role = role; }
    public void setId(String id)           { this.id = id; }
    public void setPassword(String password)          { this.password = password; }

}