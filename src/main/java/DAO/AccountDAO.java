package DAO;

import Model.Account;

public interface AccountDAO {
    Account addAccount(Account Account);
    Account getAccountByUsername(String username);
    Account getAccountByUsernameAndPassword(Account account);
    Account getAccountById(int id);
}
