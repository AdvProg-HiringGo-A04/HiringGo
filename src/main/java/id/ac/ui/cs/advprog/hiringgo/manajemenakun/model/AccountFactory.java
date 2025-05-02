package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class AccountFactory {
    public static Users createAccount(Role role, AccountData data) {
        if (role == null) {
            throw new IllegalArgumentException("Role tidak boleh null");
        }

        switch (role) {
            case DOSEN:
                if (data.getIdentifier() == null || data.getFullName() == null)
                    throw new IllegalArgumentException("Dosen data incomplete");
                return new Dosen(data);
            case ADMIN:
                return new Admin(data);
            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }
}
