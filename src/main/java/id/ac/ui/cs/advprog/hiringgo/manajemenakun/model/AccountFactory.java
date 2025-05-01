package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class AccountFactory {
    public static Account createAccount(Role role, AccountData data) {
        switch (role) {
            case DOSEN:
                if (data.getNip() == null || data.getFullName() == null)
                    throw new IllegalArgumentException("Dosen data incomplete");
                return new Dosen(data);
            case ADMIN:
                return new Admin(data);
            default:
                throw new IllegalArgumentException("Unsupported role: " + role);
        }
    }
}
