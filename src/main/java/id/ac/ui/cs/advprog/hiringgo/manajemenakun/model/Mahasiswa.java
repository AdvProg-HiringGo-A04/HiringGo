package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Mahasiswa implements Users {
    private String id = UUID.randomUUID().toString();
    private String nim;
    private String fullName;
    private String email;
    private String password;
    private Role role = Role.MAHASISWA;

    public Mahasiswa(AccountData data) {
        this.nim       = data.getIdentifier();
        this.fullName  = data.getFullName();
        this.email     = data.getEmail();
        this.password  = data.getPassword();
    }

    @Override public String getId()        { return id; }
    @Override public String getEmail()     { return email; }
    @Override public String getFullName()  { return fullName; }
    @Override public Role getRole()        { return role; }
    @Override public String getPassword()  { return password; }

    public String getIdentifier()          { return nim; }
    public void setRole(Role role)         { this.role = role; }
    public void setId(String id)         { this.id = id; }
    public void setIdentifier(String nim)   { this.nim = nim; }
    public void setFullName(String name)   { this.fullName = name; }
    public void setPassword(String pwd)    { this.password = pwd; }
    public void setEmail(String email)    { this.email = email; }

}