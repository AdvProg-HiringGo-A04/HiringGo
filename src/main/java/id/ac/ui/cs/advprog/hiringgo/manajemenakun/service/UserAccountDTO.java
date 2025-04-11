package id.ac.ui.cs.advprog.hiringgo.manajemenakun.service;

public class UserAccountDTO {
    private final String id;
    private final String namaLengkap;
    private final String email;
    private final String roleDisplay;

    public UserAccountDTO(String id, String namaLengkap, String email, String roleDisplay) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.roleDisplay = roleDisplay;
    }

    public String getId() { return id; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getEmail() { return email; }
    public String getRoleDisplay() { return roleDisplay; }
}