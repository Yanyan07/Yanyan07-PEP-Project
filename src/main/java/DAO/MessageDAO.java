package DAO;

import java.util.List;
import Model.Message;

public interface MessageDAO {
    Message addMessage(Message message);
    List<Message> getAllMessages();
    Message getMessageById(int id);
    Message deleteMessageById(int id);
    Message updateMessageById(int id, String text);
    List<Message> getAllMessagesByAccount(int account_id);
}
