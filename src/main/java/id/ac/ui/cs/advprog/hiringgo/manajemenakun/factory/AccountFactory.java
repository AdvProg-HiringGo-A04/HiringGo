package id.ac.ui.cs.advprog.hiringgo.manajemenakun.factory;

import id.ac.ui.cs.advprog.hiringgo.entity.User;

public interface AccountFactory<T> {
    User createAccount(T dto);
}