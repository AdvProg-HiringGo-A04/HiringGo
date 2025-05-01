package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class AccountData {
    private final String nip;
    private final String fullName;
    private final String email;
    private final String password;

    public AccountData(String nip, String fullName, String email, String password) {
        this.nip = nip;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getNip() {
        return nip;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}