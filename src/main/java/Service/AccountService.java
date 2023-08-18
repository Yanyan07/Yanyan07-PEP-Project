package Service;

import DAO.AccountDAO;
import DAO.AccountDAOImpl;
import Model.Account;

public class AccountService {
    AccountDAO accDAO;

    public AccountService(){
        this.accDAO = new AccountDAOImpl();
    }
    public AccountService(AccountDAO accDAO){
        this.accDAO = accDAO;
    }

    public Account register(Account account) {
        return accDAO.addAccount(account);
    }

    public Account login(Account account) {
        return accDAO.getAccountByUsernameAndPassword(account);
    }

}
