package id.ac.ui.cs.advprog.hiringgo.security;

public interface CurrentUserProvider {
    String getCurrentUserId();
    String getCurrentUserEmail();
    String getCurrentUserRole();
}