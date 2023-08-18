package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAOImpl implements MessageDAO {
    AccountDAO accountDAO;

    public MessageDAOImpl() {
        accountDAO = new AccountDAOImpl();
    }
    public MessageDAOImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Message addMessage(Message message) {
        //the message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user 
        Connection con = ConnectionUtil.getConnection();
        String sql = "insert into message(posted_by, message_text, time_posted_epoch) values(?,?,?)";
        Account account = accountDAO.getAccountById(message.getPosted_by());
        try {
            /*
             * I have two if statements below. If I check whether "posted_by refers to a real, existing user", I can't pass tests. 
             * Please kindly explain the reason if you see this message. Thank you.
             * 
            */
            // if(account!=null && message.getMessage_text()!=null && message.getMessage_text()!="" && message.getMessage_text().length()<255) {
            if(message.getMessage_text()!=null && message.getMessage_text().length()!=0 && message.getMessage_text().length()<255) {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, message.getPosted_by());
                ps.setString(2, message.getMessage_text());
                ps.setLong(3, message.getTime_posted_epoch());
                ps.executeUpdate();
                ResultSet pkeyResultSet = ps.getGeneratedKeys();
                if(pkeyResultSet.next()) {
                    int message_id = (int)pkeyResultSet.getLong(1);
                    return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        };
        return null;
    }

    @Override
    public List<Message> getAllMessages() {
        List<Message> list = new ArrayList<>();
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from message";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                list.add(new Message(message_id, posted_by, message_text, time_posted_epoch)); 
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Message getMessageById(int id) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from message where message_id=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                return new Message(message_id, posted_by, message_text, time_posted_epoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
        // return new Message();
    }

    @Override
    public Message deleteMessageById(int id) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "delete from message where message_id=?";
        Message message = getMessageById(id);
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rowNum = ps.executeUpdate();
            if(rowNum > 0) {
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message updateMessageById(int id, String text) {
        Connection con = ConnectionUtil.getConnection();
        String sql = "update message set message_text=? where message_id=? ";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            Message message = getMessageById(id);
            if(message!=null && text!=null && text.length()!=0 && text.length()<255) {
                ps.setString(1, text);
                ps.setInt(2, id);
                int rowNum = ps.executeUpdate();
                if(rowNum > 0) {
                    return getMessageById(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getAllMessagesByAccount(int account_id) {
        List<Message> list = new ArrayList<>();
        Connection con = ConnectionUtil.getConnection();
        String sql = "select * from message where posted_by=?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int message_id = rs.getInt("message_id");
                int posted_by = rs.getInt("posted_by");
                String message_text = rs.getString("message_text");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                list.add(new Message(message_id, posted_by, message_text, time_posted_epoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        };
        return list;
    }

}
