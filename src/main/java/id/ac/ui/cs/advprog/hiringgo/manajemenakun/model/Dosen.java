package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

import java.util.UUID;

public class Dosen implements Users {
    private String id = UUID.randomUUID().toString();
    private String nip;
    private String fullName;
    private String email;
    private Role role = Role.DOSEN;
    private String password;

    public Dosen(AccountData data) {
        this.nip       = data.getIdentifier();
        this.fullName  = data.getFullName();
        this.email     = data.getEmail();
        this.password  = data.getPassword();
    }

    @Override public String getId()        { return id; }
    @Override public String getEmail()     { return email; }
    @Override public String getFullName()  { return fullName; }
    @Override public Role getRole()        { return role; }
    @Override public String getPassword()  { return password; }

    public String getIdentifier()          { return nip; }
    public void setRole(Role role)         { this.role = role; }
    public void setIdentifier(String id)   { this.nip = id; }
    public void setFullName(String name)   { this.fullName = name; }
    public void setId(String id)           { this.id = id; }
    public void setEmail(String email)        { this.email = email; }
    public void setPassword(String password)        { this.password = password; }

}