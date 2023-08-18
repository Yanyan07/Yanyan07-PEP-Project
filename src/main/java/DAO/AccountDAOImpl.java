package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAOImpl implements AccountDAO {

    @Override
    public Account addAccount(Account account) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "insert into Account(username,password) values(?,?)";
        Account accDuplicate = getAccountByUsername(account.getUsername());
        try {     
            if(account.getUsername()!=null && account.getUsername()!="" && account.getPassword().length()>=4 && accDuplicate==null) {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, account.getUsername());
                ps.setString(2, account.getPassword());
                
                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();
                if(pkeyResultSet.next()) {
                    int account_id = (int)pkeyResultSet.getLong(1);
                    return new Account(account_id, account.getUsername(), account.getPassword());
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        };
        return null;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from Account where username=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int account_id = rs.getInt("account_id");
                String uname = rs.getString("username");
                String password = rs.getString("password");
                return new Account(account_id, uname, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account getAccountByUsernameAndPassword(Account account) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from Account where username=? and password=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                return new Account(account_id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account getAccountById(int id) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from Account where id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int account_id = rs.getInt("account_id");
                String uname = rs.getString("username");
                String password = rs.getString("password");
                return new Account(account_id, uname, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    
}
