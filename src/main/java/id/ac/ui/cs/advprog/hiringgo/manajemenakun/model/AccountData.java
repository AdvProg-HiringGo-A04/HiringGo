package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class AccountData {
    private final String identifier;
    private final String fullName;
    private final String email;
    private final String password;

    public AccountData(String identifier, String fullName, String email, String password) {
        this.identifier = identifier;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
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