package id.ac.ui.cs.advprog.hiringgo.manajemenakun.model;

public class Admin extends User {

    public Admin() {
        super();
        setRole("ADMIN");
    }

    public Admin(String id, String email, String password) {
        super(id, email, password, "ADMIN");
    }
}